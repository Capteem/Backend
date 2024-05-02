package com.plog.demo.dto.Provider;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@Builder
public class ProviderAdminDto {
    private int providerId;
    private String providerName;
    private String providerArea;
    private int providerType;
    private String providerPhoneNum;
    private int providerStatus;
}
