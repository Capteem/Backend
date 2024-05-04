package com.plog.demo.controller.portfolio;

import com.plog.demo.common.file.PortfolioFileStore;
import com.plog.demo.dto.ErrorDto;
import com.plog.demo.dto.file.UploadFileDto;
import com.plog.demo.dto.portfolio.PortfolioResponseDto;
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
import org.springframework.http.HttpStatus;
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
            String fileExtension = originalFilename.substring(originalFilename.lastIndexOf(".") + 1);

            if(isNotSupportedExtension(fileExtension)){
                throw new CustomException("지원하지 않는 파일 형식입니다.", HttpStatus.BAD_REQUEST.value());
            }
        }


        return ResponseEntity.status(HttpStatus.OK).body(portfolioService.addPortfolios(portfolioUploadDto));
    }


    /**
     * TODO 조금 더 수정 필요?
     */
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
    public ResponseEntity<String> deletePortfolios(@PathVariable int portfolioId) throws CustomException {

        boolean isDeleted = portfolioService.deletePortfolio(portfolioId);

        if(!isDeleted){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("포트폴리오 제거 중 에러 발생");
        }

        return ResponseEntity.status(HttpStatus.OK).body("포트폴리오 정상 삭제");
    }

    /**
     * TODO 포트폴리오 수정 제목 등
     */

    private boolean isNotSupportedExtension(String fileExtension) {
        return !fileExtension.equalsIgnoreCase("jpg") && !fileExtension.equalsIgnoreCase("png");
    }

//    @GetMapping("/images/{filename}")
//    public ResponseEntity<Resource> downloadImage(@PathVariable String filename) throws MalformedURLException {
//        return ResponseEntity.status(HttpStatus.OK).body(new UrlResource("file:" + portfolioFileStore.getFullPath("20240501/", filename)));
//    }



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
