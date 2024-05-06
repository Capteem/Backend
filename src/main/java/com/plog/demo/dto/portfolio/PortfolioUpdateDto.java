package com.plog.demo.dto.portfolio;


import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PortfolioUpdateDto {

    private int portfolioId;

    private String portfolioTitle;
}
