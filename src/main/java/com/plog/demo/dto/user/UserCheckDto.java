package com.plog.demo.dto.user;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserCheckDto {

    private String id;
    private String email;

}
