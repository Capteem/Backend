package com.plog.demo.dto.admin;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Getter
@Builder
@AllArgsConstructor
public class AdminRequestDto {

    private String type;
    private String userId;
    private Integer providerId;
    private int status;

}
