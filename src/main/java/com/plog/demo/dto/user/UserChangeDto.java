package com.plog.demo.dto.user;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserChangeDto {

    private String id;
    private String password;
}
