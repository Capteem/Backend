package com.plog.demo.service.reservation;


import com.plog.demo.common.ReservationStatus;
import com.plog.demo.dto.reservation.ReservationRequestDto;
import com.plog.demo.dto.reservation.ReservationResponseDto;
import com.plog.demo.exception.CustomException;
import com.plog.demo.model.IdTable;

import com.plog.demo.model.ReservationTable;
import com.plog.demo.repository.IdTableRepository;

import com.plog.demo.repository.ReservationTableRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;


@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class ReservationServiceImpl implements ReservationService{

    private final ReservationTableRepository reservationTableRepository;
    private final IdTableRepository idTableRepository;
//a
    @Override
    public void deleteReservation(int reservationId) {

        log.info("[deleteReservation] 예약 삭제를 시작합니다.");
        ReservationTable reservationTable = reservationTableRepository.findById(reservationId).get();
        try{
            reservationTable.setStatus(ReservationStatus.CANCELLED.getCode());
            reservationTableRepository.save(reservationTable);
            log.info("[deleteReservation] 예약이 취소되었습니다.");
        } catch (DataAccessException e){
            log.info("[deleteReservation] 데이터 베이스 접근 오류");
        }
    }

    @Override
    public void addReservation(ReservationRequestDto reservationRequestDto) throws CustomException {


        log.info("[addReservation] 예약 서비스 로직 시작");

        if (isOverlappingReservation(reservationRequestDto)) {
            throw new CustomException("예약 날짜 중복", HttpStatus.CONFLICT.value());
        }

        IdTable idTable = idTableRepository.findById(reservationRequestDto.getUserId())
                .orElseThrow(() -> new CustomException("존재하지 않는 회원입니다.", HttpStatus.NOT_FOUND.value()));

        try {
            ReservationTable reservation = ReservationTable.builder()
                    .reservation_camera(reservationRequestDto.getReservationCameraId())
                    .reservation_studio(reservationRequestDto.getReservationStudioId())
                    .reservation_hair(reservationRequestDto.getReservationHairId())
                    .reservation_start_date(reservationRequestDto.getReservationStartDate())
                    .reservation_end_date(reservationRequestDto.getReservationEndDate())
                    .reservationCameraName(reservationRequestDto.getReservationCameraName())
                    .reservationStudioName(reservationRequestDto.getReservationStudioName())
                    .reservationHairName(reservationRequestDto.getReservationHairName())
                    .reservation_studio_confirm(true)
                    .reservation_camera_confirm(true)
                    .reservation_hair_confirm(true)
                    .status(ReservationStatus.WAITING.getCode())
                    .build();

            if (reservation.getReservation_camera() != 0) {
                reservation.setReservation_camera_confirm(false);
            }
            if (reservation.getReservation_studio() != 0) {
                reservation.setReservation_studio_confirm(false);
            }
            if (reservation.getReservation_hair() != 0) {
                reservation.setReservation_hair_confirm(false);
            }
            reservationTableRepository.save(reservation);
            log.info("[addReservation] 예약 완료");
            reservation.setUserId(idTable);
        } catch (Exception e) {
            throw new CustomException("예약 서비스 로직 오류", HttpStatus.INTERNAL_SERVER_ERROR.value());
        }

//        try {
//            reservationTableRepository.save(reservation);
//            log.info("[addReservation] 예약 완료");
//        }catch (Exception e){
//            throw new RuntimeException("데이터 베이스 오류", e);
//        }
    }

    @Override
    public List<ReservationResponseDto> getReservationAll(String userId) throws CustomException {

        log.info("[getReservationAll] 모든 예약 조회");

        // 유저 엔티티 생성
        IdTable idTable = idTableRepository.findById(userId)
                .orElseThrow(() -> new CustomException("존재하지 않는 회원입니다.", HttpStatus.NOT_FOUND.value()));

        List<ReservationTable> reservations = reservationTableRepository.findByUserId(idTable);

        //예약 없을 때
        if(reservations.isEmpty()){
            return new ArrayList<>();
        }

        List<ReservationResponseDto> reservationResponseDtoList = getReservationResponseDtoList(reservations);


        log.info("[getReservationAll] 모든 예약 조회 성공");

        return reservationResponseDtoList;
    }

    private List<ReservationResponseDto> getReservationResponseDtoList(List<ReservationTable> reservations) throws CustomException {

        List<ReservationResponseDto> reservationResponseDtoList = new ArrayList<>();

        for (ReservationTable reservation : reservations) {
            if (reservation.getTid() == null) {
                continue;
            }
            ReservationResponseDto reservationResponseDto = ReservationResponseDto.builder()
                    .reservationTableId(reservation.getReservationId())
                    .reservationCameraId(reservation.getReservation_camera())
                    .reservationStudioId(reservation.getReservation_studio())
                    .reservationHairId(reservation.getReservation_hair())
                    .reservationStartDate(reservation.getReservation_start_date())
                    .reservationEndDate(reservation.getReservation_end_date())
                    .reservationCameraName(reservation.getReservationCameraName())
                    .reservationStudioName(reservation.getReservationStudioName())
                    .reservationHairName(reservation.getReservationHairName())
                    .amount(reservation.getTid().getPaymentAmount())
                    .status(reservation.getStatus())
                    .tid(reservation.getTid().getPaymentId())
                    .build();

            reservationResponseDtoList.add(reservationResponseDto);
        }

        return reservationResponseDtoList;
    }



    private boolean isOverlappingReservation(ReservationRequestDto reservationRequestDto) {
        List<ReservationTable> overlappingReservations = reservationTableRepository.findReservationTableWithLock(
                 reservationRequestDto.getReservationCameraId(),
                 reservationRequestDto.getReservationHairId(),
                 reservationRequestDto.getReservationStudioId(),
                 reservationRequestDto.getReservationStartDate(),
                 reservationRequestDto.getReservationEndDate());

        if(!overlappingReservations.isEmpty()){
            log.info("[addReservation] 예약 날짜 중복");
            return true;
        }
        return false;
    }

}
