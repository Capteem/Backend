package com.plog.demo.service.confirm;

import com.plog.demo.dto.confirm.ConfirmCheckProviderRequestDto;
import com.plog.demo.dto.confirm.ConfirmRequestDto;
import com.plog.demo.dto.confirm.ConfirmResponseDto;
import com.plog.demo.exception.CustomException;
import org.springframework.stereotype.Service;

public interface ConfirmService {
    ConfirmResponseDto getBusinessStatus(String businessNumber) throws CustomException;

    void checkProvider(ConfirmCheckProviderRequestDto confirmCheckProviderRequestDto) throws CustomException;

    boolean deleteFiles(String userId) throws CustomException;

    void mailSend(String setForm, String toMail, String title, String content) throws CustomException;

    String joinEmail(String email) throws CustomException;
}
