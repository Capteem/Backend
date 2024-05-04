package com.plog.demo.dto.portfolio;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
public class PortfolioDto {

    private int portfolioId;

    private String portfolioTitle;

}
