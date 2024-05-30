package com.plog.demo.controller.sign;

import com.plog.demo.dto.ErrorDto;
import com.plog.demo.dto.sign.LoginRequestDto;
import com.plog.demo.dto.sign.LoginResponseDto;
import com.plog.demo.dto.user.UserDto;
import com.plog.demo.exception.CustomException;
import com.plog.demo.service.sign.SignService;
import lombok.Getter;
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
            if(e.getResultCode() == HttpStatus.NOT_FOUND.value()){
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(responseData);
            }


            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(responseData);
        }catch (RuntimeException e) {
            // 데이터베이스 접근 예외 처리
            responseData.put("message", "서버에서 오류가 발생했습니다.");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(responseData);
        }

    }

    @GetMapping("/checkId")
    public ResponseEntity<Map<String, String>> checkId(@RequestParam String userId){
        Map<String, String> responseData = new HashMap<>();

        try {
            signService.checkId(userId);
            responseData.put("message", "사용 가능한 아이디입니다.");
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

    @GetMapping("/checkEmail")
    public ResponseEntity<Map<String, String>> checkEmail(@RequestParam String email){
        Map<String, String> responseData = new HashMap<>();

        try {
            signService.checkEmail(email);
            responseData.put("message", "사용 가능한 이메일입니다.");
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

    @GetMapping("/checkNickname")
    public ResponseEntity<Map<String, String>> checkNickname(@RequestParam String nickname){
        Map<String, String> responseData = new HashMap<>();

        try {
            signService.checkNickname(nickname);
            responseData.put("message", "사용 가능한 닉네임입니다.");
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


    /**
     * logout TODO
     */


    @ExceptionHandler(CustomException.class)
    public ResponseEntity<ErrorDto> customExceptionHandler(CustomException e){
        log.warn("customExceptionHandler 호출, {}, {}", e.getCause(), e.getMessage());

        ErrorDto errorDto = ErrorDto.builder()
                .resultCode(e.getResultCode())
                .msg(e.getMessage())
                .build();

        return ResponseEntity.status(e.getResultCode()).body(errorDto);
    }


    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<ErrorDto> runtimeExceptionHandler(Exception e){
        log.error("runtimeExceptionHandler 호출, {}, {}", e.getCause(), e.getMessage());

        ErrorDto errorDto = ErrorDto.builder()
                .resultCode(HttpStatus.INTERNAL_SERVER_ERROR.value())
                .msg(HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase())
                .build();

        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorDto);
    }
}
