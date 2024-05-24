package com.plog.demo.dto.sign;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class RenewAccessTokenResponseDto {

    private String accessToken;
}
