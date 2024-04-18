package com.plog.demo.controller.reservation;


import com.plog.demo.dto.reservation.ReservationDto;
import com.plog.demo.repository.ReservationTableRepository;
import com.plog.demo.service.reservation.ReservationService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/reservation")
public class Reservation {

    private final ReservationService reservationService;
    private final ReservationTableRepository reservationTableRepository;//지워야함

    @PostMapping()
    public ReservationDto makeReservation(@RequestBody ReservationDto reservationDto){

        ReservationDto result = reservationService.addReservation(reservationDto);

        if(result == null){
            return null;
        }
        return result;
    }

    @DeleteMapping("/all-delet")
    public void allDelete(){
        reservationTableRepository.deleteAll();
    }
}
