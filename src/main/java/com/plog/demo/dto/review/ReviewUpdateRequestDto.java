package com.plog.demo.dto.review;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ReviewUpdateRequestDto {

    private int reviewId;

    private String reviewContent;

    private int reviewScore;

    private String userId;

    private String userNickName;

    private LocalDateTime reviewDate;

}
