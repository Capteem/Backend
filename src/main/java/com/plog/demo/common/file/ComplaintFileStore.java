package com.plog.demo.common.file;

import com.plog.demo.dto.file.ProviderCheckFileDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Component
@Slf4j
public class ComplaintFileStore {

    @Value("${complaint.upload.dir}")
    private String complaintFileDir;

    public String getFullPath(String fileName){
        return complaintFileDir + fileName;
    }

    public List<ProviderCheckFileDto> storeFiles(List<MultipartFile> multipartFiles) {

        log.info("[storeFiles] 파일 저장 로직 시작 start");
        List<ProviderCheckFileDto> providerCheckFileDtos = new ArrayList<>();

        try {

            String uploadPath = complaintFileDir;
            // 디렉토리 생성 (존재하지 않을 경우, 모든 path 디렉토리 자동 생성)
            Files.createDirectories(Paths.get(uploadPath));

            for (MultipartFile multipartFile : multipartFiles) {
                if (!multipartFile.isEmpty()) {
                    //저장로직
                    providerCheckFileDtos.add(storeFile(multipartFile, uploadPath));
                }
            }
        }catch (IOException e){
            log.error("[storeFiles] error {}", e.getMessage());
        }

        log.info("[storeFiles] success");

        return providerCheckFileDtos;
    }

    private ProviderCheckFileDto storeFile(MultipartFile multipartFile, String uploadPath) throws IOException {


        String originalFileName = multipartFile.getOriginalFilename();
        String storeFileName = createStoreFileName(originalFileName);
        multipartFile.transferTo(new File(uploadPath + storeFileName));

        return ProviderCheckFileDto.builder()
                .originFileName(originalFileName)
                .storeFileName(storeFileName)
                .build();
    }

    private String createStoreFileName(String originalFileName){

        String ext = extractExt(originalFileName);

        //저장파일 중복 방지
        String uuid = UUID.randomUUID().toString();

        return uuid + "." + ext;
    }

    public String extractExt(String originalFileName){

        int pos = originalFileName.lastIndexOf(".");
        return originalFileName.substring(pos+1);

    }


}
