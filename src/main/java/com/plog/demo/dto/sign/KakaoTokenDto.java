package com.plog.demo.dto.sign;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class KakaoTokenDto {
    private String access_token;
    private String token_type;
    private String id_token;
    private String expires_in;
    private String refresh_token;
    private String refresh_token_expires_in;
    private String scope;

}
