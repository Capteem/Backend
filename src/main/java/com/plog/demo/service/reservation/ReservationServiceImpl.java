package com.plog.demo.service.reservation;


import com.plog.demo.dto.reservation.ReservationDto;
import com.plog.demo.model.ReservationTable;
import com.plog.demo.repository.ReservationTableRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class ReservationServiceImpl implements ReservationService{

    private final ReservationTableRepository reservationTableRepository;

    @Override
    public void deleteReservation(int reservationId) {

        log.info("[deleteReservation] 예약 삭제를 시작합니다.");
        ReservationTable reservationTable = reservationTableRepository.findById(reservationId).get();
        if(reservationTable == null){
            log.info("[deleteReservation] 예약이 존재하지 않습니다.");
            return;
        }
        try{
            reservationTable.setStatus("CANCEL");
            reservationTableRepository.save(reservationTable);
            log.info("[deleteReservation] 예약이 취소되었습니다.");
        } catch (DataAccessException e){
            log.info("[deleteReservation] 데이터 베이스 접근 오류");
        }
    }

    @Override
    public ReservationDto addReservation(ReservationDto reservationDto) {

        log.info("[addReservation] {}", reservationDto);

        List<ReservationTable> reservations = reservationTableRepository.findAll();


        /**
         * TODO 예약 동시성 컨트롤 체크 해야함, 로직 개선 필요
         */

        // 예약 중복 검증
        if (isCameraDuplicate(reservations, reservationDto) ||
                isStudioDuplicate(reservations, reservationDto) ||
                isHairDuplicate(reservations, reservationDto)) {
            log.info("[addReservation] 중복발생");
            return null;
        }

        // TODO status 확인 후 추가
        ReservationTable reservation = ReservationTable.builder()
                .reservationId(reservationDto.getUserId())
                .reservation_camera(reservationDto.getReservationCameraId())
                .reservation_studio(reservationDto.getReservationStudioId())
                .reservation_hair(reservationDto.getReservationHairId())
                .reservation_date(reservationDto.getReservationDate())
                .build();

        try {
            ReservationTable saveReservation = reservationTableRepository.save(reservation);
        }catch (DataIntegrityViolationException e){
            log.info("[addReservation] 복합 유니크 제약 조건 위반");
            return null;
        }catch(Exception e){
            log.info("[addReservation] {}" ,e.getMessage());
            log.info("[addReservation] {}" ,e.getCause());
            return null;
        }
//        if(saveReservation == null){
//
//            log.info("[addReservation] 이미 저장되어 있음");
//            return null;
//        }

        return reservationDto;
    }

    /**
     * 사진 작가 중복 체크
     */
    private boolean isCameraDuplicate(List<ReservationTable> reservations, ReservationDto reservationDto) {

        //사진작가 기존 예약 목록
        List<ReservationTable> foundReserations = reservations.stream().filter(
                reservation -> reservation.getReservation_camera() == reservationDto.getReservationCameraId()
        ).toList();

        //사진작가 기존 예약 목록과 요청 날짜 중복 체크
        Optional<ReservationTable> foundReservation = foundReserations.stream()
                .filter(reservation -> reservation.getReservation_date().equals(reservationDto.getReservationDate()))
                .findAny();

        //사진작가 예약 날짜 중복
        if(foundReservation.isPresent()){
            return true;
        }

        return false;
    }

    /**
     * 스튜디오 중복 체크
     */
    private boolean isStudioDuplicate(List<ReservationTable> reservations, ReservationDto reservationDto) {

        //스튜디오 기존 예약 목록
        List<ReservationTable> foundReservations = reservations.stream().filter(
                reservation -> reservation.getReservation_studio() == reservationDto.getReservationStudioId()
        ).toList();

        //스튜디오 기존 예약 목록과 요청 날짜 중복 체크
        Optional<ReservationTable> foundReservation = foundReservations.stream()
                .filter(reservation -> reservation.getReservation_date().equals(reservationDto.getReservationDate()))
                .findAny();

        //스튜디오 예약 중복
        if(foundReservation.isPresent()){
            return true;
        }

        return false;
    }

    /**
     * 헤어메이크업 중복 체크
     */
    private boolean isHairDuplicate(List<ReservationTable> reservations, ReservationDto reservationDto) {

        //헤어메이크업 기존 예약 목록
        List<ReservationTable> foundReservations = reservations.stream().filter(
                reservation -> reservation.getReservation_hair() == reservationDto.getReservationHairId()
        ).toList();

        //헤어메이크업 기존 예약 목록과 요청 날짜 중복 체크
        Optional<ReservationTable> foundReservation = foundReservations.stream()
                .filter(reservation -> reservation.getReservation_date().equals(reservationDto.getReservationDate()))
                .findAny();

        //헤어메이크업 예약 중복
        if(foundReservation.isPresent()){
            return true;
        }

        return false;
    }

}
