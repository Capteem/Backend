package com.plog.demo.service.sign;

import com.plog.demo.dto.UserDto;
import com.plog.demo.exception.CustomException;

public interface SignService {

    UserDto addUser(UserDto userDto) throws CustomException;

    UserDto login(UserDto userDto);

}
