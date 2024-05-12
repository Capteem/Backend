package com.plog.demo.common;

import lombok.Getter;

@Getter
public enum ComplaintType {

    RESERVATION(1, "예약"),
    PAYMENT(2, "결제"),
    SCAM(3, "사기"),
    ETC(4, "기타");

    private final int code;
    private final String description;

    ComplaintType(int code, String description){
        this.code = code;
        this.description = description;
    }

}
