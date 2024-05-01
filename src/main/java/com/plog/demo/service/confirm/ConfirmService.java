package com.plog.demo.service.confirm;

import com.plog.demo.dto.confirm.ConfirmRequestDto;
import com.plog.demo.dto.confirm.ConfirmResponseDto;
import com.plog.demo.exception.CustomException;
import org.springframework.stereotype.Service;

public interface ConfirmService {
    ConfirmResponseDto getBusinessStatus(String businessNumber) throws CustomException;

}
