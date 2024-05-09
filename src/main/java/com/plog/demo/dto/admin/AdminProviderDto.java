package com.plog.demo.dto.admin;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@AllArgsConstructor
@Builder
public class AdminProviderDto {

    private Integer providerId;
    private int providerStatus;

}
