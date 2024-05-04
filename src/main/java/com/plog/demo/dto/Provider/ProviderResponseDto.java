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
    private String providerName;
    private int providerType;
    private int providerStatus;
}
