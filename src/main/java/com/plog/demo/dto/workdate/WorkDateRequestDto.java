package com.plog.demo.dto.workdate;

import lombok.*;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class WorkDateRequestDto {

    private String date;
    private String time;
    private String day;
}
