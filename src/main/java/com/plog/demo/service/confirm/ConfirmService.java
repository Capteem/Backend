package com.plog.demo.service.confirm;

import com.plog.demo.dto.confirm.ConfirmRequestDto;
import com.plog.demo.dto.confirm.ConfirmResponseDto;
import com.plog.demo.exception.CustomException;

public interface ConfirmService {
    ConfirmResponseDto getBusinessStatus(ConfirmRequestDto confirmRequestDto) throws CustomException;

}
