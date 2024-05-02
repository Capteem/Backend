package com.plog.demo.dto.payment;

import com.plog.demo.dto.reservation.ReservationRequestDto;
import lombok.*;

@Getter
@Setter
@RequiredArgsConstructor
@AllArgsConstructor
@Builder
public class PayInfoDto {
    private int purchaseAmount;
    private String purchaseName;
    private ReservationRequestDto reservationRequestDto;
}
