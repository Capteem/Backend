package com.plog.demo.service.review;

import com.plog.demo.common.ReservationStatus;
import com.plog.demo.dto.review.*;
import com.plog.demo.dto.review.comment.CommentAddRequestDto;
import com.plog.demo.dto.review.comment.CommentGetDto;
import com.plog.demo.dto.review.comment.CommentUpdateRequestDto;
import com.plog.demo.exception.CustomException;
import com.plog.demo.model.CommentTable;
import com.plog.demo.model.ProviderTable;
import com.plog.demo.model.ReservationTable;
import com.plog.demo.model.ReviewTable;
import com.plog.demo.repository.CommentTableRepository;
import com.plog.demo.repository.ProviderTableRepository;
import com.plog.demo.repository.ReservationTableRepository;
import com.plog.demo.repository.ReviewTableRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Slf4j
@RequiredArgsConstructor
@Service
@Transactional
public class ReviewServiceImpl implements ReviewService{

    private final ProviderTableRepository providerTableRepository;
    private final ReviewTableRepository reviewTableRepository;
    private final CommentTableRepository commentTableRepository;
    private final ReservationTableRepository reservationTableRepository;


    @Override
    public void canWriteReview(ReviewCheckWriteRequestDto reviewCheckWriteRequestDto) throws CustomException {

        ReservationTable reservation = reservationTableRepository.findById(reviewCheckWriteRequestDto.getReservationId())
                .orElseThrow(() -> new CustomException("존재하지 않은 예약입니다.", HttpStatus.NOT_FOUND.value()));

        if(isReservationNotCompleted(reservation)){
            throw new CustomException("예약이 완료 되지않았습니다.", HttpStatus.FORBIDDEN.value());
        }

        ProviderTable provider = providerTableRepository.findById(reviewCheckWriteRequestDto.getProviderId())
                .orElseThrow(() -> new CustomException("존재하지 않은 서비스입니다.", HttpStatus.NOT_FOUND.value()));

        Optional<ReviewTable> review = reviewTableRepository.findByUserIdAndAndProviderIdAndReservationId(
                reviewCheckWriteRequestDto.getUserId(),
                provider,
                reservation);
        if(review.isPresent()){
            throw new CustomException("이미 리뷰를 작성했습니다.", HttpStatus.CONFLICT.value());
        }
    }

    @Override
    public void addReview(ReviewAddRequestDto reviewAddRequestDto) throws CustomException {

        ReservationTable reservation = reservationTableRepository.findById(reviewAddRequestDto.getReservationId())
                .orElseThrow(() -> new CustomException("존재하지 않은 예약입니다.", HttpStatus.NOT_FOUND.value()));

        ProviderTable provider = providerTableRepository.findById(reviewAddRequestDto.getProviderId())
                .orElseThrow(() -> new CustomException("존재하지 않은 서비스입니다.", HttpStatus.NOT_FOUND.value()));



        //리뷰에 저장
        ReviewTable review = ReviewTable.builder()
                .providerId(provider)
                .reviewContent(reviewAddRequestDto.getReviewContent())
                .reviewScore(reviewAddRequestDto.getReviewScore())
                .reviewDate(reviewAddRequestDto.getReviewDate())
                .userId(reviewAddRequestDto.getUserId())
                .userNickName(reviewAddRequestDto.getUserNickName())
                .reservationId(reservation)
                .build();
        try {
            reviewTableRepository.save(review);
        }catch (Exception e){
            throw new RuntimeException("리뷰 저장 중 오류 발생", e);
        }

    }

    @Override
    public ReviewUpdateResponseDto updateReview(ReviewUpdateRequestDto reviewUpdateRequestDto) throws CustomException {

        ReviewTable review = reviewTableRepository.findById(reviewUpdateRequestDto.getReviewId())
                .orElseThrow(() -> new CustomException("존재하지 않는 리뷰입니다.", HttpStatus.NOT_FOUND.value()));


        review.setReviewContent(reviewUpdateRequestDto.getReviewContent());
        review.setReviewScore(reviewUpdateRequestDto.getReviewScore());
        review.setReviewDate(reviewUpdateRequestDto.getReviewDate());
        review.setUserId(reviewUpdateRequestDto.getUserId());
        review.setUserNickName(reviewUpdateRequestDto.getUserNickName());

        return ReviewUpdateResponseDto.builder()
                .reviewId(review.getReviewId())
                .reviewContent(review.getReviewContent())
                .reviewScore(review.getReviewScore())
                .reviewDate(review.getReviewDate())
                .userId(review.getUserId())
                .userNickName(review.getUserNickName())
                .providerId(review.getProviderId().getProviderId())
                .build();
    }

    @Override
    public void deleteReview(int reviewId) throws CustomException {

        //review.setStatus(CommonStatus.DELETE.getCode());

        //임시로
        try {
            reviewTableRepository.deleteById(reviewId);
        }catch (Exception e){
            log.error("[deleteReview] 제거중 오류 발생");
            throw new RuntimeException("데이터 베이스 오류", e);
        }
    }

