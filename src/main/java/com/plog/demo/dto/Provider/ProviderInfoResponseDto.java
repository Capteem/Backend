package com.plog.demo.dto.Provider;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ProviderInfoResponseDto {

    private int providerId;
    private String providerName;
    private String providerPhoneNum;
    private int providerPrice;
}
