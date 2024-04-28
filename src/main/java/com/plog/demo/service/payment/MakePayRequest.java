package com.plog.demo.service.payment;

import com.plog.demo.dto.payment.PayCancelDto;
import com.plog.demo.dto.payment.PayInfoDto;
import com.plog.demo.dto.payment.PayRequestDto;
import com.plog.demo.model.PaymentTable;
import com.plog.demo.repository.PaymentTableRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;

@Service
@Slf4j
public class MakePayRequest {

    public PayRequestDto getReadyRequest(String id, PayInfoDto payInfoDto) {
        LinkedMultiValueMap<String, Object> map = new LinkedMultiValueMap<>();
        PayRequestDto payRequestDto;

        String orderId = "point" + id;

        map.add("cid", "TC0ONETIME");
        map.add("partner_order_id", orderId);
        map.add("partner_user_id", "Plog");

        map.add("item_name", payInfoDto.getPurchaseName());

        map.add("quantity", 1);

        map.add("total_amount", payInfoDto.getPurchaseAmount());
        map.add("tax_free_amount", 0);

        map.add("approval_url", "http://222.251.241.116:8084/payment/success");
        map.add("cancel_url", "http://222.251.241.116:8084/payment/cancel");
        map.add("fail_url", "http://222.251.241.116:8084/payment/fail");
        try{
            payRequestDto = new PayRequestDto("https://open-api.kakaopay.com/online/v1/payment/ready", map);
            return payRequestDto;
        } catch (Exception e) {
            log.info("[getReadyRequest] failure to get ready request");
        }

        return null;
    }

    public PayRequestDto getApproveRequest(String tid, String id, String pgToken) {
        LinkedMultiValueMap<String, Object> map = new LinkedMultiValueMap<>();

        String orderId = "point" + id;

        map.add("cid", "TC0ONETIME");
        map.add("tid", tid);
        map.add("partner_order_id", orderId);
        map.add("partner_user_id", "Plog");
        map.add("pg_token", pgToken);

        return new PayRequestDto("https://open-api.kakaopay.com/online/v1/payment/approve", map);
    }

    public PayRequestDto getCancelRequest(String tid, int cancelAmount) {
        LinkedMultiValueMap<String, Object> map = new LinkedMultiValueMap<>();

        map.add("cid", "TC0ONETIME");
        map.add("tid", tid);
        map.add("cancel_amount", cancelAmount);
        map.add("cancel_tax_free_amount", 0);

        return new PayRequestDto("https://open-api.kakaopay.com/online/v1/payment/cancel", map);
    }
}
