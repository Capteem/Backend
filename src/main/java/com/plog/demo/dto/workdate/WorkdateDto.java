package com.plog.demo.dto.workdate;


import lombok.*;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class WorkdateDto {

    private int providerId;
    private List<WorkDateRequestDto> dateList;
}
