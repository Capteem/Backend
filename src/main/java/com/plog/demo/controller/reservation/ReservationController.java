package com.plog.demo.controller.reservation;


import com.plog.demo.dto.reservation.ReservationDto;
import com.plog.demo.service.reservation.ReservationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/reservation")
public class ReservationController {

    private final ReservationService reservationService;

    @PostMapping()
    public ReservationDto makeReservation(@RequestBody ReservationDto reservationDto){

        ReservationDto result = reservationService.addReservation(reservationDto);

        if(result == null){
            return null;
        }
        return result;
    }

    @PostMapping("/cancel")
    public ResponseEntity<Map<String, Integer>> cancelReservation(@RequestBody int reservationId){

            Map<String, Integer> responseData = new HashMap<>();

            try {
                reservationService.deleteReservation(reservationId);
                responseData.put("예약 취소 성공", reservationId);
                return ResponseEntity.status(200).body(responseData);
            }catch (Exception e){
                responseData.put("예약 취소 실패", reservationId);
                return ResponseEntity.status(400).body(responseData);
            }
    }
}