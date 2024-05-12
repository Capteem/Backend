package com.plog.demo.dto.reservation;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Setter
public class ReservationResponseDto {

    private int reservationTableId;

    private int reservationCameraId;

    private String reservationCameraName;

    private int reservationStudioId;

    private String reservationStudioName;

    private int reservationHairId;

    private String reservationHairName;

    private int amount;

    private int status;

    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime reservationStartDate;

    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime reservationEndDate;
}