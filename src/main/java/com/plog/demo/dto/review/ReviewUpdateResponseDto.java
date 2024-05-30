package com.plog.demo.dto.review;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ReviewUpdateResponseDto {

    private int reviewId;

    private String reviewContent;

    private int reviewScore;

    private String userId;

    private String userNickName;

    private int providerId;

    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime reviewDate;
}
