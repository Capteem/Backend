package com.plog.demo.service.confirm;

import com.plog.demo.common.file.ProviderCheckFileStore;
import com.plog.demo.dto.confirm.ConfirmCheckProviderRequestDto;
import com.plog.demo.dto.confirm.ConfirmResponseDto;
import com.plog.demo.dto.file.ProviderCheckFileDto;
import com.plog.demo.exception.CustomException;
import com.plog.demo.model.IdTable;
import com.plog.demo.model.ProviderCheckTable;
import com.plog.demo.repository.IdTableRepository;
import com.plog.demo.repository.ProviderCheckTableRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

import java.net.URI;
import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
@Transactional
public class ConfirmServiceImpl implements ConfirmService{

    private final ProviderCheckTableRepository providerCheckTableRepository;
    private final IdTableRepository idTableRepository;
    private final ProviderCheckFileStore providerCheckFileStore;

    @Value("${openapi.authkey}")
    private String authKey;
    @Override
    public ConfirmResponseDto getBusinessStatus(String businessNumber) throws CustomException {

        String baseUrl = "https://api.odcloud.kr/api/nts-businessman/v1/status?serviceKey=" + authKey;

        URI uri = URI.create(baseUrl);

        LinkedMultiValueMap<String, String> jsonMap = new LinkedMultiValueMap<>();
        HttpHeaders headers = new HttpHeaders();
        ConfirmResponseDto confirmResponseDto;
        jsonMap.add("b_no", businessNumber);

        headers.set("Content-Type", "application/json");
        headers.set("accept", "*/*");

        HttpEntity<LinkedMultiValueMap<String, String>> request = new HttpEntity<>(jsonMap, headers);
        try {
            RestTemplate restTemplate = new RestTemplate();
            confirmResponseDto = restTemplate.postForObject(uri, request, ConfirmResponseDto.class);
            return confirmResponseDto;
        } catch (HttpClientErrorException e) {
            log.info("[getBusinessStatus]"+e.getMessage());
            throw new CustomException("[getBusinessStatus] failure to get business status");
        }
    }

    @Override
    public void checkProvider(ConfirmCheckProviderRequestDto confirmCheckProviderRequestDto) throws CustomException {

        List<MultipartFile> providerCheckFiles = confirmCheckProviderRequestDto.getProviderCheckFiles();

        //파일 검증
        providerCheckFileStore.validateFiles(providerCheckFiles);


        IdTable idTable = idTableRepository.findById(confirmCheckProviderRequestDto.getUserId())
                .orElseThrow(() -> new CustomException("존재하지 않는 유저입니다.", HttpStatus.NOT_FOUND.value()));

        //체크 파일 업로드
        List<ProviderCheckFileDto> providerCheckFileDtos = providerCheckFileStore
                .storeFiles(confirmCheckProviderRequestDto.getProviderCheckFiles());


        //체크파일테이블 등록
        List<ProviderCheckTable> providerCheckTables = providerCheckFileDtos.stream().map(providerCheckFileDto ->
                getProviderCheckTable(providerCheckFileDto, idTable)
        ).toList();

        providerCheckTableRepository.saveAll(providerCheckTables);


    }

    @Override
    public boolean deleteFiles(String userId) throws CustomException {

        IdTable idTable = idTableRepository.findById(userId)
                .orElseThrow(() -> new CustomException("존재하지 않은 유저입니다.", HttpStatus.NOT_FOUND.value()));

        //디비에서 파일 경로, 이름 가져오기
        List<ProviderCheckTable> providerCheckTables = providerCheckTableRepository.findAllById(idTable);

        //파일 삭제 & 디비 삭제
        for(ProviderCheckTable providerCheckTable : providerCheckTables){
            if(!providerCheckFileStore.deleteFile(providerCheckTable.getStoredFileName())){
                return false;
            }
            providerCheckTableRepository.deleteById(providerCheckTable.getProviderCheckId());
        }


        return true;
    }

    private ProviderCheckTable getProviderCheckTable(ProviderCheckFileDto providerCheckFileDto, IdTable idTable) {
        return ProviderCheckTable.builder()
                .id(idTable)
                .storedFileName(providerCheckFileDto.getStoreFileName())
                .build();
    }
}
