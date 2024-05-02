package com.plog.demo.common;

public enum ComplaintStatus {

        WAITING(0, "처리 대기"),
        COMPLETED(2, "처리 완료");


        private final int code;
        private final String description;

        ComplaintStatus(int code, String description){
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
