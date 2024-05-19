package com.plog.demo.service.reservation;

import com.plog.demo.dto.reservation.ReservationRequestDto;
import com.plog.demo.dto.reservation.ReservationResponseDto;
import com.plog.demo.exception.CustomException;
import com.plog.demo.model.ReservationTable;

import java.util.List;

public interface ReservationService {

    void deleteReservation(int reservationId);

    ReservationTable addReservation(ReservationRequestDto reservationRequestDto) throws CustomException;

    List<ReservationResponseDto> getReservationAll(String userId) throws CustomException;

}
