package com.plog.demo.dto.portfolio;


import lombok.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Setter
public class PortfolioUploadDto {

    private int providerId;

    private List<MultipartFile> portfolioUploadFiles;
}
