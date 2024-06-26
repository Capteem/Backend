package com.plog.demo.dto.confirm;


import lombok.*;

import java.util.List;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ConfirmGetCheckFilesDto {

    private String uuid;

    private List<String> fileNameList;
}
