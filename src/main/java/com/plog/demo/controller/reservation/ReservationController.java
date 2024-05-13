package com.plog.demo.controller.reservation;
import com.plog.demo.dto.ErrorDto;
import com.plog.demo.dto.SuccessDto;
import com.plog.demo.dto.reservation.ReservationRequestDto;
import com.plog.demo.dto.reservation.ReservationResponseDto;
import com.plog.demo.dto.review.ReviewGetResponseDto;
import com.plog.demo.exception.CustomException;
import com.plog.demo.service.reservation.ReservationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
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
@Tag(name = "Reservation", description = "예약 관련 API")
public class ReservationController {


    private final ReservationService reservationService;

    @ApiResponses({
            @ApiResponse(responseCode = "200",
                    description = "사용자가 예약",
                    content = @Content(schema = @Schema(implementation = SuccessDto.class))),
            @ApiResponse(responseCode = "400",
                    description = "예약 실패",
                    content = @Content(schema = @Schema(implementation = ErrorDto.class)))
    })
    @Operation(summary = "사용자가 예약 ", description = "예약을 시작합니다.")
    @PostMapping("/booking")
    public ResponseEntity<SuccessDto> makeReservation(@RequestBody ReservationRequestDto reservationRequestDto) throws CustomException {


        reservationService.addReservation(reservationRequestDto);
        

        return ResponseEntity.status(HttpStatus.OK).body(SuccessDto.builder().message("예약 성공").build());
    }

    @ApiResponses({
            @ApiResponse(responseCode = "200",
                    description = "사용자가 예약 목록 확인",
                    content = @Content(schema = @Schema(implementation = ReservationResponseDto.class))),
            @ApiResponse(responseCode = "400",
                    description = "예약 목록 확인 실패",
                    content = @Content(schema = @Schema(implementation = ErrorDto.class)))
    })
    @Operation(summary = "사용자가 예약 목록 확인", description = "확인된 제공자 목록을 조회합니다.")
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


    @ExceptionHandler(CustomException.class)
    public ResponseEntity<ErrorDto> customExceptionHandler(CustomException e){
        log.warn("customExceptionHandler 호출, {}, {}", e.getCause(), e.getMessage());

        ErrorDto errorDto = ErrorDto.builder()
                .resultCode(e.getResultCode())
                .msg(e.getMessage())
                .build();

        return ResponseEntity.status(e.getResultCode()).body(errorDto);
    }


    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<ErrorDto> runtimeExceptionHandler(Exception e){
        log.error("runtimeExceptionHandler 호출, {}, {}", e.getCause(), e.getMessage());

        ErrorDto errorDto = ErrorDto.builder()
                .resultCode(HttpStatus.INTERNAL_SERVER_ERROR.value())
                .msg(HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase())
                .build();

        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorDto);
    }
}