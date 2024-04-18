package com.plog.demo.service.reservation;

import com.plog.demo.dto.reservation.ReservationDto;

public interface ReservationService {

    void deleteReservation(int reservationId);

    ReservationDto addReservation(ReservationDto reservationDto);

}
