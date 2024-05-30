package com.plog.demo.dto.confirm;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class EmailAuthCheckDto {

    private String email;
    private int authCode;
}
