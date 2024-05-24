package com.plog.demo.controller.user;

import com.plog.demo.dto.user.UserChangeDto;
import com.plog.demo.dto.user.UserCheckDto;
import com.plog.demo.dto.user.UserInfoDto;
import com.plog.demo.exception.CustomException;
import com.plog.demo.service.user.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/user")
@Tag(name = "User", description = "유저 API")
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class UserController {

    private final UserService userService;

    @PostMapping("/changeinfo")
    @Operation(summary = "유저 정보 변경", description = "유저 정보를 변경합니다.")
    @ApiResponse(responseCode = "200", description = "성공")
    public ResponseEntity<Object> changeUserInfo(@RequestBody UserInfoDto userInfoDto) throws CustomException {
        log.info("[changeUserInfo] change user info");
        try{
            userService.changeUserInfo(userInfoDto);
            return ResponseEntity.ok().build();
        } catch (CustomException e) {
            log.error("[changeUserInfo] change user info failed");
            throw new CustomException(e.getMessage(), e.getResultCode());
        }
    }

    @GetMapping("/getinfo")
    @Operation(summary = "유저 정보 조회", description = "유저 정보를 조회합니다.")
    @ApiResponse(responseCode = "200", description = "성공", content = @Content(schema = @Schema(implementation = UserInfoDto.class)))
    public ResponseEntity<Object> getUserInfo(@RequestParam String userId) throws CustomException {
        log.info("[getUserInfo] get user info");
        try{
            return ResponseEntity.ok().body(userService.getUserInfo(userId));
        } catch (CustomException e) {
            log.error("[getUserInfo] get user info failed");
            throw new CustomException(e.getMessage(), e.getResultCode());
        }
    }

    @PostMapping("/getconfirm")
    @Operation(summary = "유저 비밀번호 확인", description = "유저 비밀번호를 확인합니다.")
    @ApiResponse(responseCode = "200", description = "성공")
    public ResponseEntity<Object> getUserConfirm(@RequestBody UserChangeDto userChangeDto) throws CustomException {
        log.info("[getUserConfirm] get user confirm");
        try{
            boolean result = userService.checkUserPassword(userChangeDto);
            if(!result){
                return ResponseEntity.status(HttpStatus.BAD_REQUEST.value()).build();
            }
            return ResponseEntity.ok().build();
        } catch (CustomException e) {
            log.error("[getUserConfirm] get user confirm failed");
            throw new CustomException(e.getMessage(), e.getResultCode());
        }
    }

    @PostMapping("/changePwd")
    @Operation(summary = "유저 비밀번호 변경", description = "유저 비밀번호를 변경합니다.")
    @ApiResponse(responseCode = "200", description = "성공")
    public ResponseEntity<Object> changeUserPwd(@RequestBody UserChangeDto userChangeDto) throws CustomException {
        log.info("[changeUserPwd] change user password");
        try{
            userService.changeUserPassword(userChangeDto);
            return ResponseEntity.ok().build();
        } catch (CustomException e) {
            log.error("[changeUserPwd] change user password failed");
            throw new CustomException(e.getMessage(), e.getResultCode());
        }
    }

    @PostMapping("/checkemail")
    @Operation(summary = "아이디찾기 or 비밀번호 찾기", description = "유저 이메일을 확인합니다.(UserCheckDto에 id가 null이면 아이디 찾기, id가 있으면 비밀번호 찾기)")
    @ApiResponse(responseCode = "200", description = "성공")
    public ResponseEntity<Object> checkUserEmail(@RequestBody UserCheckDto userCheckDto) throws CustomException {
        log.info("[checkUserEmail] check user email");
        try{
            if(userCheckDto.getId() == null){
                String result = userService.checkUserEmail(userCheckDto.getEmail());
                return ResponseEntity.status(HttpStatus.OK).body(result);
            }
            String result = userService.checkUserEmailAndId(userCheckDto.getEmail(), userCheckDto.getId());
            return ResponseEntity.status(HttpStatus.OK).body(result);
        } catch (CustomException e) {
            log.error("[checkUserEmail] check user email failed");
            throw new CustomException(e.getMessage(), e.getResultCode());
        }
    }
}
