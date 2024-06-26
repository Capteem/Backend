package com.plog.demo.controller.portfolio;

import com.plog.demo.dto.ErrorDto;
import com.plog.demo.dto.SuccessDto;
import com.plog.demo.dto.file.UploadFileDto;
import com.plog.demo.dto.portfolio.*;
import com.plog.demo.exception.CustomException;
import com.plog.demo.service.portfolio.PortfolioService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.MalformedURLException;


import java.util.List;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/portfolio")
@Tag(name = "Portfolio", description = "포트폴리오 API")
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class PortfolioController {

    private final PortfolioService portfolioService;

    @Operation(summary = "포트 폴리오 조회", description = "포트 폴리오 조회합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200",
                    description = "포트폴리오 조회 성공",
                    content = @Content(schema = @Schema(implementation = PortfolioResponseDto.class))),
            @ApiResponse(responseCode = "400",
                    description = "포트 폴리오 조회 실패",
                    content = @Content(schema = @Schema(implementation = ErrorDto.class)))
    })
    @GetMapping("/data/{providerId}")
    public ResponseEntity<PortfolioResponseDto> getPortfolio(@PathVariable int providerId) throws CustomException {

        return ResponseEntity.status(HttpStatus.OK).body(portfolioService.getPortfolio(providerId));
    }

    @Operation(summary = "포트 폴리오 랜덤 조회", description = "포트 폴리오 랜덤 조회합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200",
                    description = "포트폴리오 랜덤 조회 성공",
                    content = @Content(array = @ArraySchema(schema = @Schema(implementation = PortfolioRandomResponseDto.class)))),
            @ApiResponse(responseCode = "400",
                    description = "포트 폴리오 랜덤 조회 실패",
                    content = @Content(schema = @Schema(implementation = ErrorDto.class)))
    })
    @GetMapping("/random")
    public ResponseEntity<List<PortfolioRandomResponseDto>> getPortfolioRandom(@RequestParam int page) throws CustomException {

        return ResponseEntity.status(HttpStatus.OK).body(portfolioService.getPortfolioRandom(page));
    }

    @Operation(summary = "포트 폴리오 등록", description = "포트 폴리오 등록합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200",
                    description = "포트폴리오 등록 성공",
                    content = @Content(array = @ArraySchema(schema = @Schema(implementation = UploadFileDto.class)))),
            @ApiResponse(responseCode = "400",
                    description = "포트 폴리오 등록 실패",
                    content = @Content(schema = @Schema(implementation = ErrorDto.class)))
    })
    @PostMapping("/upload")
    public ResponseEntity<List<UploadFileDto>> addPortfolios(@ModelAttribute PortfolioUploadDto portfolioUploadDto) throws CustomException {


        return ResponseEntity.status(HttpStatus.OK).body(portfolioService.addPortfolios(portfolioUploadDto));
    }


    @Operation(summary = "포트 폴리오 삭제", description = "포트 폴리오 삭제합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200",
                    description = "포트폴리오 삭제 성공",
                    content = @Content(schema = @Schema(implementation = String.class))),
            @ApiResponse(responseCode = "400",
                    description = "포트 폴리오 삭제 실패",
                    content = @Content(schema = @Schema(implementation = ErrorDto.class)))
    })
    @DeleteMapping("/{portfolioId}")
    public ResponseEntity<SuccessDto> deletePortfolio(@PathVariable int portfolioId) throws CustomException {

        boolean isDeleted = portfolioService.deletePortfolio(portfolioId);

        if(!isDeleted){
            throw new RuntimeException("파일 삭제중 에러 발생");
        }

        return ResponseEntity.status(HttpStatus.OK).body(SuccessDto.builder().message("성공").build());

    }


    @Operation(summary = "이미지 파일 불러오기", description = "이미지 파일을 불러온다.")
    @ApiResponse(responseCode = "200", description = "이미지 파일 불러오기 성공")
    @GetMapping("/image/{middleDir}/{fileName}")
    public ResponseEntity<Resource> getImage(@PathVariable String middleDir, @PathVariable String fileName) throws CustomException, MalformedURLException {

        PortfolioImageDto portfolioImageDto = portfolioService.getImage(middleDir, fileName);

        Path filePath = Paths.get(portfolioImageDto.getImgFullPath());


        // 파일 MIME 타입 결정
        String contentType;

        try {
            contentType = Files.probeContentType(filePath);
        } catch (IOException e) {
            e.printStackTrace();
            contentType = "application/octet-stream";
        }

        return ResponseEntity.status(HttpStatus.OK)
                .contentType(MediaType.parseMediaType(contentType))
                .body(new UrlResource("file:" + portfolioImageDto.getImgFullPath()));
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
