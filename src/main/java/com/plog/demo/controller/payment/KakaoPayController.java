package com.plog.demo.controller.payment;

import com.plog.demo.dto.payment.CancelRequestDto;
import com.plog.demo.dto.payment.PayApproveResDto;
import com.plog.demo.dto.payment.PayInfoDto;
import com.plog.demo.service.payment.PaymentService;
import io.swagger.v3.oas.annotations.Operation;
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

    @PostMapping("/ready")
    @Operation(summary = "결제 준비", description = "결제 준비를 진행합니다.")
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
            return ResponseEntity.status(HttpStatus.OK).body(paymentService.getCancelApprove(cancelRequestDto.getTid(), cancelRequestDto.getUserId()));
        } catch (Exception e){
            log.info("[getRefund] error");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("환불 중 오류가 발생했습니다.");
        }
    }

    @GetMapping("/cancel")
    @Operation(summary = "결제 취소", description = "결제를 취소합니다.(API 콜 X)")
    public ResponseEntity<Object> cancelPayment(){
        return ResponseEntity.status(HttpStatus.OK).body("결제가 취소되었습니다.");
    }

    @GetMapping("/fail")
    @Operation(summary = "결제 실패", description = "결제에 실패했습니다.(API 콜 X)")
    public ResponseEntity<Object> failPayment(){
        return ResponseEntity.status(HttpStatus.OK).body("결제에 실패했습니다.");
    }
}
