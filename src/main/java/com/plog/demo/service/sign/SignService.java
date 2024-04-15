package com.plog.demo.service.sign;

import com.plog.demo.dto.sign.LoginResponseDto;
import com.plog.demo.dto.user.UserDto;
import com.plog.demo.exception.CustomException;

public interface SignService {

    UserDto register(UserDto userDto) throws CustomException;

    LoginResponseDto login(String userId, String password) throws CustomException;

}
