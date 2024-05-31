package com.plog.demo.dto.review;

import com.plog.demo.model.ProviderTable;
import jakarta.persistence.Column;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ReviewAddRequestDto {

    private String reviewContent;

    private int reviewScore;

    private String userId;

    private String userNickName;

    private LocalDateTime reviewDate;

    private int reservationId;

    private int providerId;
}
