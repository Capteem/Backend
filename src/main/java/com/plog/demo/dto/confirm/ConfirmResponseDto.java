package com.plog.demo.dto.confirm;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@Builder
public class ConfirmResponseDto {
    private String status_code;
    private int request_cnt;
    private int match_cnt;
    private ConfirmResponseDataDto confirmResponseDataDto;
}
