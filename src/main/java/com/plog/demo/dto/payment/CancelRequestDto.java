package com.plog.demo.dto.payment;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@Builder
public class CancelRequestDto {
    private String tid;
    private String userId;
}
