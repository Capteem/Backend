package com.plog.demo.service.admin;

import com.plog.demo.dto.admin.AdminProviderApproveDto;
import com.plog.demo.dto.admin.AdminProviderDto;
import com.plog.demo.dto.admin.AdminRequestDto;
import com.plog.demo.dto.complaint.ComplaintReplyDto;
import com.plog.demo.exception.CustomException;
import com.plog.demo.model.IdTable;
import com.plog.demo.model.ProviderTable;

import java.util.List;

public interface AdminService {

    void changeStatus(AdminRequestDto adminRequestDto) throws CustomException;

    void approveProvider(AdminProviderApproveDto adminProviderApproveDto) throws CustomException;

    void addComplainReply(ComplaintReplyDto complaintReplyDto) throws CustomException;

    void refuseProvider(AdminProviderApproveDto adminProviderApproveDto) throws CustomException;

    List<ProviderTable> getProviderList(String adminId) throws CustomException;

    List<IdTable> getUserList(String adminId) throws CustomException;

}
