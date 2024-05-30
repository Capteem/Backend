package com.plog.demo.dto.reservation;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ReservationProviderResponseDto {

    private int reservationId;
    private String userId;
    private int providerId;

}
