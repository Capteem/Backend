package com.plog.demo.dto.reservation;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Builder
public class ReservationDeleteDto {

    private String reservationId;
    private String userId;

}
