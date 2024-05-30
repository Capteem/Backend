package com.plog.demo.dto.portfolio;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@Builder
public class PortfolioAddDto {
    private String portfolioRepPhoto;
    private List<MultipartFile> portfolioPhotoPath;
}
