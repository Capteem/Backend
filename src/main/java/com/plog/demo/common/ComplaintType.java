package com.plog.demo.common;

import lombok.Getter;

@Getter
public enum ComplaintType {

    RESERVATION(0, "예약"),
    PAYMENT(1, "결제"),
    SCAM(2, "사기"),
    ETC(3, "기타");

    private final int code;
    private final String description;

    ComplaintType(int code, String description){
        this.code = code;
        this.description = description;
    }

}
