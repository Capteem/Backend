package com.plog.demo.dto.confirm;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
@Builder
@AllArgsConstructor
public class ConfirmRequestDto {
    private String businessId;
}
