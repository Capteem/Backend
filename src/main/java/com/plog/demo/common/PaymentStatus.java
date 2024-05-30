package com.plog.demo.common;

import lombok.Getter;

@Getter
public enum PaymentStatus {
    COMPLETE(1, "결제 완료"),
    CANCEL(2, "결제 취소"),
    FAIL(3, "결제 실패");

    private final int code;
    private final String description;

    PaymentStatus(int code, String description){
        this.code = code;
        this.description = description;
    }

}
