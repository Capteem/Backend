package com.plog.demo.service.reservation;

import com.plog.demo.dto.reservation.ReservationDto;

public interface ReservationService {

    void deleteReservation(String reservationId, String userId);

    ReservationDto addReservation(ReservationDto reservationDto);

}
