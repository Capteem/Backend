package com.plog.demo.dto.complaint;

import com.plog.demo.model.ComplaintAnswerTable;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@AllArgsConstructor
@Setter
@Builder
public class ComplaintResponseDto {
    private int complaintId;
    private String complaintTitle;
    private String complaintContent;
    private int complaintStatus;
    private String complaintDate;
    private String userId;
    private int complaintType;
    private ComplaintReplyResponseDto complaintAnswerTable;

}
