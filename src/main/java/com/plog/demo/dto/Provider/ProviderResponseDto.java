package com.plog.demo.dto.Provider;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@Builder
public class ProviderResponseDto {
    private int providerId;
    private String providerName;
    private int providerType;
    private String providerAddress;
    private String providerPhoneNum;
}
