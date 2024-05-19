package com.plog.demo.service.Provider;


import com.plog.demo.common.UserStatus;

import com.plog.demo.dto.Provider.ProviderDto;
import com.plog.demo.dto.Provider.ProviderListDto;
import com.plog.demo.dto.Provider.ProviderReservationDto;
import com.plog.demo.dto.Provider.ProviderResponseDto;

import com.plog.demo.dto.reservation.ReservationProviderResponseDto;
import com.plog.demo.dto.workdate.WorkDateRequestDto;
import com.plog.demo.dto.workdate.WorkdateDto;
import com.plog.demo.dto.workdate.DateListDto;
import com.plog.demo.exception.CustomException;
import com.plog.demo.common.ReservationStatus;
import com.plog.demo.model.*;

import com.plog.demo.repository.*;
import com.plog.demo.service.payment.PaymentService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class ProviderServiceImpl implements ProviderService{

    private final ProviderTableRepository providerTableRepository;
    private final IdTableRepository idTableRepository;
    private final WorkdateTableRepository workdateTableRepository;
    private final ReservationTableRepository reservationTableRepository;
    private final PaymentService paymentService;

    @Override
    public ProviderDto addProvider(ProviderDto providerDto) throws CustomException{

        IdTable idTable = idTableRepository.findById(providerDto.getUserId()).orElseThrow(() -> new CustomException("존재하지 않는 사용자입니다."));

        ProviderTable providerTable = ProviderTable.builder()
                .userId(idTable)
                .providerName(providerDto.getProviderName())
                .providerType(providerDto.getProviderType())
                .providerArea(providerDto.getProviderArea())
                .providerSubArea(providerDto.getProviderSubArea())
                .providerDetailArea(providerDto.getProviderDetail())
                .providerPhoneNum(providerDto.getProviderPhoneNum())
                .providerPrice(-1)
                .providerStatus(UserStatus.STOP.getCode())
                .build();

        try{
            log.info("[addProvider] save제공자 저장 로직 시작");
            providerTableRepository.save(providerTable);
        }catch (Exception e){
            log.error("[addProvider] db데이터 베이스 접근 오류");
            throw new RuntimeException("데이터베이스 접근 중 오류가 발생했습니다.", e);
        }

        return providerDto;
    }

    @Override
    public List<ProviderResponseDto> getSelectedProvider(String userId) throws CustomException{

        IdTable idTable = idTableRepository.findById(userId).orElseThrow(() -> new CustomException("존재하지 않는 사용자입니다."));
        try{
            log.info("[getProvider] 제공자 조회 로직 시작");
            List<ProviderTable> providerTables = providerTableRepository.findAllByUserId(idTable);
            List<ProviderResponseDto> providerDtos = providerTables.stream().map(providerTable -> ProviderResponseDto.builder()
                    .providerName(providerTable.getProviderName())
                    .providerType(providerTable.getProviderType())
                    .providerId(providerTable.getProviderId())
                    .providerAddress(providerTable.getProviderArea() + " " + providerTable.getProviderSubArea() + " " + providerTable.getProviderDetailArea())
                    .providerPhoneNum(providerTable.getProviderPhoneNum())
                    .build()).toList();
            if(providerTables.isEmpty()){
                log.error("[getProvider] 제공자가 존재하지 않습니다.");
                throw new CustomException("제공자가 존재하지 않습니다.");
            }
            return providerDtos;
        } catch (Exception e){
            log.error("[getProvider] db데이터 베이스 접근 오류");
            throw new RuntimeException("데이터베이스 접근 중 오류가 발생했습니다.", e);
        }
    }

    @Override
    public List<ProviderResponseDto> getProviderListWithConfirm(String userId) throws CustomException{
        IdTable idTable = idTableRepository.findById(userId).orElseThrow(() -> new CustomException("존재하지 않는 사용자입니다."));
        List<ProviderTable> providerTables = providerTableRepository.findByUserIdAndProviderStatus(idTable, UserStatus.ACTIVE.getCode());
        List<ProviderResponseDto> providerDtos = providerTables.stream().map(providerTable -> ProviderResponseDto.builder()
                .providerName(providerTable.getProviderName())
                .providerType(providerTable.getProviderType())
                .providerId(providerTable.getProviderId())
                .providerAddress(providerTable.getProviderArea() + " " + providerTable.getProviderSubArea() + " " + providerTable.getProviderDetailArea())
                .providerPhoneNum(providerTable.getProviderPhoneNum())
                .build()).toList();
        if(providerTables.isEmpty()){
            log.error("[getProviderListWithConfirm] 제공자가 존재하지 않습니다.");
            throw new CustomException("제공자가 존재하지 않습니다.");
        }
        return providerDtos;
    }

    @Override
    public List<ProviderReservationDto> getProviderReservationList(int providerId) throws CustomException{
        ProviderTable providerTable = providerTableRepository.findById(providerId).orElseThrow(() -> new CustomException("존재하지 않는 제공자입니다."));
        List<ReservationTable> reservationTables = reservationTableRepository.findReservationTableByProviderId(providerTable.getProviderId());
        List<ProviderReservationDto> providerReservationDtos = reservationTables.stream().map(reservationTable -> ProviderReservationDto.builder()
                .reservationId(reservationTable.getReservationId())
                .reservationStartTime(reservationTable.getReservation_start_date())
                .reservationEndTime(reservationTable.getReservation_end_date())
                .reservationStatus(reservationTable.getStatus())
                .providerType(providerTable.getProviderType())
                .providerName(providerTable.getProviderName())
                .build()).toList();
        if(reservationTables.isEmpty()){
            log.error("[getProviderReservationList] 예약이 존재하지 않습니다.");
           throw new CustomException("예약이 존재하지 않습니다.", HttpStatus.NOT_FOUND.value());
        }
        return providerReservationDtos;
    }

    @Override
    @Operation(summary = "허가된 제공자 목록 조회", description = "허가된 제공자 목록을 조회합니다.")
    public List<ProviderListDto> getConfirmedProviderList() throws CustomException {
        try {
            log.info("[getConfirmedProviderList] 제공자 리스트 조회 로직 시작");
            long now = System.currentTimeMillis();
            List<ProviderTable> providerTables = providerTableRepository.findAllByProviderStatus(UserStatus.ACTIVE.getCode());
            List<ProviderListDto> providerList = providerTables.stream().map(providerTable -> ProviderListDto.builder()
                    .providerId(providerTable.getProviderId())
                    .providerName(providerTable.getProviderName())
                    .providerPhone(providerTable.getProviderPhoneNum())
                    .providerArea(providerTable.getProviderArea())
                    .providerSubArea(providerTable.getProviderSubArea())
                    .providerDetailArea(providerTable.getProviderDetailArea())
                    .providerPrice(providerTable.getProviderPrice())
                    .providerType(providerTable.getProviderType())
                    .providerRepPhoto(providerTable.getProviderRepPhoto())
                    .providerRepPhotoPath(providerTable.getProviderRepPhotoPath())
                    .dateList(providerTable.getWorkdateTableList().stream().map(workdateTable -> DateListDto.builder()
                            .date(workdateTable.getWorkDate())
                            .time(workdateTable.getWorkTime())
                            .build()).toList())
                    .build()).toList();
            if (providerTables.isEmpty()) {
                log.error("[getConfirmedProviderList] not exist.");
                throw new CustomException("제공자가 존재하지 않습니다.", HttpStatus.NOT_FOUND.value());
            }
            long end = System.currentTimeMillis();
            log.info("[getConfirmedProviderList] 제공자 리스트 조회 로직 종료, 소요시간 : " + (end - now) + "ms");
            return providerList;
        } catch (Exception e) {
            log.error("[getConfirmedProviderList] db데이터 베이스 접근 오류");
            throw new RuntimeException("데이터베이스 접근 중 오류가 발생했습니다.", e);
        }
    }

    @Override
    public void updateProviderWorkDate(WorkdateDto workdateDto) throws CustomException {
        IdTable idTable = idTableRepository.findById(workdateDto.getUserId()).orElseThrow(() -> new CustomException("존재하지 않는 사용자입니다."));
        ProviderTable providerTable = providerTableRepository.findByUserId(idTable).orElseThrow(() -> new CustomException("존재하지 않는 제공자입니다."));

        for(WorkDateRequestDto dateListDto : workdateDto.getDateList()){
            for(String time : dateListDto.getTime()) {
                WorkdateTable workdateTable = WorkdateTable.builder()
                        .providerId(providerTable)
                        .workDate(dateListDto.getDate())
                        .workTime(time)
                        .build();
                try{
                    workdateTableRepository.save(workdateTable);
                }catch (Exception e){
                    log.error("[updateProviderWorkDate] db데이터 베이스 접근 오류");
                    throw new CustomException("데이터베이스 접근 중 오류가 발생했습니다.", HttpStatus.INTERNAL_SERVER_ERROR.value());
                }
            }
        }
    }

    @Override
    public ReservationProviderResponseDto refuseReservation(int reservationId, int providerId) throws CustomException {
        ReservationTable reservationTable = reservationTableRepository.findById(reservationId).orElseThrow(() -> new CustomException("존재하지 않는 예약입니다."));
        if(reservationTable.getStatus() != ReservationStatus.WAITING.getCode()){
            log.error("[refuseReservation] 이미 처리된 예약입니다.");
            throw new CustomException("이미 처리된 예약입니다.", HttpStatus.BAD_REQUEST.value());
        }

        if(reservationTable.getReservation_camera() != providerId && reservationTable.getReservation_hair() != providerId && reservationTable.getReservation_studio() != providerId){
            log.error("[refuseReservation] 권한이 없습니다.");
            throw new CustomException("권한이 없습니다.", HttpStatus.FORBIDDEN.value());
        }

        reservationTable.setStatus(ReservationStatus.CANCELLED.getCode());
        try{
            reservationTableRepository.save(reservationTable);
            paymentService.getCancelApprove(reservationTable.getTid().getPaymentId(), reservationTable.getUserId().getId());
            return ReservationProviderResponseDto.builder()
                    .reservationId(reservationTable.getReservationId())
                    .userId(reservationTable.getUserId().getId())
                    .providerId(providerId)
                    .build();
        }catch (Exception e){
            log.error("[refuseReservation] db데이터 베이스 접근 오류");
            throw new CustomException("데이터베이스 접근 중 오류가 발생했습니다.", HttpStatus.INTERNAL_SERVER_ERROR.value());
        }
    }

    @Override
    public void acceptReservation(int reservationId) throws CustomException {
        ReservationTable reservationTable = reservationTableRepository.findById(reservationId).orElseThrow(() -> new CustomException("존재하지 않는 예약입니다."));
        reservationTable.setStatus(ReservationStatus.CONFIRMED.getCode());
        try{
            reservationTableRepository.save(reservationTable);
        }catch (Exception e){
            log.error("[acceptReservation] db데이터 베이스 접근 오류");
            throw new CustomException("데이터베이스 접근 중 오류가 발생했습니다.", HttpStatus.INTERNAL_SERVER_ERROR.value());
        }
    }

    @Override
    public void completeReservation(int reservationId) throws CustomException {
        ReservationTable reservationTable = reservationTableRepository.findById(reservationId).orElseThrow(() -> new CustomException("존재하지 않는 예약입니다."));
        reservationTable.setStatus(ReservationStatus.COMPLETED.getCode());
        try{
            reservationTableRepository.save(reservationTable);
        }catch (Exception e){
            log.error("[completeReservation] db데이터 베이스 접근 오류");
            throw new CustomException("데이터베이스 접근 중 오류가 발생했습니다.", HttpStatus.INTERNAL_SERVER_ERROR.value());
        }
    }

}


