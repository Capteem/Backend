package com.plog.demo.controller.admin;

import com.plog.demo.dto.ErrorDto;
import com.plog.demo.dto.admin.AdminProviderApproveDto;
import com.plog.demo.dto.admin.AdminRequestDto;
import com.plog.demo.dto.complaint.ComplaintReplyDto;
import com.plog.demo.exception.CustomException;
import com.plog.demo.service.admin.AdminService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@Slf4j
@CrossOrigin(origins = "*", allowedHeaders = "*")
@RequiredArgsConstructor
@Tag(name = "Admin", description = "관리자 API")
@RequestMapping("/admin")
public class AdminController {

    private final AdminService adminService;

    @PostMapping("/change")
    @Operation(summary = "유저 및 제공자 상태 변경", description = "유저 및 제공자의 상태를 변경합니다.(providerId값을 넣지 않으면 사용자 상태 변경, providerId값을 넣으면 제공자 상태 변경)")
    @ApiResponse(responseCode = "200", description = "성공")
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

    @PostMapping("/approve")
    @Operation(summary = "제공자 승인", description = "제공자를 승인합니다.")
    @ApiResponse(responseCode = "200", description = "성공")
    public ResponseEntity<Object> approveProvider(@RequestBody AdminProviderApproveDto adminProviderApproveDto) throws CustomException {
        log.info("[approveProvider] start");
        try{
            adminService.approveProvider(adminProviderApproveDto);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            log.error("[approveProvider] 제공자 승인 실패");
            throw new CustomException("제공자 승인 실패", HttpStatus.INTERNAL_SERVER_ERROR.value());
        }
    }

    @GetMapping("/provider")
    @Operation(summary = "제공자 목록 조회", description = "제공자 목록을 조회합니다.")
    public ResponseEntity<Object> getProviderList(@Parameter(description = "관리자 아이디") @RequestParam String adminId) throws CustomException {
        log.info("[getProviderList] 제공자 목록 조회");
        try{
            return ResponseEntity.ok(adminService.getProviderList(adminId));
        } catch (Exception e) {
            log.error("[getProviderList] 제공자 목록 조회 실패");
            throw new CustomException("제공자 목록 조회 실패", HttpStatus.INTERNAL_SERVER_ERROR.value());
        }
    }

    @PostMapping("/refuse")
    @Operation(summary = "제공자 거절", description = "제공자를 거절합니다.")
    @ApiResponse(responseCode = "200", description = "성공")
    public ResponseEntity<Object> refuseProvider(@RequestBody AdminProviderApproveDto adminProviderApproveDto) throws CustomException {
        log.info("[refuseProvider] start");
        try{
            adminService.refuseProvider(adminProviderApproveDto);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            log.error("[refuseProvider] 제공자 거절 실패");
            throw new CustomException("제공자 거절 실패", HttpStatus.INTERNAL_SERVER_ERROR.value());
        }
    }

    @GetMapping("/user")
    @Operation(summary = "유저 목록 조회", description = "유저 목록을 조회합니다.")
    public ResponseEntity<Object> getUserList(@Parameter(description = "관리자 아이디") @RequestParam String adminId) throws CustomException {
        log.info("[getUserList] 유저 목록 조회");
        try{
            return ResponseEntity.ok(adminService.getUserList(adminId));
        } catch (Exception e) {
            log.error("[getUserList] 유저 목록 조회 실패");
            throw new CustomException("유저 목록 조회 실패", HttpStatus.INTERNAL_SERVER_ERROR.value());
        }
    }

    @PostMapping("/reply")
    @Operation(summary = "신고 글 작성" , description = "신고 글을 작성합니다.")
public ResponseEntity<Object> addComplainReply(@RequestBody ComplaintReplyDto complaintReplyDto) throws CustomException {
        log.info("[addComplainReply] start");
        try{
            adminService.addComplainReply(complaintReplyDto);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            log.error("[addComplainReply] 신고 글 작성 실패");
            throw new CustomException("신고 글 작성 실패", HttpStatus.INTERNAL_SERVER_ERROR.value());
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
