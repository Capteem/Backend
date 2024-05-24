package com.plog.demo.dto.user;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserInfoDto {

    private String id;
    private String nickname;
    private String email;
    private String phoneNum;
}
