package com.plog.demo.common;

public enum ReservationStatus {

    WAITING(0, "예약 대기"),
    CONFIRMED(1, "예약 확정"),
    COMPLETED(2, "예약 완료"),
    CANCELLED(3, "예약 취소");

    private final int code;
    private final String description;

    ReservationStatus(int code, String description){
        this.code = code;
        this.description = description;
    }

    public int getCode(){
        return code;
    }

    public String getDescription(){
        return description;
    }

}
