package com.plog.demo.dto.Provider;

import com.plog.demo.dto.workdate.DateListDto;
import lombok.*;

import java.util.List;

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
    private List<DateListDto> dateList;
}
