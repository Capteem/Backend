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

    @GetMapping("/success/{id}")
    public ResponseEntity<Object> getApprove(@PathVariable("id") String id, @RequestParam("pg_token") String pgToken){
        try{
            PayApproveResDto payApproveResDto = paymentService.getApprove(pgToken, id);
            return ResponseEntity.status(HttpStatus.OK).body(payApproveResDto);
        } catch (Exception e){
            log.info("[getApprove] 결제 승인 중 오류 발생");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("결제 승인 중 오류가 발생했습니다.");
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
