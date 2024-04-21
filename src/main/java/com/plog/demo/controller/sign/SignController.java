package com.plog.demo.controller.sign;

import com.plog.demo.dto.sign.LoginRequestDto;
import com.plog.demo.dto.sign.LoginResponseDto;
import com.plog.demo.dto.user.UserDto;
import com.plog.demo.exception.CustomException;
import com.plog.demo.service.sign.SignService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/sign-api")
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class SignController {

    private final SignService signService;


    @PostMapping("/signup")
    public ResponseEntity<Map<String, String>> signUp(@RequestBody UserDto userDto){

        Map<String, String> responseData = new HashMap<>();

        try {
            signService.register(userDto);
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

    @PostMapping("/signin")
    public ResponseEntity<Object> signIn(@RequestBody LoginRequestDto loginRequestDto){
        Map<String, String> responseData = new HashMap<>();

        try {
            LoginResponseDto loginResponseDto = signService.login(loginRequestDto.getId(), loginRequestDto.getPassword());
            return ResponseEntity.status(HttpStatus.OK).body(loginResponseDto);
        }catch (CustomException e){
            responseData.put("message", e.getMessage());

            //유저 없음
            if(e.getResultCode() == HttpStatus.UNAUTHORIZED.value()){
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(responseData);
            }


            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(responseData);
        }catch (RuntimeException e) {
            // 데이터베이스 접근 예외 처리
            responseData.put("message", "서버에서 오류가 발생했습니다.");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(responseData);
        }

    }

    /**
     * logout TODO
     */


    /**
     * exceptionHanlder TODO
     */
}