    @Override
    public ReviewGetResponseDto getReviewsByUserId(String userId) throws CustomException {

        List<ReviewTable> reviews = reviewTableRepository.findByUserId(userId);


        if(reviews.isEmpty()){
            throw new CustomException("리뷰가 존재하지 않습니다.", HttpStatus.NOT_FOUND.value());
        }

        List<ReviewGetDto> reviewGetDtos = reviews.stream()
                .map(review -> {
                    CommentGetDto commentGetDto = createCommentGetDto(review);

                    ReviewGetDto reviewGetDto = createReviewGetDto(review, commentGetDto);

                    reviewGetDto.setProviderName(review.getProviderId().getProviderName());

                    return reviewGetDto;

                }).toList();

        return ReviewGetResponseDto.builder()
                .reviewList(reviewGetDtos)
                .build();
    }

    @Override
    public ReviewGetResponseDto getReviewsByProviderId(int providerId) throws CustomException {

        ProviderTable provider = providerTableRepository.findById(providerId)
                .orElseThrow(() -> new CustomException("존재하지 않는서비스 제공자 입니다.", HttpStatus.NOT_FOUND.value()));


        List<ReviewTable> reviews = reviewTableRepository.findByProviderId(provider);

        List<ReviewGetDto> reviewGetDtos = new ArrayList<>();

        if(reviews.isEmpty()){

            reviewGetDtos = null;
        }else {

            for(ReviewTable review : reviews){
                CommentGetDto commentGetDto = createCommentGetDto(review);
                ReviewGetDto reviewGetDto = createReviewGetDto(review, commentGetDto);
                reviewGetDtos.add(reviewGetDto);
            }

        }

        return ReviewGetResponseDto.builder()
                .reviewList(reviewGetDtos)
                .build();
    }

    @Override
    public void addComment(CommentAddRequestDto commentAddRequestDto) throws CustomException {
        ReviewTable review = reviewTableRepository.findById(commentAddRequestDto.getReviewId())
                .orElseThrow(() -> new CustomException("존재하지 않는 리뷰입니다.", HttpStatus.NOT_FOUND.value()));

        if(review.getCommentId() != null){
            throw new CustomException("이미 답글이 존재합니다.", HttpStatus.BAD_REQUEST.value());
        }

        try {
            CommentTable savedComment = commentTableRepository.save(CommentTable.builder()
                    .CommentContent(commentAddRequestDto.getCommentContent())
                    .CommentDate(commentAddRequestDto.getCommentDate())
                    .build());
            review.setCommentId(savedComment);
        }catch (Exception e){
            log.error("[addComment] 데이터 베이스 저장 중 오류");
            throw new RuntimeException("데이터 베이스 저장 중 오류", e);
        }

    }

    @Override
    public void updateComment(CommentUpdateRequestDto commentUpdateRequestDto) throws CustomException {

        CommentTable comment = commentTableRepository.findById(commentUpdateRequestDto.getCommentId())
                .orElseThrow(() -> new CustomException("존재하지 않는 리뷰 답글입니다.", HttpStatus.NOT_FOUND.value()));

        comment.setCommentContent(commentUpdateRequestDto.getCommentContent());
        comment.setCommentDate(commentUpdateRequestDto.getCommentDate());

    }

    @Override
    public void deleteComment(int reviewId) throws CustomException {

        ReviewTable review = reviewTableRepository.findById(reviewId)
                .orElseThrow(() -> new CustomException("존재하지 않는 리뷰입니다.", HttpStatus.NOT_FOUND.value()));

        int commentId = review.getCommentId().getCommentId();

        try {
            review.setCommentId(null);
            commentTableRepository.deleteById(commentId);
        }catch (Exception e){
            log.error("[deleteComment] 데이터 삭제 중 에러 발생");
            throw new RuntimeException("[deleteComment] 데이터 삭제 중 에러 발생", e);
        }
    }

    private ReviewGetDto createReviewGetDto(ReviewTable review, CommentGetDto commentGetDto) {
        return ReviewGetDto.builder()
                .reviewId(review.getReviewId())
                .reviewContent(review.getReviewContent())
                .reviewScore(review.getReviewScore())
                .reviewDate(review.getReviewDate())
                .userId(review.getUserId())
                .userNickName(review.getUserNickName())
                .comment(commentGetDto)
                .build();
    }

    private CommentGetDto createCommentGetDto(ReviewTable review) {

        if(review.getCommentId() == null){
            return null;
        }

        return CommentGetDto.builder()
                .commentId(review.getCommentId().getCommentId())
                .commentContent(review.getCommentId().getCommentContent())
                .commentDate(review.getCommentId().getCommentDate())
                .build();
    }

    private boolean isReservationNotCompleted(ReservationTable reservation) {
        return reservation.getStatus() != ReservationStatus.COMPLETED.getCode();
    }
}
