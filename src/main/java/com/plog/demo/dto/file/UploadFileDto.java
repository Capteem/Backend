package com.plog.demo.dto.file;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UploadFileDto {

    //파일 원본 이름
    private String originalFileName;

    //서버에 저장용 파일 이름
    private String storeFileName;

    //서버에 저장할 날짜 디렉토리
    private String dateBasedImagePath;
}
