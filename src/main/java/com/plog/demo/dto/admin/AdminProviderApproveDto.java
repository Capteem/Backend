package com.plog.demo.dto.admin;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

@Getter
@AllArgsConstructor
@Slf4j
@Builder
public class AdminProviderApproveDto {

    private int providerId;
    private String userId;
}
