package com.plog.demo.service.sign;

import com.plog.demo.dto.sign.LoginResponseDto;
import com.plog.demo.dto.user.UserDto;
import com.plog.demo.exception.CustomException;

public interface SignService {//

    UserDto register(UserDto userDto) throws CustomException;

    LoginResponseDto login(String userId, String password) throws CustomException;

    void checkId(String userId) throws CustomException;

    void checkEmail(String email) throws CustomException;

    void checkNickname(String nickname) throws CustomException;
    /**
     * 로그아웃아면 리프레쉬 토큰 삭제 + 스케쥴러나 이런거 이용해서 7일 지나면 삭제
     */

}
