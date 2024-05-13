package com.plog.demo.controller.payment;

import com.plog.demo.dto.ErrorDto;
import com.plog.demo.dto.payment.*;
import com.plog.demo.exception.CustomException;
import com.plog.demo.service.payment.PaymentService;
import com.plog.demo.service.reservation.ReservationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/payment")
@CrossOrigin(origins = "*", allowedHeaders = "*")
@Tag(name = "Payment", description = "결제 API")
public class KakaoPayController {

    private final PaymentService paymentService;
    private final ReservationService reservationService;

    @PostMapping("/ready")
    @Operation(summary = "결제 준비", description = "결제 준비를 진행합니다.")
    @ApiResponse(responseCode = "200", description = "결제 준비 성공",content = @Content(schema = @Schema(implementation = PayReadyResDto.class)))
    public ResponseEntity<Object> getRedirectUrl(@RequestBody PayInfoDto payInfoDto){
        try{
            return ResponseEntity.status(HttpStatus.OK).body(paymentService.payReady(payInfoDto));
        } catch (Exception e){
            log.info("[getRedirectUrl] failure to get redirect url");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("failure to get redirect url");
        }
    }

    @GetMapping("/success")
    @Operation(summary = "결제 성공", description = "결제가 성공적으로 완료되었습니다.(API 콜 X)")
    public ResponseEntity<Object> getApprove(@RequestParam("userId") String id, @RequestParam("pg_token") String pgToken){
        try{
            PayApproveResDto payApproveResDto = paymentService.getApprove(id, pgToken);
            log.info(id + " 결제 승인 완료");
            return ResponseEntity.status(HttpStatus.OK).body(payApproveResDto);
        } catch (Exception e){
            log.info("[getApprove] error");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("결제 승인 중 오류가 발생했습니다.");
        }
    }

    @PostMapping("/refund")
    @Operation(summary = "환불", description = "환불을 진행합니다.")
    public ResponseEntity<Object> getRefund(@RequestBody CancelRequestDto cancelRequestDto){
        try{
            PayCancelDto approveCancel = paymentService.getCancelApprove(cancelRequestDto.getTid(), cancelRequestDto.getUserId());
            return ResponseEntity.status(HttpStatus.OK).body(approveCancel);
        } catch (Exception e){
            log.info("[getRefund] error");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("환불 중 오류가 발생했습니다.");
        }
    }

    @GetMapping("/cancel")
    @Operation(summary = "결제 취소", description = "결제를 취소합니다.(API 콜 X)")
    public ResponseEntity<Object> cancelPayment(@RequestBody CancelRequestDto cancelRequestDto) throws CustomException {
        try{
            paymentService.payCancel(cancelRequestDto.getTid(), cancelRequestDto.getUserId());
            return ResponseEntity.status(HttpStatus.OK).body("결제가 취소되었습니다.");
        } catch (CustomException e){
            log.info("[cancelPayment] error");
            throw new CustomException(e.getMessage(), e.getResultCode());
        }
    }

    @GetMapping("/fail")
    @Operation(summary = "결제 실패", description = "결제에 실패했습니다.(API 콜 X)")
    public ResponseEntity<Object> failPayment(@RequestBody CancelRequestDto cancelRequestDto) throws CustomException {
        try{
            paymentService.payFail(cancelRequestDto.getTid(), cancelRequestDto.getUserId());
            return ResponseEntity.status(HttpStatus.OK).body("결제가 실패되었습니다.");
        } catch (CustomException e){
            log.info("[failPayment] error");
            throw new CustomException(e.getMessage(), e.getResultCode());
        }
    }

    @GetMapping("/success/info")
    @Operation(summary = "결제 정보", description = "카카오페이 팝업창이 꺼지면 부르는 API, 결제 정보를 가져옵니다.")
    public ResponseEntity<Object> getPaymentInfo(@RequestParam("userId") String id) throws CustomException {
        try{
            PaymentInfoDto paymentInfo = paymentService.getPaymentInfo(id);
            return ResponseEntity.status(HttpStatus.OK).body(paymentInfo);
        } catch (CustomException e){
            log.info("[getPaymentInfo] error");
            throw new CustomException(e.getMessage(), e.getResultCode());
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
