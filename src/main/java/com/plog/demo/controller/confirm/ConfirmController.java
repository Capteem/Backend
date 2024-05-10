package com.plog.demo.controller.confirm;

import com.plog.demo.dto.ErrorDto;
import com.plog.demo.dto.SuccessDto;
import com.plog.demo.dto.confirm.ConfirmCheckProviderRequestDto;
import com.plog.demo.dto.confirm.ConfirmRequestDto;
import com.plog.demo.dto.confirm.ConfirmResponseDto;
import com.plog.demo.exception.CustomException;
import com.plog.demo.service.confirm.ConfirmService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.coyote.Response;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/confirm")
@Slf4j
@CrossOrigin(origins = "*", allowedHeaders = "*")
@Tag(name = "Confirm", description = "사업자 번호 확인 API")
public class ConfirmController {

    private final ConfirmService confirmService;


    @GetMapping("/check")
    @Operation(summary = "사업자 번호 확인", description = "사업자 번호를 확인합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "사업자 번호 확인 로직 성공",
                    content = @Content(
                            schema = @Schema(implementation = Integer.class),
                            examples = {@ExampleObject(value = "1", description = "사업자 번호 일치")}
                    )),
            @ApiResponse(responseCode = "201", description = "사업자 번호 확인 로직 성공",
                    content = @Content(
                            schema = @Schema(implementation = Integer.class),
                            examples = {@ExampleObject(value = "0", description = "사업자 번호 불일치")}
                    )),
            @ApiResponse(responseCode = "400", description = "사업자 번호 확인 실패")
    })
    public ResponseEntity<Object> checkBusinessStatus(
            @Parameter(description = "사업자등록번호 값", example = "000000000(10자리 숫자, -제거)", required = true) @RequestParam String businessNumber){
        try{
            ConfirmResponseDto confirmResponseDto = confirmService.getBusinessStatus(businessNumber);
            if (confirmResponseDto.getMatch_cnt() == 1){
                return ResponseEntity.status(200).body(1);
            } else {
                return ResponseEntity.status(201).body(0);
            }
        } catch (Exception e){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("사업자 번호 확인 중 오류가 발생했습니다..");
        }
    }

    @PostMapping("/checkProvider")
    @Operation(summary = "제공자 등록 체크(사진작가, 헤어메이크업)", description = "제공자(사진작가, 헤어메이크업)를 등록하고 체크합니다.")
    @ApiResponse(responseCode = "200", description = "제공자 등록 성공", content = @Content(schema = @Schema(implementation = SuccessDto.class)))
    public ResponseEntity<SuccessDto> checkProvider(@ModelAttribute ConfirmCheckProviderRequestDto confirmCheckProviderRequestDto) throws CustomException {


        confirmService.checkProvider(confirmCheckProviderRequestDto);

        return ResponseEntity.status(HttpStatus.OK).body(SuccessDto.builder().message("성공").build());
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
