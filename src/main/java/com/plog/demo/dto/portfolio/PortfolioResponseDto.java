package com.plog.demo.dto.portfolio;

import com.plog.demo.dto.review.ReviewGetDto;
import com.plog.demo.dto.review.ReviewGetResponseDto;
import lombok.*;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PortfolioResponseDto {

    private int providerId;

    private List<PortfolioGetDto> portfolioList;

    private List<ReviewGetDto> reviewList;

}
