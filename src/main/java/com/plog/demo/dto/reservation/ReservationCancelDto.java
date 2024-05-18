package com.plog.demo.dto.reservation;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ReservationCancelDto {

    private int reservationId;
    private String userId;
    private String providerId;
    private String cancelReason;
}
