package com.plog.demo.dto.Provider;


import lombok.*;
import org.springframework.web.multipart.MultipartFile;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ProviderRepRequestDto {

    private int providerId;

    private MultipartFile providerRepFile;
}
