package com.plog.demo.dto;

import com.plog.demo.model.IdTable;

public class UserDto {
    private String id;
    private String name;
    private String password;
    private String email;
    private String phonenum;
    private String nickname;

    public UserDto(String id, String name, String password, String email, String phonenum, String nickname) {
        this.id = id;
        this.name = name;
        this.password = password;
        this.email = email;
        this.phonenum = phonenum;
        this.nickname = nickname;
    }

}
