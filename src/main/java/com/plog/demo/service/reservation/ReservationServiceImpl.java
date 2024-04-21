package com.plog.demo.service.reservation;


import com.plog.demo.dto.reservation.ReservationDto;
import com.plog.demo.exception.CustomException;
import com.plog.demo.model.ReservationTable;
import com.plog.demo.repository.ReservationTableRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class ReservationServiceImpl implements ReservationService{

    private final ReservationTableRepository reservationTableRepository;

    @Override
    public void deleteReservation(String reservationId, String userId) {
        // TODO Auto-generated method stub

    }

    @Override
    public ReservationDto addReservation(ReservationDto reservationDto) throws CustomException {


        log.info("[addReservation] 예약 서비스 로직 시작");

        if (isOverlappingReservation(reservationDto)) {
            throw new CustomException("예약 날짜 중복", HttpStatus.CONFLICT.value());
        }

        ReservationTable reservation = ReservationTable.builder()
                .reservation_camera(reservationDto.getReservationCameraId())
                .reservation_studio(reservationDto.getReservationStudioId())
                .reservation_hair(reservationDto.getReservationHairId())
                .reservation_start_date(reservationDto.getReservationStartDate())
                .reservation_end_date(reservationDto.getReservationEndDate())
                .reservationId(reservationDto.getUserId())
                .build();

        reservationTableRepository.save(reservation);

        log.info("[addReservation] 예약 완료");

        return reservationDto;
    }

    private boolean isOverlappingReservation(ReservationDto reservationDto) {
        List<ReservationTable> overlappingReservations = reservationTableRepository.findReservationTableWithLock(
                 reservationDto.getReservationCameraId(),
                 reservationDto.getReservationHairId(),
                 reservationDto.getReservationStudioId(),
                 reservationDto.getReservationStartDate(),
                 reservationDto.getReservationEndDate());

        if(!overlappingReservations.isEmpty()){
            log.info("[addReservation] 예약 날짜 중복");
            return true;
        }
        return false;
    }

}
