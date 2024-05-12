package com.plog.demo.dto.payment;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@Builder
public class PaymentInfoDto {

    private String userId;
    private String paymentDate;
    private int paymentAmount;
    private String paymentType;
    private int paymentPoint;

}
