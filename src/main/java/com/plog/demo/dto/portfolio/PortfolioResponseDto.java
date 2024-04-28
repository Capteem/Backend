package com.plog.demo.dto.portfolio;

import lombok.*;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PortfolioResponseDto {

    private int providerId;

    private List<ReviewResponseDto> reviewList;

    private List<String> imgUrlList;
}
