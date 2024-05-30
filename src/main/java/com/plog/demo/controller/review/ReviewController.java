package com.plog.demo.controller.review;

import com.plog.demo.dto.ErrorDto;
import com.plog.demo.dto.SuccessDto;
import com.plog.demo.dto.portfolio.PortfolioResponseDto;
import com.plog.demo.dto.review.*;
import com.plog.demo.dto.review.comment.CommentAddRequestDto;
import com.plog.demo.dto.review.comment.CommentUpdateRequestDto;
import com.plog.demo.exception.CustomException;
import com.plog.demo.service.review.ReviewService;
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

@RestController
@RequiredArgsConstructor
@RequestMapping("/review")
@Slf4j
@Tag(name = "Review", description = "리뷰 관련 API")
@CrossOrigin(origins = "*", allowedHeaders = "*")

public class ReviewController {

    private final ReviewService reviewService;

    /**
     * 등록
     */
    @ApiResponses({
            @ApiResponse(responseCode = "200",
                    description = "리뷰 등록 성공",
                    content = @Content(schema = @Schema(implementation = SuccessDto.class))),
            @ApiResponse(responseCode = "400",
                    description = "리뷰 등록 실패",
                    content = @Content(schema = @Schema(implementation = ErrorDto.class)))
    })
    @PostMapping("/add")
    public ResponseEntity<SuccessDto> addReview(@RequestBody ReviewAddRequestDto reviewAddRequestDto) throws CustomException {

        reviewService.addReview(reviewAddRequestDto);

        return ResponseEntity.status(HttpStatus.OK).body(SuccessDto.builder().message("성공").build());
    }


    /**
     * 수정
     */
    @ApiResponses({
            @ApiResponse(responseCode = "200",
                    description = "리뷰 수정 성공",
                    content = @Content(schema = @Schema(implementation = ReviewUpdateResponseDto.class))),
            @ApiResponse(responseCode = "400",
                    description = "리뷰 수정 실패",
                    content = @Content(schema = @Schema(implementation = ErrorDto.class)))
    })
    @PutMapping
    public ResponseEntity<ReviewUpdateResponseDto> updateReview(@RequestBody ReviewUpdateRequestDto reviewUpdateRequestDto) throws CustomException {
        return ResponseEntity.status(HttpStatus.OK).body(reviewService.updateReview(reviewUpdateRequestDto));
    }

    /**
     * 삭제
     */
    @ApiResponses({
            @ApiResponse(responseCode = "200",
                    description = "리뷰 삭제 성공",
                    content = @Content(schema = @Schema(implementation = SuccessDto.class))),
            @ApiResponse(responseCode = "400",
                    description = "리뷰 삭제 실패",
                    content = @Content(schema = @Schema(implementation = ErrorDto.class)))
    })
    @DeleteMapping("/{reviewId}")
    public ResponseEntity<SuccessDto> deleteReview(@PathVariable int reviewId) throws CustomException {
        reviewService.deleteReview(reviewId);
        return ResponseEntity.status(HttpStatus.OK).body(SuccessDto.builder().message("성공").build());
    }

    /**
     * 사용자가 조회
     */
    @ApiResponses({
            @ApiResponse(responseCode = "200",
                    description = "사용자가 자기가 작성한 리뷰 보기",
                    content = @Content(schema = @Schema(implementation = ReviewGetResponseDto.class))),
            @ApiResponse(responseCode = "400",
                    description = "리뷰 조회 실패",
                    content = @Content(schema = @Schema(implementation = ErrorDto.class)))
    })
    @PostMapping("/user/get")
    public ResponseEntity<ReviewGetResponseDto> getReviewsByUserId(@RequestBody ReviewGetRequestUserId reviewGetRequestUserId) throws CustomException {



        return ResponseEntity.status(HttpStatus.OK).body(reviewService.getReviewsByUserId(reviewGetRequestUserId.getUserId()));
    }

    /**
     * 서비스 제공자 별로 리뷰 조회
     */
    @ApiResponses({
            @ApiResponse(responseCode = "200",
                    description = "서비스 제공자 별로 리뷰 조회",
                    content = @Content(schema = @Schema(implementation = ReviewGetResponseDto.class))),
            @ApiResponse(responseCode = "400",
                    description = "리뷰 조회 실패",
                    content = @Content(schema = @Schema(implementation = ErrorDto.class)))
    })
    @GetMapping("/provider/get/{providerId}")
    public ResponseEntity<ReviewGetResponseDto> getReviewsByProviderId(@PathVariable int providerId) throws CustomException {
        return ResponseEntity.status(HttpStatus.OK).body(reviewService.getReviewsByProviderId(providerId));
    }

    /**
     * 리뷰 답글 등록
     */
    @ApiResponses({
            @ApiResponse(responseCode = "200",
                    description = "리뷰 답글 등록",
                    content = @Content(schema = @Schema(implementation = SuccessDto.class))),
            @ApiResponse(responseCode = "400",
                    description = "리뷰 답글 등록 실패",
                    content = @Content(schema = @Schema(implementation = ErrorDto.class)))
    })
    @PostMapping("/comment")
    public ResponseEntity<SuccessDto> addComment(@RequestBody CommentAddRequestDto commentAddRequestDto) throws CustomException{

        reviewService.addComment(commentAddRequestDto);

        return ResponseEntity.status(HttpStatus.OK).body(SuccessDto.builder().message("성공").build());
    }

    /**
     * 리뷰 답글 수정
     */
    @ApiResponses({
            @ApiResponse(responseCode = "200",
                    description = "리뷰 답글 수정",
                    content = @Content(schema = @Schema(implementation = SuccessDto.class))),
            @ApiResponse(responseCode = "400",
                    description = "리뷰 답글 수정 실패",
                    content = @Content(schema = @Schema(implementation = ErrorDto.class)))
    })
    @PutMapping("/comment")
    public ResponseEntity<SuccessDto> updateComment(@RequestBody CommentUpdateRequestDto commentUpdateRequestDto) throws CustomException{
        reviewService.updateComment(commentUpdateRequestDto);

        return ResponseEntity.status(HttpStatus.OK).body(SuccessDto.builder().message("성공").build());
    }

    /**
     * 리뷰 답글 삭제
     */
    @ApiResponses({
            @ApiResponse(responseCode = "200",
                    description = "리뷰 답글 삭제",
                    content = @Content(schema = @Schema(implementation = SuccessDto.class))),
            @ApiResponse(responseCode = "400",
                    description = "리뷰 답글 삭제 실패",
                    content = @Content(schema = @Schema(implementation = ErrorDto.class)))
    })
    @DeleteMapping("/comment/delete/{reviewId}")
    public ResponseEntity<SuccessDto> deleteComment(@PathVariable int reviewId) throws CustomException{
        reviewService.deleteComment(reviewId);
        return ResponseEntity.status(HttpStatus.OK).body(SuccessDto.builder().message("성공").build());
    }

    /**
     * 예외 핸들러
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
