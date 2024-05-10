package com.plog.demo.dto.portfolio;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PortfolioImageDto {

    private String fileExtension;

    private String imgFullPath;
}
