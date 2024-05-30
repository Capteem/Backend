package com.plog.demo.dto.confirm;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class EmailRequestDto {
    @Email
    @NotEmpty(message = "이메일을 입력해주세요.")
    private String email;
}
