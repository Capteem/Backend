package com.plog.demo.controller.reservation;
import com.plog.demo.dto.ErrorDto;
import com.plog.demo.dto.reservation.ReservationRequestDto;
import com.plog.demo.dto.reservation.ReservationResponseDto;
import com.plog.demo.exception.CustomException;
import com.plog.demo.service.reservation.ReservationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/reservation")
@Slf4j
public class ReservationController {

    /**
     * TODO 인터셉터로 토큰 검증 미리 해야함
     */

    private final ReservationService reservationService;

    @PostMapping("/booking")
    public ResponseEntity<Map<String, String>> makeReservation(@RequestBody ReservationRequestDto reservationRequestDto) throws CustomException {


        reservationService.addReservation(reservationRequestDto);

        Map<String, String> responseData = new HashMap<>();

        responseData.put("msg", "예약 성공");

        return ResponseEntity.status(HttpStatus.OK).body(responseData);
    }

    @PostMapping("/list")
    public ResponseEntity<List<ReservationResponseDto>> getReservations(@RequestBody Map<String, String> requestBody) throws CustomException {


        List<ReservationResponseDto> reservations = reservationService.getReservationAll(requestBody.get("userId"));

        return ResponseEntity.status(HttpStatus.OK).body(reservations);
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

    /**
     * 커스텀 예외
     */
    @ExceptionHandler(CustomException.class)
    public ResponseEntity<ErrorDto> customExceptionHandler(CustomException e){
        log.info("customExceptionHandler 호출, {}, {}", e.getCause(), e.getMessage());

        ErrorDto errorDto = ErrorDto.builder()
                .resultCode(e.getResultCode())
                .msg(e.getMessage())
                .build();

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorDto);
    }

    /**
     * 서부 내부 에러 TODO 나중에 더 손좀 봐줘야함 이녀석
     */
    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<ErrorDto> runtimeExceptionHandler(Exception e){
        log.info("runtimeExceptionHandler 호출, {}, {}", e.getCause(), e.getMessage());

        ErrorDto errorDto = ErrorDto.builder()
                .resultCode(HttpStatus.INTERNAL_SERVER_ERROR.value())
                .msg(HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase())
                .build();

        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorDto);
    }
}