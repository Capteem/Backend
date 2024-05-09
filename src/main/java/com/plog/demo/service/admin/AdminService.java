package com.plog.demo.service.admin;

import com.plog.demo.dto.admin.AdminProviderDto;
import com.plog.demo.dto.admin.AdminRequestDto;
import com.plog.demo.exception.CustomException;

public interface AdminService {

    void changeStatus(AdminRequestDto adminRequestDto) throws CustomException;

    void approveProvider(AdminProviderDto adminProviderDto) throws CustomException;
}
