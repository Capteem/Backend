package com.plog.demo.dto.Provider;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProviderDto {

    private String providerName;
    private String providerArea;
    private String providerSubArea;
    private String providerDetail;
    private int providerType;
    private int providerStatus;
    private String userId;
    private String providerPhoneNum;

}
