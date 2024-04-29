package com.plog.demo.dto.payment;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.util.LinkedMultiValueMap;

import java.util.Map;

@Getter
@AllArgsConstructor
public class PayRequestDto {
    private String url;
    private Map<String, Object> map;
}
