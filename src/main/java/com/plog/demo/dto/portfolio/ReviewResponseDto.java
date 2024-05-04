package com.plog.demo.dto.portfolio;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ReviewResponseDto {

    private int reviewId;

    private String reviewContent;

    private int reviewScore;

    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime reviewDate;

    private CommentResponseDto comment;
}
