package com.plog.demo.dto.complaint;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@Builder
public class ComplaintReplyDto {

    private int complaintId;
    private String complaintReplyContent;
    private String complaintReplyDate;

}
