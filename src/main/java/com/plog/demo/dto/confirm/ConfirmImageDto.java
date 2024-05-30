package com.plog.demo.dto.confirm;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ConfirmImageDto {

    private String fileExtension;

    private String imgFullPath;
}
