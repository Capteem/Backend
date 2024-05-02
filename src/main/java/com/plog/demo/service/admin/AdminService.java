package com.plog.demo.service.admin;

import com.plog.demo.exception.CustomException;

public interface AdminService {

    void providerStatusChange(int providerId) throws CustomException;

    void providerDelete(int providerId) throws CustomException;

    void userStop(String userId) throws CustomException;

    void userDelete(String userId) throws CustomException;
}
