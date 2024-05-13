package com.plog.demo.dto.portfolio;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PortfolioGetDto {

    private int portfolioId;

    private String imgUrl;
}
