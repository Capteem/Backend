package com.plog.demo.service.complaint;

import com.plog.demo.dto.complaint.ComplaintDto;
import com.plog.demo.dto.complaint.ComplaintRequestDto;
import com.plog.demo.exception.CustomException;
import com.plog.demo.model.ComplaintTable;

import java.util.List;

public interface ComplaintService {

    ComplaintRequestDto addComplain(ComplaintRequestDto complaintRequestDto) throws CustomException;

    List<ComplaintTable> getComplain(String userId) throws CustomException;
}
