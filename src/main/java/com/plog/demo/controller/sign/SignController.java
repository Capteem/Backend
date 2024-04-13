package com.plog.demo.controller.sign;

import com.plog.demo.dto.UserDto;
import com.plog.demo.exception.CustomException;
import com.plog.demo.service.sign.SignService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/sign-api")
public class SignController {

    private final SignService signService;


    @PostMapping("/signup")
    public ResponseEntity<Map<String, String>> signUp(@RequestBody UserDto userDto){

        Map<String, String> responseData = new HashMap<>();

        try {
            signService.addUser(userDto);
            responseData.put("message", "회원가입 성공");
            return ResponseEntity.status(HttpStatus.OK).body(responseData);
        }catch (CustomException e){
            responseData.put("message", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(responseData);
        }catch (RuntimeException e) {
            // 데이터베이스 접근 예외 처리
            responseData.put("message", "서버에서 오류가 발생했습니다.");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(responseData);
        }
    }
}
