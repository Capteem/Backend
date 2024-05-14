package com.plog.demo.dto.workdate;


import lombok.*;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class WorkdateDto {

    private String userId;
    private List<DateListDto> dateList;
}
