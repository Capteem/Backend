package com.plog.demo.dto.file;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class ProviderCheckFileDto {

    private String originFileName;

    private String storeFileName;
}
