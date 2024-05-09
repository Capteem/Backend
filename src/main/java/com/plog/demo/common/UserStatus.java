package com.plog.demo.common;

public enum UserStatus {
    ACTIVE(0, "활성화"),
    STOP(1, "정지"),
    BANNED(2, "차단");

    private final int code;
    private final String description;

    UserStatus(int code, String description){
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
