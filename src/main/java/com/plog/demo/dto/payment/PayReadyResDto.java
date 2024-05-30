package com.plog.demo.dto.payment;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@Builder
public class PayReadyResDto {
    private String tid;
    private String next_redirect_pc_url;
    private String created_at;
}
