package com.plog.demo.dto.review.comment;

import lombok.*;

import java.time.LocalDateTime;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CommentUpdateRequestDto {

    private int commentId;

    private String commentContent;

    private LocalDateTime commentDate;
}
