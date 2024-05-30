package com.plog.demo.dto.sign;

import lombok.*;
//c
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class LoginRequestDto {

    private String id;

    private String password;
}
