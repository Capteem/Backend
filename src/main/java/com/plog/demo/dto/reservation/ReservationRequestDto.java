package com.plog.demo.dto.reservation;
import lombok.*;

import java.time.LocalDateTime;


@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Setter
public class ReservationRequestDto {

    private int reservationCameraId;

    private int reservationStudioId;

    private int reservationHairId;

    private LocalDateTime reservationStartDate;

    private LocalDateTime reservationEndDate;

}
