package com.plog.demo.dto.review;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.plog.demo.dto.review.comment.CommentGetDto;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@Builder
public class ReviewGetDto {

    private int reviewId;

    private String reviewContent;

    private int reviewScore;

    private String userId;

    private String userNickName;


    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime reviewDate;

    private CommentGetDto comment;
}
