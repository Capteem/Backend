package com.plog.demo.service.payment;

import com.plog.demo.common.PaymentStatus;
import com.plog.demo.common.ReservationStatus;
import com.plog.demo.dto.payment.*;
import com.plog.demo.dto.workdate.WorkDateRequestDto;
import com.plog.demo.dto.workdate.WorkdateDto;
import com.plog.demo.exception.CustomException;
import com.plog.demo.model.*;
import com.plog.demo.repository.*;
import com.plog.demo.service.Provider.ProviderService;
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
import org.springframework.web.client.RestTemplate;

import java.time.LocalDateTime;
import java.util.ArrayList;
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
    private final WorkdateTableRepository workdateTableRepository;
    private final ProviderTableRepository providerTableRepository;

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
            log.info("[payReady] "+ e.getMessage());
            throw new RuntimeException("failure to get redirect url", e);
        }

        try{
            assert payReadyResDto != null;
            paymentId = payReadyResDto.getTid();
        } catch (Exception e){
            log.info("[payReady] error while getting paymentId");
            throw new RuntimeException("결제 준비 중 오류가 발생했습니다.", e);
        }

        try{
            reservationService.addReservation(payInfoDto.getReservationRequestDto());
        } catch (Exception e){
            log.info("[payReady] error while saving reservation info");
            throw new RuntimeException("예약 정보 저장 중 오류가 발생했습니다.", e);
        }

        List<ReservationTable> reservationTables;
        ReservationTable currentReservation;
        try{
            reservationTables = reservationTableRepository.findAllByUserId(idTable);
            currentReservation = reservationTables.get(reservationTables.size() - 1);
            log.info("[payReady] currentReservation : " + currentReservation.toString());
        } catch (Exception e){
            log.info("[payReady] errow while getting reservation info");
            throw new RuntimeException("예약 정보 조회 중 오류가 발생했습니다.", e);
        }

        try {
            PaymentTable paymentTable = PaymentTable.builder()
                    .paymentId(paymentId)
                    .paymentAmount(payInfoDto.getPurchaseAmount())
                    .paymentTaxFreeAmount(0)
                    .userId(idTable)
                    .paymentStatus(PaymentStatus.COMPLETE.getCode())
                    .build();
            paymentTableRepository.save(paymentTable);
        } catch (Exception e) {
            log.info("[payReady] error while saving payment info");
            currentReservation.setUserId(null);
            reservationTableRepository.delete(currentReservation);
            throw new RuntimeException("데이터베이스 접근 중 오류가 발생했습니다.", e);
        }

        return payReadyResDto;
    }

    @Override
    public void payCancel(String id, String tid) throws CustomException {
        try {
            PaymentTable paymentTable = paymentTableRepository.findByPaymentId(tid);
            ReservationTable reservationTable = reservationTableRepository.findByTid(paymentTable);
            reservationTableRepository.deleteById(reservationTable.getReservationId());
            paymentTableRepository.deleteById(paymentTable.getPaymentId());
        } catch (Exception e){
            log.info("[payCancel] 결제 취소 중 오류 발생");
            throw new CustomException("결제 취소 중 오류가 발생했습니다.", HttpStatus.INTERNAL_SERVER_ERROR.value());
        }
    }

    @Override
    public void payFail(String tid, String id) throws CustomException {
        try {
            PaymentTable paymentTable = paymentTableRepository.findByPaymentId(tid);
            ReservationTable reservationTable = reservationTableRepository.findByTid(paymentTable);
            reservationTableRepository.deleteById(reservationTable.getReservationId());
            paymentTableRepository.deleteById(paymentTable.getPaymentId());
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
            paymentTable = paymentTables.get(paymentTables.size() - 1);
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

        PayApproveResDto payApproveResDto;
        HttpEntity<Map<String, Object>> urlRequest = new HttpEntity<>(payRequestDto.getMap(), headers);

        RestTemplate restTemplate = new RestTemplate();

        try{
            payApproveResDto = restTemplate.postForObject(payRequestDto.getUrl(), urlRequest, PayApproveResDto.class);
            assert payApproveResDto != null;
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
            List<ReservationTable> reservationTables = reservationTableRepository.findAllByUserId(idTable);
            if(reservationTables.isEmpty()){
                throw new CustomException("예약 정보가 존재하지 않습니다.", HttpStatus.NOT_FOUND.value());
            }
            ReservationTable reservationTable = reservationTables.get(reservationTables.size() - 1);
            reservationTable.setTid(paymentTable);
            reservationTableRepository.save(reservationTable);
            List<ProviderTable> providerTables = getProviderList(reservationTable);
            for(ProviderTable providerTable : providerTables){
                try{
                    workdateTableRepository.deleteByProviderIdAndWorkTime(providerTable, reservationTable.getReservation_start_date(), reservationTable.getReservation_end_date());
                } catch (Exception e){
                    log.error(e.getMessage());
                    throw new CustomException("예약 정보 삭제 중 오류가 발생했습니다.", HttpStatus.INTERNAL_SERVER_ERROR.value());
                }
            }
        }catch (Exception e){
            log.error(e.getMessage());
            throw new CustomException("failure to get approve", HttpStatus.UNAUTHORIZED.value());
        }

        return payApproveResDto;
    }

    @Override
    public PaymentInfoDto getPaymentInfo(String id) throws CustomException{
        log.info("[getPaymentInfo] start");
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
            ReservationTable reservationTable = reservationTableRepository.findByTid(paymentTable);
            reservationTable.setStatus(ReservationStatus.CANCELLED.getCode());
            reservationTableRepository.save(reservationTable);
            paymentTableRepository.save(paymentTable);
            List<WorkdateTable> workdateTables = saveWorkDateList(reservationTable);
            for(WorkdateTable workdateTable : workdateTables){
                log.info("[getCancelApprove] workdateTable : " + workdateTable.getWorkDate());
            }
            workdateTableRepository.saveAll(workdateTables);
        }catch (Exception e){
            log.error("[getCancelApprove] failure to get cancel approve");
            throw new CustomException("failure to get cancel approve", HttpStatus.UNAUTHORIZED.value());
        }
        return payCancelDto;
    }

    private List<ProviderTable> getProviderList(ReservationTable reservationTable){
        log.info("[getProviderList] start");
        List<ProviderTable> providerTables = new ArrayList<>();
        int[] providerIds = new int[]{reservationTable.getReservation_camera(), reservationTable.getReservation_hair(), reservationTable.getReservation_studio()};
        for(int providerId : providerIds){
            if(providerId != 0){
                providerTables.add(providerTableRepository.findByProviderId(providerId));
            }
        }
        return providerTables;
    }

    private List<WorkdateTable> saveWorkDateList(ReservationTable reservationTable){
        List<WorkdateTable> workdateTables = new ArrayList<>();
        List<ProviderTable> providerTables = getProviderList(reservationTable);
        LocalDateTime startDate = reservationTable.getReservation_start_date();
        LocalDateTime endDate = reservationTable.getReservation_end_date();
        for(ProviderTable providerTable : providerTables){
            LocalDateTime currentDate = startDate;
            while (currentDate.isBefore(endDate)){
                WorkdateTable workdateTable = WorkdateTable.builder()
                        .providerId(providerTable)
                        .workDate(currentDate)
                        .workDay(currentDate.getDayOfWeek().toString())
                        .build();
                workdateTables.add(workdateTable);
                currentDate = currentDate.plusHours(1);
            }
        }
        return workdateTables;
    }
}
