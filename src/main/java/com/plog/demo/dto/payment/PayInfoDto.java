package com.plog.demo.dto.payment;

import lombok.*;

@Getter
@Setter
@RequiredArgsConstructor
@AllArgsConstructor
@Builder
public class PayInfoDto {
    private String userId;
    private int purchaseAmount;
    private String purchaseName;
}
