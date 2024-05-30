package com.plog.demo.dto.review.comment;

import lombok.*;

import java.time.LocalDateTime;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class CommentAddRequestDto {

    private int reviewId;

    private String commentContent;

    private LocalDateTime commentDate;
}
