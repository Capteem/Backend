package com.plog.demo.service.payment;

import com.plog.demo.dto.payment.PayCancelDto;
import com.plog.demo.dto.payment.PayInfoDto;
import com.plog.demo.dto.payment.PayRequestDto;
import com.plog.demo.model.PaymentTable;
import com.plog.demo.repository.PaymentTableRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.h2.util.json.JSONObject;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;

@Service
@Slf4j
public class MakePayRequest {

    public PayRequestDto getReadyRequest(PayInfoDto payInfoDto) {
        Map<String, Object> jsonMap = new HashMap<>();
        PayRequestDto payRequestDto;

        String orderId = "PlogDevTest1";

        jsonMap.put("cid", "TC0ONETIME");
        jsonMap.put("partner_order_id", orderId);
        jsonMap.put("partner_user_id", "Plog");

        jsonMap.put("item_name", payInfoDto.getPurchaseName());

        jsonMap.put("quantity", 1);

        jsonMap.put("total_amount", payInfoDto.getPurchaseAmount());
        jsonMap.put("tax_free_amount", 0);

        jsonMap.put("approval_url", "http://13.124.122.238:8084/payment/success?userId" + "=" + payInfoDto.getReservationRequestDto().getUserId());
        jsonMap.put("cancel_url", "http://13.124.122.238:8084/payment/cancel");
        jsonMap.put("fail_url", "http://13.124.122.238:8084/payment/fail");
        try{
            payRequestDto = new PayRequestDto("https://open-api.kakaopay.com/online/v1/payment/ready", jsonMap);
            return payRequestDto;
        } catch (Exception e) {
            log.info("[getReadyRequest]" + e.getMessage());
        }

        return null;
    }

    public PayRequestDto getApproveRequest(String tid, String id, String pgToken) {
        Map<String, Object> map = new HashMap<>();

        String orderId = "PlogDevTest1";

        map.put("cid", "TC0ONETIME");
        map.put("tid", tid);
        map.put("partner_order_id", orderId);
        map.put("partner_user_id", "Plog");
        map.put("pg_token", pgToken);

        return new PayRequestDto("https://open-api.kakaopay.com/online/v1/payment/approve", map);
    }

    public PayRequestDto getCancelRequest(String tid, int cancelAmount) {
        Map<String, Object> map = new HashMap<>();

        map.put("cid", "TC0ONETIME");
        map.put("tid", tid);
        map.put("cancel_amount", cancelAmount);
        map.put("cancel_tax_free_amount", 0);

        return new PayRequestDto("https://open-api.kakaopay.com/online/v1/payment/cancel", map);
    }
}
