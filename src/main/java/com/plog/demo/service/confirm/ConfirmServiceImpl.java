package com.plog.demo.service.confirm;

import com.plog.demo.common.file.ProviderCheckFileStore;
import com.plog.demo.dto.confirm.ConfirmCheckProviderRequestDto;
import com.plog.demo.dto.confirm.ConfirmGetCheckFilesDto;
import com.plog.demo.dto.confirm.ConfirmImageDto;
import com.plog.demo.dto.confirm.ConfirmResponseDto;
import com.plog.demo.dto.file.ProviderCheckFileDto;
import com.plog.demo.dto.user.CheckAuthDto;
import com.plog.demo.exception.CustomException;
import com.plog.demo.model.AuthTable;
import com.plog.demo.model.IdTable;
import com.plog.demo.model.ProviderCheckTable;
import com.plog.demo.repository.AuthTableRepository;
import com.plog.demo.repository.IdTableRepository;
import com.plog.demo.repository.ProviderCheckTableRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
@Transactional
public class ConfirmServiceImpl implements ConfirmService{

    private final ProviderCheckTableRepository providerCheckTableRepository;
    private final IdTableRepository idTableRepository;
    private final ProviderCheckFileStore providerCheckFileStore;

    @Autowired
    private final JavaMailSender mailSender;
    private final AuthTableRepository authTableRepository;
    private static final String SENDER_EMAIL = "plog20240520@gmail.com";
    private static int AUTH_NUMBER;

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
                getProviderCheckTable(providerCheckFileDto, idTable, confirmCheckProviderRequestDto.getUuid())
        ).toList();

        try {
            providerCheckTableRepository.saveAll(providerCheckTables);
        }catch (Exception e){
            throw new RuntimeException("서비스 등록용 파일을 db에 저장하는데 오류", e);
        }

    }

    @Override
    public ConfirmGetCheckFilesDto getCheckfileUrls(String uuid) throws CustomException {



        List<ProviderCheckTable> providerCheckTables = providerCheckTableRepository.findAllByProviderUuid(uuid);

        if(providerCheckTables.isEmpty()){
            throw new CustomException("파일이 존재하지 않습니다.", HttpStatus.NOT_FOUND.value());
        }


        List<String> fileNames = providerCheckTables.stream()
                .map(providerCheckTable -> providerCheckTable.getStoredFileName())
                .toList();


        return ConfirmGetCheckFilesDto.builder()
                .uuid(uuid)
                .fileNameList(fileNames)
                .build();

    }

    @Override
    public ConfirmImageDto getImage(String fileName) throws CustomException {
        String fileExtension = providerCheckFileStore.extractExt(fileName);

        if(providerCheckFileStore.isNotSupportedExtension(fileExtension)){
            throw new CustomException("지원되지 않는 파일 형식입니다.", HttpStatus.BAD_REQUEST.value());
        }

        return ConfirmImageDto.builder()
                .imgFullPath(providerCheckFileStore.getFullPath(fileName))
                .fileExtension(fileExtension)
                .build();
    }

    @Override
    public boolean deleteFiles(String uuid) throws CustomException {

        List<ProviderCheckTable> providerCheckTables = providerCheckTableRepository.findAllByProviderUuid(uuid);


        //파일 삭제 & 디비 삭제
        for(ProviderCheckTable providerCheckTable : providerCheckTables){
            if(!providerCheckFileStore.deleteFile(providerCheckTable.getStoredFileName())){
                return false;
            }
            try {
                providerCheckTableRepository.deleteById(providerCheckTable.getProviderCheckId());
            }catch (Exception e){
                throw new RuntimeException("서비스 등록용 파일정보를 db에서 삭제하는 중 에러", e);
            }
        }
        return true;
    }

    public static void createNumber() {
        AUTH_NUMBER = (int)(Math.random() * (900000)) + 100000; //(int) Math.random() * (최댓값-최소값+1) + 최소값
    }

    @Override
    public void joinEmail(String email) throws CustomException {
        createNumber();
        String subject = "PLOG 인증번호입니다.";
        String text = "인증번호는 " + AUTH_NUMBER + "입니다.";
        sendEmail(SENDER_EMAIL, email, subject, text);
    }

    @Override
    public void sendEmail(String setFrom, String toMail, String title, String content) throws CustomException {
        MimeMessage message = mailSender.createMimeMessage();
        try {
            MimeMessageHelper messageHelper = new MimeMessageHelper(message, true, "UTF-8");
            messageHelper.setFrom(setFrom);
            messageHelper.setTo(toMail);
            messageHelper.setSubject(title);
            messageHelper.setText(content, true);
            mailSender.send(message);
            AuthTable authTable = AuthTable.builder()
                    .email(toMail)
                    .auth(AUTH_NUMBER)
                    .createdAt(java.time.LocalDateTime.now())
                    .build();
            authTableRepository.save(authTable);
        } catch (MessagingException e) {
            throw new CustomException("메일 전송 중 오류가 발생했습니다.", HttpStatus.BAD_REQUEST.value());
        }
    }

    private int validate(CheckAuthDto checkAuthDto){
        if(checkAuthDto.getEmail() == null || checkAuthDto.getAuth() == 0){
            return -1;
        }
        if(checkAuthDto.getUserId() == null){
            return 0;
        }
        return 1;
    }

    @Override
    public String checkAuthNumber(CheckAuthDto checkAuthDto) throws CustomException{
        int check = validate(checkAuthDto);
        if(check == -1){
            throw new CustomException("이메일 또는 인증번호가 입력되지 않았습니다.", HttpStatus.BAD_REQUEST.value());
        }
        if(check == 0){
            return checkAuthNumberWithoutId(checkAuthDto);
        }else{
            return checkAuthNumberWithId(checkAuthDto);
        }
    }

    private String checkAuthNumberWithId(CheckAuthDto checkAuthDto) throws CustomException {
        IdTable idTable = idTableRepository.findById(checkAuthDto.getUserId())
                .orElseThrow(() -> new CustomException("존재하지 않는 회원입니다.", HttpStatus.NOT_FOUND.value()));
        AuthTable authTable = authTableRepository.findByEmail(checkAuthDto.getEmail());
        if(authTable == null){
            throw new CustomException("인증번호가 일치하지 않습니다.", HttpStatus.BAD_REQUEST.value());
        }
        if(authTable.getAuth() == checkAuthDto.getAuth()){
            authTableRepository.delete(authTable);
            return idTable.getId();
        }else{
            throw new CustomException("인증번호가 일치하지 않습니다.", HttpStatus.BAD_REQUEST.value());
        }
    }

    private String checkAuthNumberWithoutId(CheckAuthDto checkAuthDto) throws CustomException {//
        AuthTable authTable = authTableRepository.findByEmail(checkAuthDto.getEmail());
        if(authTable == null){
            throw new CustomException("인증번호가 일치하지 않습니다.", HttpStatus.BAD_REQUEST.value());
        }
        if(authTable.getAuth() == checkAuthDto.getAuth()){
            authTableRepository.delete(authTable);
            return idTableRepository.findByEmail(checkAuthDto.getEmail()).getId();
        }else{
            throw new CustomException("인증번호가 일치하지 않습니다.", HttpStatus.BAD_REQUEST.value());
        }
    }

    private ProviderCheckTable getProviderCheckTable(ProviderCheckFileDto providerCheckFileDto, IdTable idTable, String uuid) {
        return ProviderCheckTable.builder()
                .id(idTable)
                .storedFileName(providerCheckFileDto.getStoreFileName())
                .providerUuid(uuid)
                .build();
    }
}
