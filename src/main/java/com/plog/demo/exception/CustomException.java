package com.plog.demo.exception;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class CustomException extends Exception{

    /**
     * TODO 나중에 더 수정 해야함
     */
    private int resultCode;
    public CustomException(String message) {
        super(message);
    }

    public CustomException(String message, int resultCode) {
        super(message);
        this.resultCode = resultCode;
    }
}
