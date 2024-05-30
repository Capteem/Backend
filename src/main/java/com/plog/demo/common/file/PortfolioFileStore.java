package com.plog.demo.common.file;

import com.plog.demo.dto.file.DeleteFileDto;
import com.plog.demo.dto.file.UploadFileDto;
import com.plog.demo.exception.CustomException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
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
public class PortfolioFileStore {

    @Value("${portfolio.upload.dir}")
    private String portfolioFileDir;


    public String getFullPath(String fileMiddleDir, String fileName){
        return portfolioFileDir + fileMiddleDir + "/" + fileName;
    }

    //임시
    public String getFileDirPath(){
        return portfolioFileDir;
    }

    public List<UploadFileDto> storeFiles(List<MultipartFile> multipartFiles) {

        log.info("[storeFiles] 파일 저장 로직 시작");
        List<UploadFileDto> uploadFileDtos = new ArrayList<>();

        try {
            // 현재 날짜를 포함한 경로 생성 -> 날짜별로 이미지 모을거
            String currentDate = new SimpleDateFormat("yyyyMMdd").format(new Date());
            String uploadPath = portfolioFileDir + currentDate + "/";

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

    public boolean deleteFile(DeleteFileDto deleteFileDto){
        log.info("[deleteFiles] 파일 삭제 로직 시작");

        try{
            File storedFile = new File(portfolioFileDir + deleteFileDto.getImgPath() + deleteFileDto.getStoredFileName());

            if(storedFile.exists()){
                if(storedFile.delete()) {
                    log.info("삭제 성공: {}", storedFile);
                }else {
                    log.error("삭제 실패: {}", storedFile);
                    return false;
                }
            }else {
                log.warn("삭제할 파일이 존재하지 않습니다. {}", storedFile);
            }

        }catch (Exception e){
            log.error("파일 삭제 중 에러 발생: {}", e.getMessage());
            return false;
        }


        log.info("[deleteFiles] 파일 삭제 완료");
        return true;
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
    public String extractExt(String originalFileName){
        int pos = originalFileName.lastIndexOf(".");
        return originalFileName.substring(pos+1);
    }

    /**
     * 파일 확장자 jpg or png만 허용
     */
    public boolean isNotSupportedExtension(String fileExtension) {
        return !fileExtension.equalsIgnoreCase("jpg") && !fileExtension.equalsIgnoreCase("png");
    }

    public void validateFiles(List<MultipartFile> files) throws CustomException {
        for(MultipartFile file : files){
            String originalFilename = file.getOriginalFilename();

            if(originalFilename == null){
                throw new CustomException("파일 이름이 존재하지 않습니다.", HttpStatus.BAD_REQUEST.value());
            }

            String fileExtension = extractExt(originalFilename);

            if(isNotSupportedExtension(fileExtension)){
                throw new CustomException("지원하지 않는 파일 형식입니다.", HttpStatus.BAD_REQUEST.value());
            }
        }
    }
}
