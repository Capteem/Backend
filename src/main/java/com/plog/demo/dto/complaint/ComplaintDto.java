package com.plog.demo.dto.complaint;

import com.plog.demo.model.IdTable;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@Builder
public class ComplaintDto {

    private String complaintTitle;
    private String complaintContent;
    private String complaintDate;
    private String complaintStatus;
    private IdTable userId;
}
