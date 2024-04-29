package com.plog.demo.controller.payment;

import com.plog.demo.dto.payment.PayApproveResDto;
import com.plog.demo.dto.payment.PayInfoDto;
import com.plog.demo.service.payment.PaymentService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/payment")
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class KakaoPayController {

    private final PaymentService paymentService;

    @PostMapping("/ready")
    public ResponseEntity<Object> getRedirectUrl(@RequestBody PayInfoDto payInfoDto){
        try{
            return ResponseEntity.status(HttpStatus.OK).body(paymentService.payReady(payInfoDto));
        } catch (Exception e){
            log.info("[getRedirectUrl] failure to get redirect url");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("failure to get redirect url");
        }
    }

    @GetMapping("/success")
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
    public ResponseEntity<Object> getRefund(@RequestBody String userId, String tid){
        try{
            return ResponseEntity.status(HttpStatus.OK).body(paymentService.getCancelApprove(tid, userId));
        } catch (Exception e){
            log.info("[getRefund] error");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("환불 중 오류가 발생했습니다.");
        }
    }

    @GetMapping("/cancel")
    public ResponseEntity<Object> cancelPayment(){
        return ResponseEntity.status(HttpStatus.OK).body("결제가 취소되었습니다.");
    }

    @GetMapping("/fail")
    public ResponseEntity<Object> failPayment(){
        return ResponseEntity.status(HttpStatus.OK).body("결제에 실패했습니다.");
    }
}
