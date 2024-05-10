package com.plog.demo.model;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Entity
@NoArgsConstructor
@Builder
public class IdTable {
    @Id
    private String id;
    @Column
    private String name;
    @Column
    private String password;
    @Column
    private String email;
    @Column
    private String phonenum;
    @Column
    private String nickname;
    @Column
    private String role;
    @Column
    private int status;

    public IdTable(String id, String name, String password, String email, String phonenum, String nickname, String role, int status) {
        this.id = id;
        this.name = name;
        this.password = password;
        this.email = email;
        this.phonenum = phonenum;
        this.nickname = nickname;
        this.role = role;
        this.status = status;
    }
}