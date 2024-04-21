package com.plog.demo.service.reservation;

import com.plog.demo.dto.reservation.ReservationDto;
import com.plog.demo.exception.CustomException;

public interface ReservationService {

    void deleteReservation(int reservationId);

    ReservationDto addReservation(ReservationDto reservationDto) throws CustomException;

}
