package com.plog.demo.common;

import lombok.Getter;

@Getter
public enum ProviderStatus {

    WAITING(0, "심사 중"),
    COMPLETED(1, "등록 완료");

    private final int value;

    private final String description;

    ProviderStatus(int value, String description){
        this.value = value;
        this.description = description;
    }
}
