package com.plog.demo.dto.user;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CheckAuthDto {
    private String userId;
    private String email;
    private int auth;
}
