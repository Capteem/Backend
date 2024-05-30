package com.plog.demo.dto.sign;

import lombok.*;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class LoginResponseDto {

    private String accessToken;

    private String refreshToken;

    private String role;

    private String userNickname;
}
