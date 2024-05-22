package com.plog.demo.common;

import lombok.Getter;

@Getter
public enum CommonStatus {
    ACTIVE(1, "활성화"),
    DELETE(2, "삭제");

    private final int code;
    private final String description;

    CommonStatus(int code, String description){
        this.code = code;
        this.description = description;
    }
}
