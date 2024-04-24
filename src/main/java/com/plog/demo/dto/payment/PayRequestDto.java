package com.plog.demo.dto.payment;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.util.LinkedMultiValueMap;

@Getter
@AllArgsConstructor
public class PayRequestDto {
    private String url;
    private LinkedMultiValueMap<String, Object> map;
}
