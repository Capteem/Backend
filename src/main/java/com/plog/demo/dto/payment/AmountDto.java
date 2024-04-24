package com.plog.demo.dto.payment;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
@AllArgsConstructor
public class AmountDto {

    private int total;
    private int tax_free;
    private int vat;
    private int point;
    private int discount;
    private int green_deposit;

}
