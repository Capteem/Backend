package com.plog.demo.controller.sign;

import com.plog.demo.dto.ErrorDto;
import com.plog.demo.dto.SuccessDto;
import com.plog.demo.dto.sign.LoginRequestDto;
import com.plog.demo.dto.sign.LoginResponseDto;
import com.plog.demo.dto.sign.RenewAccessTokenResponseDto;
import com.plog.demo.dto.user.UserDto;
import com.plog.demo.exception.CustomException;
import com.plog.demo.service.sign.SignService;
import jakarta.servlet.http.HttpServletRequest;
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

    @GetMapping("/refresh")
    public ResponseEntity<RenewAccessTokenResponseDto> refresh(HttpServletRequest request) throws CustomException {

        return ResponseEntity.status(HttpStatus.OK).body(signService.renewAccessToken(request));
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
