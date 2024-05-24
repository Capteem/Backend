package com.plog.demo.dto.Provider;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ProviderInfoDto {

    private String providerName;
    private String providerPhoneNum;
    private String providerAddress;
    private String providerRepPhotoPath;
    private String providerRepPhoto;
    private int providerPrice;
}
