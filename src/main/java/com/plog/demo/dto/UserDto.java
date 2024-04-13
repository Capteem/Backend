package com.plog.demo.dto;

import lombok.*;

@Getter
@Setter
@RequiredArgsConstructor
@AllArgsConstructor
@Builder
public class UserDto {

    private String id;

    private String name;

    private String phoneNum;

    private String email;

    private String nickname;

}
