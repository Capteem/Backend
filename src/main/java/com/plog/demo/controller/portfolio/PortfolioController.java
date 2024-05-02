package com.plog.demo.controller.portfolio;

import com.plog.demo.common.file.FileStore;
import com.plog.demo.dto.ErrorDto;
import com.plog.demo.dto.file.UploadFileDto;
import com.plog.demo.dto.portfolio.PortfolioAddDto;
import com.plog.demo.dto.portfolio.PortfolioResponseDto;
import com.plog.demo.exception.CustomException;
import com.plog.demo.service.portfolio.PortfolioService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/portfolio")
public class PortfolioController {

    private final PortfolioService portfolioService;
    private final FileStore fileStore;

    @GetMapping("/data/{portfolioId}")
    public ResponseEntity<PortfolioResponseDto> getPortfolio(@PathVariable int portfolioId) throws CustomException {

        return ResponseEntity.status(HttpStatus.OK).body(portfolioService.getPortfolio(portfolioId));
    }


    /**
     * TODO 이거 그냥 테스용
     */
    @PostMapping("/upload")
    public ResponseEntity<List<UploadFileDto>> addImages(@RequestParam PortfolioAddDto portfolioAddDto) throws IOException {
        return ResponseEntity.status(HttpStatus.OK).body(fileStore.storeFiles(portfolioAddDto.getPortfolioPhotoPath()));
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
     * 서버 내부 에러
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
