package com.plog.demo.dto.Provider;

import lombok.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ProviderCheckRequestDto{

    private String providerName;
    private String providerArea;
    private String providerSubArea;
    private String providerDetail;
    private int providerType;
    private int providerStatus;
    private String userId;
    private String providerPhoneNum;

    List<MultipartFile> providerCheckFiles;
}
