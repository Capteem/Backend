package com.plog.demo.service.payment;

import com.plog.demo.dto.payment.*;
import com.plog.demo.model.IdTable;
import com.plog.demo.model.PaymentTable;
import com.plog.demo.repository.IdTableRepository;
import com.plog.demo.repository.PaymentTableRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;


@Service
@Slf4j
@RequiredArgsConstructor
public class PaymentServiceImpl implements PaymentService{

    private final IdTableRepository idTableRepository;
    private final PaymentTableRepository paymentTableRepository;

    @Value("${pay.admin-key}")
    private String adminKey;

    @Override
    public PayReadyResDto payReady(PayInfoDto payInfoDto) {

        IdTable idTable = idTableRepository.findById(payInfoDto.getUserId()).orElseThrow(() -> new IllegalArgumentException("해당 id가 없습니다."));
        String payId = idTable.getId();
        String paymentId;
        PayReadyResDto payReadyResDto;

        HttpHeaders headers = new HttpHeaders();

        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("Authorization", "SECRET_KEY " + adminKey);

        PayRequestDto payRequestDto = new MakePayRequest().getReadyRequest(payId, payInfoDto);

        HttpEntity<MultiValueMap<String, Object>> urlRequest = new HttpEntity<>(payRequestDto.getMap(), headers);

        RestTemplate restTemplate = new RestTemplate();
        try{
            payReadyResDto = restTemplate.postForObject(payRequestDto.getUrl(), urlRequest, PayReadyResDto.class);
        } catch (Exception e){
            log.info("[payReady] failure to get redirect url");
            throw new RuntimeException("failure to get redirect url", e);
        }

        try{
            assert payReadyResDto != null;
            paymentId = payReadyResDto.getTid();
        } catch (Exception e){
            log.info("[payReady] 결제 준비 중 오류 발생");
            throw new RuntimeException("결제 준비 중 오류가 발생했습니다.", e);
        }

        try {
            PaymentTable paymentTable = PaymentTable.builder()
                    .paymentId(paymentId)
                    .paymentAmount(payInfoDto.getPurchaseAmount())
                    .paymentTaxFreeAmount(0)
                    .userId(idTable)
                    .build();
            paymentTableRepository.save(paymentTable);
        } catch (Exception e) {
            log.info("[payReady] 데이터 베이스 접근 오류");
            throw new RuntimeException("데이터베이스 접근 중 오류가 발생했습니다.", e);
        }

        return payReadyResDto;
    }

    @Override
    public PayApproveResDto getApprove(String pgToken, String id) throws Exception {

        IdTable idTable = idTableRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("해당 id가 없습니다."));
        String tid = paymentTableRepository.findByUserId(idTable).getPaymentId();

        HttpHeaders headers = new HttpHeaders();
        String auth = "SECRET_KEY " + adminKey;

        headers.add("Authorization", auth);
        headers.add("Content-Type", "application/json");

        PayRequestDto payRequestDto = new MakePayRequest().getApproveRequest(tid, id, pgToken);

        HttpEntity<MultiValueMap<String, Object>> urlRequest = new HttpEntity<>(payRequestDto.getMap(), headers);

        RestTemplate restTemplate = new RestTemplate();

        return restTemplate.postForObject(payRequestDto.getUrl(), urlRequest, PayApproveResDto.class);
    }

    public PayCancelDto getCancelApprove(String tid, String id) throws Exception{
        IdTable idTable = idTableRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("해당 id가 없습니다."));
        PaymentTable paymentTable = paymentTableRepository.findByPaymentId(tid);
        PayCancelDto payCancelDto;

        HttpHeaders headers = new HttpHeaders();
        String auth = "SECRET_KEY " + adminKey;

        headers.add("Authorization", auth);
        headers.add("Content-Type", "application/json");

        PayRequestDto payRequestDto = new MakePayRequest().getCancelRequest(tid, paymentTable.getPaymentAmount());

        HttpEntity<MultiValueMap<String, Object>> urlRequest = new HttpEntity<>(payRequestDto.getMap(), headers);

        RestTemplate restTemplate = new RestTemplate();
        try{
            payCancelDto = restTemplate.postForObject(payRequestDto.getUrl(), urlRequest, PayCancelDto.class);
        }catch (Exception e){
            log.info("[getCancelApprove] failure to get cancel approve");
            throw new RuntimeException("failure to get cancel approve", e);
        }
        return restTemplate.postForObject(payRequestDto.getUrl(), urlRequest, PayCancelDto.class);
    }
}
