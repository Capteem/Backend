package com.plog.demo.dto.Provider;

import com.plog.demo.dto.workdate.DateListDto;
import lombok.*;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ProviderListDto {

    private int providerId;
    private String providerName;
    private String providerPhone;
    private String providerAddress;
    private int providerPrice;
    private int providerType;
    private String providerRepPhoto;
    private String providerRepPhotoPath;

    private List<DateListDto> dateList;
}
