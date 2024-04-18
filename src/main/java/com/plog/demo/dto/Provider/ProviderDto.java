package com.plog.demo.dto.Provider;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProviderDto {

    private String providerName;
    private String providerAddress;
    private int providerService;

}
