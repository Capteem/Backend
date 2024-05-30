package com.plog.demo.dto.confirm;

import lombok.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ConfirmCheckProviderRequestDto {

    private String userId;

    List<MultipartFile> providerCheckFiles;

    private String uuid;
}
