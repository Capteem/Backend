package com.plog.demo.controller.reservation;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.plog.demo.config.JwtTokenProvider;
import com.plog.demo.dto.ErrorDto;
import com.plog.demo.dto.reservation.ReservationRequestDto;
import com.plog.demo.dto.reservation.ReservationResponseDto;
import com.plog.demo.exception.CustomException;
import com.plog.demo.service.reservation.ReservationService;
import jakarta.servlet.http.HttpServletRequest;
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

    private final ReservationService reservationService;
    private final JwtTokenProvider jwtTokenProvider;

    @PostMapping()
    public ResponseEntity<ReservationResponseDto> makeReservation(@RequestBody ReservationRequestDto reservationRequestDto,
                                                                  HttpServletRequest request) throws CustomException {

        /**
         * TODO 인터셉터로 토큰 검증 미리 해야함
         */
        String accessToken = jwtTokenProvider.resolveToken(request);
        String userId = jwtTokenProvider.getUserId(accessToken);

        ReservationResponseDto result = reservationService.addReservation(reservationRequestDto, userId);

        return ResponseEntity.status(HttpStatus.OK).body(result);
    }

    @GetMapping()
    public ResponseEntity<List<ReservationResponseDto>> getReservations(HttpServletRequest request) throws CustomException {
        /**
         * TODO 인터셉터로 토큰 검증 미리 해야함
         */
        String accessToken = jwtTokenProvider.resolveToken(request);
        String userId = jwtTokenProvider.getUserId(accessToken);

        List<ReservationResponseDto> reservations = reservationService.getReservationAll(userId);

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