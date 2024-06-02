package com.plog.demo.dto.review;


import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ReviewCheckWriteRequestDto {

    private String userId;

    private int providerId;

    private int reservationId;
}
