package com.plog.demo.Dto;

import com.plog.demo.model.IdTable;

public class UserDto {
    private String id;
    private String name;
    private String password;
    private String email;
    private String phonenum;
    private String nickname;
    private String role;

    public UserDto(String id, String name, String password, String email, String phonenum, String nickname, String role) {
        this.id = id;
        this.name = name;
        this.password = password;
        this.email = email;
        this.phonenum = phonenum;
        this.nickname = nickname;
        this.role = role;
    }

    public IdTable toEntity() {
        return new IdTable(id, name, password, email, phonenum, nickname, role);
    }
}
