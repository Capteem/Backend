package com.plog.demo.service.Reservation;

import com.plog.demo.dto.Reservation.ReservationDeleteDto;
import com.plog.demo.dto.Reservation.ReservationMakeDto;

import java.util.Date;

public interface ReservationService {

    ReservationMakeDto makeReservation(ReservationMakeDto reservationMakeDto);
    void deleteReservation(String reservationId, String userId);
}
