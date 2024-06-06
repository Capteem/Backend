package com.plog.demo.service.complaint;

import com.plog.demo.dto.complaint.ComplaintPhotoDto;
import com.plog.demo.dto.complaint.ComplaintReplyDto;
import com.plog.demo.dto.complaint.ComplaintResponseDto;
import com.plog.demo.dto.complaint.ComplaintRequestDto;
import com.plog.demo.dto.confirm.ConfirmCheckProviderRequestDto;
import com.plog.demo.exception.CustomException;

import java.util.List;

public interface ComplaintService {

    void addComplain(ComplaintRequestDto complaintRequestDto) throws CustomException;

    List<ComplaintResponseDto> getComplain(String userId) throws CustomException;

    void addPhotoToComplaint(ComplaintPhotoDto complaintPhotoDto) throws CustomException;
}
