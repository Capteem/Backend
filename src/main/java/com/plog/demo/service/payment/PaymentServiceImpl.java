package com.plog.demo.service.payment;

import com.plog.demo.dto.payment.*;
import com.plog.demo.model.IdTable;
import com.plog.demo.model.PaymentTable;
import com.plog.demo.repository.IdTableRepository;
import com.plog.demo.repository.PaymentTableRepository;
import com.plog.demo.service.reservation.ReservationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.util.Map;


@Service
@Slf4j
@RequiredArgsConstructor
public class PaymentServiceImpl implements PaymentService{

    private final IdTableRepository idTableRepository;
    private final PaymentTableRepository paymentTableRepository;
    private final ReservationService reservationService;

    @Value("${pay.admin_key}")
    private String adminKey;

    @Override
    public PayReadyResDto payReady(PayInfoDto payInfoDto) {

        IdTable idTable = idTableRepository.findById(payInfoDto.getReservationRequestDto().getUserId()).orElseThrow(() -> new IllegalArgumentException("해당 id가 없습니다."));
        log.info("[payReady] idTable : " + idTable.toString());
        String payId = idTable.getId();
        String paymentId;
        PayReadyResDto payReadyResDto;

        HttpHeaders headers = new HttpHeaders();

        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("Authorization", "SECRET_KEY " + adminKey);

        PayRequestDto payRequestDto = new MakePayRequest().getReadyRequest(payInfoDto);

        HttpEntity<Map<String, Object>> urlRequest = new HttpEntity<>(payRequestDto.getMap(), headers);
        log.info("[payReady] urlRequest : " + urlRequest.toString());
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

        try{
            reservationService.addReservation(payInfoDto.getReservationRequestDto());
        } catch (Exception e){
            log.info("[payReady] 예약 정보 저장 중 오류 발생");
            throw new RuntimeException("예약 정보 저장 중 오류가 발생했습니다.", e);
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
    public PayApproveResDto getApprove(String id, String pgToken) throws Exception {

        PaymentTable paymentTable;
        IdTable idTable = idTableRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("[getApprove] no such id exists."));
        try{
            paymentTable = paymentTableRepository.findByUserId(idTable);
        } catch (Exception e){
            log.info("[getApprove] paymentTable is null");
            throw new RuntimeException("결제 정보가 존재하지 않습니다.", e);
        }

        HttpHeaders headers = new HttpHeaders();
        String auth = "SECRET_KEY " + adminKey;

        headers.add("Authorization", auth);
        headers.add("Content-Type", "application/json");

        PayRequestDto payRequestDto = new MakePayRequest().getApproveRequest(paymentTable.getPaymentId(), idTable.getId(), pgToken);
        log.info("[getApprove] payRequestDto : " + payRequestDto.toString());

        PayApproveResDto payApproveResDto;
        HttpEntity<Map<String, Object>> urlRequest = new HttpEntity<>(payRequestDto.getMap(), headers);

        RestTemplate restTemplate = new RestTemplate();

        try{
            payApproveResDto = restTemplate.postForObject(payRequestDto.getUrl(), urlRequest, PayApproveResDto.class);
        }catch (Exception e){
            log.info("[getApprove] failure to get approve");
            throw new RuntimeException("failure to get approve", e);
        }

        return payApproveResDto;
    }

    @Override
    public PayCancelDto getCancelApprove(String tid, String id) throws Exception{
        IdTable idTable = idTableRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("해당 id가 없습니다."));
        PaymentTable paymentTable = paymentTableRepository.findByPaymentId(tid);
        PayCancelDto payCancelDto;

        HttpHeaders headers = new HttpHeaders();
        String auth = "SECRET_KEY " + adminKey;

        headers.add("Authorization", auth);
        headers.add("Content-Type", "application/json");

        PayRequestDto payRequestDto = new MakePayRequest().getCancelRequest(tid, paymentTable.getPaymentAmount());

        HttpEntity<Map<String, Object>> urlRequest = new HttpEntity<>(payRequestDto.getMap(), headers);

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
