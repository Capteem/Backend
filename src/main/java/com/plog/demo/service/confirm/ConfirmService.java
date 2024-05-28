package com.plog.demo.service.confirm;

import com.plog.demo.dto.confirm.ConfirmCheckProviderRequestDto;
import com.plog.demo.dto.confirm.ConfirmGetCheckFilesDto;
import com.plog.demo.dto.confirm.ConfirmImageDto;
import com.plog.demo.dto.confirm.ConfirmResponseDto;
import com.plog.demo.dto.user.CheckAuthDto;
import com.plog.demo.exception.CustomException;

public interface ConfirmService {
    ConfirmResponseDto getBusinessStatus(String businessNumber) throws CustomException;

    void checkProvider(ConfirmCheckProviderRequestDto confirmCheckProviderRequestDto) throws CustomException;

    ConfirmGetCheckFilesDto getCheckfileUrls(String userId) throws CustomException;

    ConfirmImageDto getImage(String fileName) throws CustomException;

    boolean deleteFiles(String userId) throws CustomException;

    void joinEmail(String email) throws CustomException;

    void sendEmail(String setFrom, String toMail, String title, String content) throws CustomException;

    String checkAuthNumber(CheckAuthDto checkAuthDto) throws CustomException;
}
