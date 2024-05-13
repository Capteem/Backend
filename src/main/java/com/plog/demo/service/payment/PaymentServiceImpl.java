package com.plog.demo.service.payment;

import com.plog.demo.common.PaymentStatus;
import com.plog.demo.common.ReservationStatus;
import com.plog.demo.dto.payment.*;
import com.plog.demo.dto.reservation.ReservationResponseDto;
import com.plog.demo.exception.CustomException;
import com.plog.demo.model.IdTable;
import com.plog.demo.model.PaymentDataTable;
import com.plog.demo.model.PaymentTable;
import com.plog.demo.model.ReservationTable;
import com.plog.demo.repository.IdTableRepository;
import com.plog.demo.repository.PaymentDataTableRepository;
import com.plog.demo.repository.PaymentTableRepository;
import com.plog.demo.repository.ReservationTableRepository;
import com.plog.demo.service.reservation.ReservationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Map;
import java.util.Optional;


@Service
@Slf4j
@RequiredArgsConstructor
@Transactional
public class PaymentServiceImpl implements PaymentService{

    private final IdTableRepository idTableRepository;
    private final PaymentTableRepository paymentTableRepository;
    private final ReservationService reservationService;
    private final ReservationTableRepository reservationTableRepository;
    private final PaymentDataTableRepository paymentDataTableRepository;

    @Value("${pay.admin_key}")
    private String adminKey;

    @Override
    public PayReadyResDto payReady(PayInfoDto payInfoDto) {

        IdTable idTable = idTableRepository.findById(payInfoDto.getReservationRequestDto().getUserId()).orElseThrow(() -> new IllegalArgumentException("해당 id가 없습니다."));
        String paymentId;
        PayReadyResDto payReadyResDto;

        HttpHeaders headers = new HttpHeaders();

        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("Authorization", "SECRET_KEY " + adminKey);

        PayRequestDto payRequestDto = new MakePayRequest().getReadyRequest(payInfoDto);

        HttpEntity<Map<String, Object>> urlRequest = new HttpEntity<>(payRequestDto.getMap(), headers);
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

        List<ReservationTable> reservationTables;
        ReservationTable currentReservation;
        try{
            reservationTables = reservationTableRepository.findAllByUserId(idTable);
            currentReservation = reservationTables.get(reservationTables.size() - 1);
        } catch (Exception e){
            log.info("[payReady] 예약 정보 조회 중 오류 발생");
            throw new RuntimeException("예약 정보 조회 중 오류가 발생했습니다.", e);
        }

        try {
            PaymentTable paymentTable = PaymentTable.builder()
                    .paymentId(paymentId)
                    .paymentAmount(payInfoDto.getPurchaseAmount())
                    .paymentTaxFreeAmount(0)
                    .reservationId(currentReservation)
                    .userId(idTable)
                    .paymentStatus(PaymentStatus.COMPLETE.getCode())
                    .build();
            paymentTableRepository.save(paymentTable);
        } catch (Exception e) {
            log.info("[payReady] 데이터 베이스 접근 오류");
            currentReservation.setUserId(null);
            reservationTableRepository.delete(currentReservation);
            throw new RuntimeException("데이터베이스 접근 중 오류가 발생했습니다.", e);
        }

        return payReadyResDto;
    }

