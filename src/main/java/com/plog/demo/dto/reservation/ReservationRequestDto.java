package com.plog.demo.dto.reservation;

import lombok.*;

import java.time.LocalDateTime;


@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Setter
public class ReservationRequestDto {

    private String userId;

    private int reservationCameraId;

    private String reservationCameraName;

    private int reservationStudioId;

    private String reservationStudioName;

    private int reservationHairId;

    private String reservationHairName;

    private LocalDateTime reservationStartDate;

    private LocalDateTime reservationEndDate;

    public String toString(){
        return "userId: " + userId + ", reservationCameraId: " + reservationCameraId + ", reservationCameraName: " + reservationCameraName + ", reservationStudioId: " + reservationStudioId + ", reservationStudioName: " + reservationStudioName + ", reservationHairId: " + reservationHairId + ", reservationHairName: " + reservationHairName + ", reservationStartDate: " + reservationStartDate + ", reservationEndDate: " + reservationEndDate;
    }

}
