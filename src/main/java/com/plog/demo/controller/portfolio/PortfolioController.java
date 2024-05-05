package com.plog.demo.controller.portfolio;


import com.plog.demo.common.file.PortfolioFileStore;
import com.plog.demo.dto.ErrorDto;
import com.plog.demo.dto.SuccessDto;
import com.plog.demo.dto.file.UploadFileDto;
import com.plog.demo.dto.portfolio.PortfolioResponseDto;
import com.plog.demo.dto.portfolio.PortfolioUpdateDto;
import com.plog.demo.dto.portfolio.PortfolioUploadDto;
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
import org.springframework.web.multipart.MultipartFile;

import java.net.MalformedURLException;
import java.util.List;

@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/portfolio")
@Tag(name = "Payment", description = "포트폴리오 API")
public class PortfolioController {

    private final PortfolioService portfolioService;
    private final PortfolioFileStore portfolioFileStore;

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

        List<MultipartFile> portfolioUploadFiles = portfolioUploadDto.getPortfolioUploadFiles();

        for(MultipartFile portfolioUploadFile : portfolioUploadFiles){
            String originalFilename = portfolioUploadFile.getOriginalFilename();
            String fileExtension = getFileExtension(originalFilename);

            if(isNotSupportedExtension(fileExtension)){
                throw new CustomException("지원하지 않는 파일 형식입니다.", HttpStatus.BAD_REQUEST.value());
            }
        }


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
    public ResponseEntity<SuccessDto> deletePortfolios(@PathVariable int portfolioId) throws CustomException {

        boolean isDeleted = portfolioService.deletePortfolio(portfolioId);

        if(!isDeleted){
            throw new RuntimeException("파일 저장중 에러 발생");
        }


        return ResponseEntity.status(HttpStatus.OK).body(SuccessDto.builder().message("성공").build());

    }

    @PutMapping("/update")
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "포트폴리오 삭제 완료",
                    content = @Content(schema = @Schema(implementation = PortfolioUpdateDto.class))
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "포트폴리오 삭제 실패",
                    content = @Content(schema = @Schema(implementation = ErrorDto.class))
            )
    })
    public ResponseEntity<PortfolioUpdateDto> updatePortfolio(@RequestBody PortfolioUpdateDto portfolioUpdateDto) throws CustomException {
        return ResponseEntity.status(HttpStatus.OK).body(portfolioService.updatePortfolio(portfolioUpdateDto));
    }

    /**
     * TODO 수정필요
     */
    @GetMapping("/image/{middleDir}/{fileName}")
    public ResponseEntity<Resource> getImage(@PathVariable String middleDir, @PathVariable String fileName) throws CustomException, MalformedURLException {
        String fileExtension = getFileExtension(fileName);

        if(isNotSupportedExtension(fileExtension)){
            throw new CustomException("지원되지 않는 파일 형식입니다.", HttpStatus.BAD_REQUEST.value());
        }

        HttpHeaders httpHeaders = new HttpHeaders();


        if(fileExtension.equalsIgnoreCase("jpg")){
            httpHeaders.setContentType(MediaType.IMAGE_JPEG);
        }

        if(fileExtension.equalsIgnoreCase("png")){
            httpHeaders.setContentType(MediaType.IMAGE_PNG);
        }

        return ResponseEntity.status(HttpStatus.OK)
                .headers(httpHeaders)
                .body(new UrlResource("file:" + portfolioFileStore.getFullPath(middleDir, fileName)));
    }

    private boolean isNotSupportedExtension(String fileExtension) {
        return !fileExtension.equalsIgnoreCase("jpg") && !fileExtension.equalsIgnoreCase("png");
    }

    private String getFileExtension(String originalFilename) throws CustomException {
        if(originalFilename == null){
            throw new CustomException("파일 이름이 존재하지 않습니다.", HttpStatus.BAD_REQUEST.value());
        }
        return originalFilename.substring(originalFilename.lastIndexOf(".") + 1);
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