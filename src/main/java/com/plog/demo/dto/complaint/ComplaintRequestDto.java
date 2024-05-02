package com.plog.demo.dto.complaint;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@Builder
public class ComplaintRequestDto {
    private String complaintTitle;
    private String complaintContent;
    private String complaintDate;
    private String userId;
}
