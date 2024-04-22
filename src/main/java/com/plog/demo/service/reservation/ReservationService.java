package com.plog.demo.service.reservation;

import com.plog.demo.dto.reservation.ReservationRequestDto;
import com.plog.demo.dto.reservation.ReservationResponseDto;
import com.plog.demo.exception.CustomException;

import java.util.List;

public interface ReservationService {

    void deleteReservation(int reservationId);

    ReservationResponseDto addReservation(ReservationRequestDto reservationRequestDto, String userId) throws CustomException;

    List<ReservationResponseDto> getReservationAll(String userId) throws CustomException;

}
