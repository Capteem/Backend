package com.plog.demo.dto.portfolio;

import io.swagger.v3.oas.annotations.responses.ApiResponse;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PortfolioRandomResponseDto {

    private int providerId;
    private String imgUrl;
}
