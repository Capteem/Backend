package com.plog.demo.dto.complaint;

import lombok.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ComplaintPhotoDto {

    List<MultipartFile> complaintCheckFiles;

    private String uuid;

}
