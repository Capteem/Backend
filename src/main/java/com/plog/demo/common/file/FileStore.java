package com.plog.demo.common.file;

import com.plog.demo.dto.file.UploadFileDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@Component
@Slf4j
public class FileStore {

    @Value("${upload.dir}")
    private String fileDir;


    public String getFullPath(String currentDateDir, String filename){
        return fileDir + currentDateDir + filename;
    }

    public List<UploadFileDto> storeFiles(List<MultipartFile> multipartFiles) {

        log.info("[storeFiles] 파일 저장 로직 시작");
        List<UploadFileDto> uploadFileDtos = new ArrayList<>();

        try {
            // 현재 날짜를 포함한 경로 생성 -> 날짜별로 이미지 모을거
            String currentDate = new SimpleDateFormat("yyyyMMdd").format(new Date());
            String uploadPath = fileDir + currentDate + "/";

            // 디렉토리 생성 (존재하지 않을 경우, 모든 path 디렉토리 자동 생성)
            Files.createDirectories(Paths.get(uploadPath));

            for (MultipartFile multipartFile : multipartFiles) {
                if (!multipartFile.isEmpty()) {
                    //저장로직
                    uploadFileDtos.add(storeFile(multipartFile, uploadPath, currentDate));
                }
            }
        }catch (IOException e){
            log.error("[storeFiles] 파일 저장 오류 발생 {}", e.getMessage());
        }

        log.info("[storeFiles] 파일 저장 성공");

        return uploadFileDtos;
    }

    private UploadFileDto storeFile(MultipartFile multipartFile, String uploadPath, String currentDate) throws IOException {


        String originalFileName = multipartFile.getOriginalFilename();
        String storeFileName = createStoreFileName(originalFileName);
        multipartFile.transferTo(new File(uploadPath + storeFileName));

        return UploadFileDto.builder()
                .originalFileName(originalFileName)
                .storeFileName(storeFileName)
                .dateBasedImagePath(currentDate+"/") // 날짜 폴더 저장
                .build();
    }


    /**
     * 서버에 저장할 때 파일 이름 생성 (중복 방지를 위해)
     */
    private String createStoreFileName(String originalFileName){

        String ext = extractExt(originalFileName);

        //저장파일 중복 방지
        String uuid = UUID.randomUUID().toString();

        return uuid + "." + ext;
    }


    /**
     * 파일 확장자 이름 추출
     */
    private String extractExt(String originalFileName){
        int pos = originalFileName.lastIndexOf(".");
        return originalFileName.substring(pos+1);
    }

}
