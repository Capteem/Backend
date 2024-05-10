package com.plog.demo.dto.admin;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@Builder
public class AdminUserResponseDto {

    private String userId;
    private String userNickName;
    private String userPhoneNum;
    private String userEmail;
    private int userStatus;
    private String userRole;
}
