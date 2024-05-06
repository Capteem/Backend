package com.plog.demo.controller.provider;

import com.plog.demo.dto.ErrorDto;
import com.plog.demo.dto.Provider.ProviderAdminDto;
import com.plog.demo.dto.Provider.ProviderCheckRequestDto;
import com.plog.demo.dto.Provider.ProviderDto;
import com.plog.demo.dto.Provider.ProviderResponseDto;
import com.plog.demo.dto.SuccessDto;
import com.plog.demo.exception.CustomException;
import com.plog.demo.model.ProviderTable;
import com.plog.demo.service.Provider.ProviderService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.apache.bcel.classfile.Module;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/service")
@CrossOrigin(origins = "*", allowedHeaders = "*")
@Tag(name = "Provider", description = "서비스 제공자 API")
public class ProviderController {

    private final ProviderService providerService;

    @PostMapping("/service")
    @Operation(summary = "서비스 등록", description = "서비스를 등록합니다.")
    public ResponseEntity<Map<String, String>> signUpProvider(@RequestBody ProviderDto providerDto){

        Map<String, String> responseData = new HashMap<>();

        try {
            providerService.addProvider(providerDto);
            responseData.put("message", "서비스 등록 성공");
            return ResponseEntity.status(HttpStatus.OK).body(responseData);
        }catch (CustomException e){
            responseData.put("message", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(responseData);
        }catch (RuntimeException e) {
            responseData.put("message", "서버에서 오류가 발생했습니다.");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(responseData);
        }
    }

    @GetMapping("/check")
    @Operation(summary = "유저 서비스 등록 확인", description = "등록 확인.")
    @ApiResponse(responseCode = "200", description = "등록 확인 성공", content = @Content(schema = @Schema(implementation = ProviderResponseDto.class)))
    public ResponseEntity<List<ProviderResponseDto>> getSelectedProvider(@RequestParam String userId){
        try {
            return ResponseEntity.status(HttpStatus.OK).body(providerService.getSelectedProvider(userId));
        }catch (CustomException e){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }
    }

    @GetMapping("/all")
    @Operation(summary = "제공자 목록", description = "제공자 목록을 조회합니다.")
    @ApiResponse(responseCode = "200", description = "제공자 목록 조회 성공", content = @Content(schema = @Schema(implementation = ProviderAdminDto.class)))
    public ResponseEntity<List<ProviderAdminDto>> getAllProvider() throws CustomException {
        try{
            return ResponseEntity.status(HttpStatus.OK).body(providerService.getProviderList());
        } catch (CustomException e){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }

    }

    @PostMapping("/checkProvider")
    @Operation(summary = "제공자 등록 체크", description = "제공자를 등록하고 체크합니다.")
    @ApiResponse(responseCode = "200", description = "제공자 등록 성공", content = @Content(schema = @Schema(implementation = SuccessDto.class)))
    public ResponseEntity<SuccessDto> checkProvider(@ModelAttribute ProviderCheckRequestDto providerCheckRequestDto) throws CustomException {
        providerService.checkProvider(providerCheckRequestDto);

        return ResponseEntity.status(HttpStatus.OK).body(SuccessDto.builder().message("성공").build());
    }

    /**
     * 커스텀 예외
     */
    @ExceptionHandler(CustomException.class)
    public ResponseEntity<ErrorDto> customExceptionHandler(CustomException e){
        log.warn("customExceptionHandler 호출, {}, {}", e.getCause(), e.getMessage());

        ErrorDto errorDto = ErrorDto.builder()
                .resultCode(e.getResultCode())
                .msg(e.getMessage())
                .build();

        return ResponseEntity.status(e.getResultCode()).body(errorDto);
    }

    /**
     * 서버 내부 에러
     */
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
