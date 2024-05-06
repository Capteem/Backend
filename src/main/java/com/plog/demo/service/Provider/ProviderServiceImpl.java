package com.plog.demo.service.Provider;

import com.plog.demo.common.ProviderStatus;
import com.plog.demo.common.file.ProviderCheckFileStore;
import com.plog.demo.dto.Provider.ProviderAdminDto;
import com.plog.demo.dto.Provider.ProviderCheckRequestDto;
import com.plog.demo.dto.Provider.ProviderDto;
import com.plog.demo.dto.Provider.ProviderResponseDto;
import com.plog.demo.dto.file.ProviderCheckFileDto;
import com.plog.demo.exception.CustomException;
import com.plog.demo.model.IdTable;
import com.plog.demo.model.ProviderCheckTable;
import com.plog.demo.model.ProviderTable;
import com.plog.demo.repository.IdTableRepository;
import com.plog.demo.repository.ProviderCheckTableRepository;
import com.plog.demo.repository.ProviderTableRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class ProviderServiceImpl implements ProviderService{

    private final ProviderTableRepository providerTableRepository;
    private final IdTableRepository idTableRepository;
    private final ProviderCheckFileStore providerCheckFileStore;
    private final ProviderCheckTableRepository providerCheckTableRepository;

    @Override
    public ProviderDto addProvider(ProviderDto providerDto) throws CustomException{

        IdTable idTable = idTableRepository.findById(providerDto.getUserId()).orElseThrow(() -> new CustomException("존재하지 않는 사용자입니다."));

        ProviderTable providerTable = ProviderTable.builder()
                .userId(idTable)
                .providerName(providerDto.getProviderName())
                .providerType(providerDto.getProviderType())
                .providerArea(providerDto.getProviderArea())
                .providerSubArea(providerDto.getProviderSubArea())
                .providerDetailArea(providerDto.getProviderDetail())
                .providerPhoneNum(providerDto.getProviderPhoneNum())
                .providerStatus(1)
                .build();

        try{
            log.info("[addProvider] save제공자 저장 로직 시작");
            providerTableRepository.save(providerTable);
        }catch (Exception e){
            log.info("[addProvider] db데이터 베이스 접근 오류");
            throw new RuntimeException("데이터베이스 접근 중 오류가 발생했습니다.", e);
        }

        return providerDto;
    }

    @Override
    public List<ProviderResponseDto> getSelectedProvider(String userId) throws CustomException{

        IdTable idTable = idTableRepository.findById(userId).orElseThrow(() -> new CustomException("존재하지 않는 사용자입니다."));
        try{
            log.info("[getProvider] 제공자 조회 로직 시작");
            List<ProviderTable> providerTables = providerTableRepository.findAllByUserId(idTable);
            List<ProviderResponseDto> providerDtos = providerTables.stream().map(providerTable -> ProviderResponseDto.builder()
                    .providerName(providerTable.getProviderName())
                    .providerType(providerTable.getProviderType())
                    .providerStatus(providerTable.getProviderStatus())
                    .build()).toList();
            if(providerTables.isEmpty()){
                log.info("[getProvider] 제공자가 존재하지 않습니다.");
                throw new CustomException("제공자가 존재하지 않습니다.");
            }
            return providerDtos;
        } catch (Exception e){
            log.info("[getProvider] db데이터 베이스 접근 오류");
            throw new RuntimeException("데이터베이스 접근 중 오류가 발생했습니다.", e);
        }
    }

    @Override
    public List<ProviderAdminDto> getProviderList() throws CustomException{

        try{
            log.info("[getProviderList] 제공자 리스트 조회 로직 시작");
            List<ProviderTable> providerTables = providerTableRepository.findAll();
            List<ProviderAdminDto> providerDtos = providerTables.stream().map(providerTable -> ProviderAdminDto.builder()
                    .providerName(providerTable.getProviderName())
                    .providerType(providerTable.getProviderType())
                    .providerStatus(providerTable.getProviderStatus())
                    .providerArea(providerTable.getProviderArea() + " " + providerTable.getProviderSubArea() + " " + providerTable.getProviderDetailArea())
                    .providerPhoneNum(providerTable.getProviderPhoneNum())
                    .providerId(providerTable.getProviderId())
                    .build()).toList();
            if(providerTables.isEmpty()){
                log.info("[getProviderList] 제공자가 존재하지 않습니다.");
                throw new CustomException("제공자가 존재하지 않습니다.");
            }
            return providerDtos;
        } catch (Exception e){
            log.info("[getProviderList] db데이터 베이스 접근 오류");
            throw new RuntimeException("데이터베이스 접근 중 오류가 발생했습니다.", e);
        }
    }

    @Override
    public void checkProvider(ProviderCheckRequestDto providerCheckRequestDto) throws CustomException {

        IdTable idTable = idTableRepository.findById(providerCheckRequestDto.getUserId())
                .orElseThrow(() -> new CustomException("존재하지 않는 유저입니다.", HttpStatus.NOT_FOUND.value()));
        //체크 파일 업로드
        List<ProviderCheckFileDto> providerCheckFileDtos = providerCheckFileStore
                .storeFiles(providerCheckRequestDto.getProviderCheckFiles());

        //체크파일테이블 등록
        providerCheckFileDtos.forEach(providerCheckFileDto->{
            try {
                providerCheckTableRepository.save(getProviderCheckTable(providerCheckFileDto, idTable));
            } catch (Exception e) {
                log.warn("[checkProvider] 데이터 베이스 저장 오류");
                throw new RuntimeException("데이터 베이스 저장 오류", e);
            }
        });

        //제공자 등록
        ProviderTable providerTable = ProviderTable.builder()
                .providerName(providerCheckRequestDto.getProviderName())
                .providerPhoneNum(providerCheckRequestDto.getProviderPhoneNum())
                .providerArea(providerCheckRequestDto.getProviderArea())
                .providerSubArea(providerCheckRequestDto.getProviderSubArea())
                .providerDetailArea(providerCheckRequestDto.getProviderDetail())
                .providerStatus(ProviderStatus.WAITING.getValue())
                .providerType(providerCheckRequestDto.getProviderType())
                .userId(idTable)
                .build();

        try {
            providerTableRepository.save(providerTable);
        }catch (Exception e){
            log.warn("[checkProvider] 데이터 베이스 저장 오류");
            throw new RuntimeException("데이터 베이스 저장 오류", e);
        }
    }

    private ProviderCheckTable getProviderCheckTable(ProviderCheckFileDto providerCheckFileDto, IdTable idTable) {
        return ProviderCheckTable.builder()
                .id(idTable)
                .storedFileName(providerCheckFileDto.getStoreFileName())
                .build();
    }
}