    @Override
    public void payCancel(String id, String tid) throws CustomException {
        IdTable idTable = idTableRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("해당 id가 없습니다."));
        try {
            List<PaymentTable> paymentTables = paymentTableRepository.findALlByUserIdAndPaymentId(idTable, tid);
            PaymentTable paymentTable = paymentTables.get(paymentTables.size() - 1);

            paymentTable.setPaymentStatus(PaymentStatus.CANCEL.getCode());
            paymentTableRepository.save(paymentTable);
        } catch (Exception e){
            log.info("[payCancel] 결제 취소 중 오류 발생");
            throw new CustomException("결제 취소 중 오류가 발생했습니다.", HttpStatus.INTERNAL_SERVER_ERROR.value());
        }
    }

    @Override
    public void payFail(String tid, String id) throws CustomException {
        IdTable idTable = idTableRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("해당 id가 없습니다."));
        try {
            List<PaymentTable> paymentTables = paymentTableRepository.findALlByUserIdAndPaymentId(idTable, tid);
            PaymentTable paymentTable = paymentTables.get(paymentTables.size() - 1);

            paymentTable.setPaymentStatus(PaymentStatus.FAIL.getCode());
            paymentTableRepository.save(paymentTable);
        } catch (Exception e){
            log.info("[payFail] 결제 실패 중 오류 발생");
            throw new CustomException("결제 실패 중 오류가 발생했습니다.", HttpStatus.INTERNAL_SERVER_ERROR.value());
        }
    }

    @Override
    public PayApproveResDto getApprove(String id, String pgToken) throws CustomException {

        List<PaymentTable> paymentTables;
        PaymentTable paymentTable;
        IdTable idTable = idTableRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("[getApprove] no such id exists."));
        log.info("[getApprove] idTable : " + idTable.toString());
        try{
            paymentTables = paymentTableRepository.findAllByUserId(idTable);
            log.info("[getApprove] paymentTables : " + paymentTables.toString());
            paymentTable = paymentTables.get(paymentTables.size() - 1);
            log.info("[getApprove] paymentTable : " + paymentTable.toString());
        } catch (Exception e){
            log.info("[getApprove] paymentTable is null");
            reservationTableRepository.deleteById(reservationTableRepository.findAllByUserId(idTable).get(reservationTableRepository.findAllByUserId(idTable).size() - 1).getReservationId());
            throw new CustomException("결제 정보가 존재하지 않습니다.", HttpStatus.NOT_FOUND.value());
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
            PaymentDataTable paymentDataTable = PaymentDataTable.builder()
                    .total(payApproveResDto.getAmount().getTotal())
                    .approved_at(payApproveResDto.getApproved_at())
                    .created_at(payApproveResDto.getCreated_at())
                    .vat(payApproveResDto.getAmount().getVat())
                    .discount(payApproveResDto.getAmount().getDiscount())
                    .point(payApproveResDto.getAmount().getPoint())
                    .paymentId(paymentTable)
                    .kakaopayPurchaseCorp(payApproveResDto.getCard_info() == null ? null : payApproveResDto.getCard_info().getKakaopay_purchase_corp())
                    .build();

            paymentDataTableRepository.save(paymentDataTable);
            ReservationTable reservationTable = reservationTableRepository.findAllByUserId(idTable).get(reservationTableRepository.findAllByUserId(idTable).size() - 1);
            reservationTable.setTid(paymentTable.getPaymentId());
            reservationTableRepository.save(reservationTable);
        }catch (Exception e){
            log.error("[getApprove] failure to get approve");
            throw new CustomException("failure to get approve", HttpStatus.UNAUTHORIZED.value());
        }

        return payApproveResDto;
    }

    @Override
    public PaymentInfoDto getPaymentInfo(String id) throws CustomException{
        IdTable idTable = idTableRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("해당 id가 없습니다."));
        PaymentTable paymentTable = paymentTableRepository.findAllByUserId(idTable).get(paymentTableRepository.findAllByUserId(idTable).size() - 1);
        log.info("[getPaymentInfo] paymentTable : " + paymentTable.toString());
        List<PaymentDataTable> paymentDataTables;
        try{
            paymentDataTables = paymentDataTableRepository.findAllByPaymentId(paymentTable);
            PaymentDataTable paymentDatatable = paymentDataTables.get(paymentDataTables.size() - 1);
            log.info("[getPaymentInfo] paymentDatatable : " + paymentDatatable.toString());

            return PaymentInfoDto.builder()
                    .paymentAmount(paymentDatatable.getTotal())
                    .paymentDate(paymentDatatable.getCreated_at())
                    .paymentPoint(paymentDatatable.getPoint())
                    .paymentType(paymentDatatable.getKakaopayPurchaseCorp() == null ? "카카오페이" : "카드")
                    .paymentId(paymentTable.getPaymentId())
                    .paymentStatus(paymentTable.getPaymentStatus())
                    .build();
        } catch (Exception e){
            log.error("[getPaymentInfo] paymentDataTable is null");
            throw new CustomException("결제 정보가 존재하지 않습니다.", HttpStatus.NOT_FOUND.value());
        }
    }

    @Override
    public PayCancelDto getCancelApprove(String tid, String id) throws CustomException{
        IdTable idTable = idTableRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("해당 id가 없습니다."));
        PaymentTable paymentTable;
        try{
            paymentTable = paymentTableRepository.findByPaymentId(tid);
            if(paymentTable.getUserId() != idTable){
                throw new CustomException("결제 정보와 유저가 다릅니다.", HttpStatus.BAD_REQUEST.value());
            }
        } catch (Exception e){
            log.error("[getCancelApprove] paymentTable is null");
            throw new CustomException("결제 정보가 존재하지 않습니다.", HttpStatus.NOT_FOUND.value());
        }
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
            ReservationTable reservationTable = reservationTableRepository.findAllByUserId(idTable).get(reservationTableRepository.findAllByUserId(idTable).size() - 1);
            reservationTable.setStatus(ReservationStatus.CANCELLED.getCode());
            reservationTableRepository.save(reservationTable);
            paymentTableRepository.save(paymentTable);
        }catch (Exception e){
            log.error("[getCancelApprove] failure to get cancel approve");
            throw new CustomException("failure to get cancel approve", HttpStatus.UNAUTHORIZED.value());
        }
        return payCancelDto;
    }


}
