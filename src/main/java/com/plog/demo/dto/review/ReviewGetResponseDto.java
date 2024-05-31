package com.plog.demo.dto.review;

import lombok.*;

import java.util.List;

@Builder
@AllArgsConstructor
@Getter
@Setter
public class ReviewGetResponseDto {


    private List<ReviewGetDto> reviewList;
}
