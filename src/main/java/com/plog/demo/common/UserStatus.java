package com.plog.demo.common;

import lombok.Getter;

@Getter
public enum UserStatus {
    ACTIVE(1, "활성화"),
    STOP(2, "정지"),
    BANNED(3, "차단"),
    DELETED(4, "삭제"),
    INFO_LACK(5, "정보 미입력");

    private final int code;
    private final String description;

    UserStatus(int code, String description){
        this.code = code;
        this.description = description;
    }

}
