package com.plog.demo.controller.admin;

import com.plog.demo.dto.ErrorDto;
import com.plog.demo.dto.admin.AdminProviderDto;
import com.plog.demo.dto.admin.AdminRequestDto;
import com.plog.demo.exception.CustomException;
import com.plog.demo.service.admin.AdminService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@RestController
@Slf4j
@CrossOrigin(origins = "*", allowedHeaders = "*")
@RequiredArgsConstructor
@Tag(name = "Admin", description = "관리자 API")
@RequestMapping("/admin")
public class AdminController {

    private final AdminService adminService;

    @PostMapping("/approve")
    @Operation(summary = "제공자 상태 변경", description = "제공자의 상태를 변경합니다.(providerId값을 넣지 않으면 사용자 상태 변경, providerId값을 넣으면 제공자 상태 변경)")
    @ApiResponse(responseCode = "200", description = "성공")
    public ResponseEntity<Object> approveProvider(@RequestBody AdminProviderDto adminProviderDto) throws CustomException {
        log.info("[approveProvider] 제공자 상태 변경");

        try{
            adminService.approveProvider(adminProviderDto);
            return ResponseEntity.status(HttpStatus.OK).build();
        } catch (Exception e) {
            log.error("[approveProvider] 제공자 상태 변경 실패");
            throw new CustomException("제공자 상태 변경 실패", 500);
        }
    }

    @PostMapping("/change")
    public ResponseEntity<Object> changeStatus(@RequestBody AdminRequestDto adminRequestDto) throws CustomException {
        log.info("[changeStatus] 유저 및 제공자 상태 변경");

        try{
            adminService.changeStatus(adminRequestDto);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            log.error("[changeStatus] 유저 및 제공자 상태 변경 실패");
            throw new CustomException("유저 및 제공자 상태 변경 실패", 500);
        }
    }

    @ExceptionHandler(CustomException.class)
    public ResponseEntity<ErrorDto> handleCustomException(CustomException e) {
        log.error("CustomException 호출 {} {}", e.getResultCode(), e.getMessage());

        ErrorDto errorDto = ErrorDto.builder()
                .resultCode(e.getResultCode())
                .msg(e.getMessage())
                .build();

        return ResponseEntity.status(e.getResultCode()).body(errorDto);
    }

}
