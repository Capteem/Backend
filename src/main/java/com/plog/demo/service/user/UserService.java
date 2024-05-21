package com.plog.demo.service.user;

import com.plog.demo.dto.user.UserChangeDto;
import com.plog.demo.dto.user.UserInfoDto;
import com.plog.demo.exception.CustomException;
import org.springframework.stereotype.Service;

public interface UserService {

    void changeUserInfo(UserInfoDto userInfoDto) throws CustomException;

    UserInfoDto getUserInfo(String id) throws CustomException;

    void changeUserPassword(UserChangeDto userChangeDto) throws CustomException;

    boolean checkUserPassword(UserChangeDto userChangeDto) throws CustomException;

    String checkUserEmail(String email) throws CustomException;

    String checkUserEmailAndId(String email, String id) throws CustomException;

}
