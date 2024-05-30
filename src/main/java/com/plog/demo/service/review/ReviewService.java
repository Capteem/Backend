package com.plog.demo.service.review;

import com.plog.demo.dto.review.ReviewAddRequestDto;
import com.plog.demo.dto.review.ReviewGetResponseDto;
import com.plog.demo.dto.review.ReviewUpdateRequestDto;
import com.plog.demo.dto.review.ReviewUpdateResponseDto;
import com.plog.demo.dto.review.comment.CommentAddRequestDto;
import com.plog.demo.dto.review.comment.CommentUpdateRequestDto;
import com.plog.demo.exception.CustomException;

public interface ReviewService {

    /**
     * 등록
     */
    void addReview(ReviewAddRequestDto reviewAddRequestDto) throws CustomException;

    /**
     * 수정
     */
    ReviewUpdateResponseDto updateReview(ReviewUpdateRequestDto reviewUpdateRequestDto) throws CustomException;

    /**
     * 삭제
     */
    void deleteReview(int reviewId) throws CustomException;

    /**
     * 사용자가 자신이 작성한 리뷰 조회 (답글 있으면 포함)
     */
    ReviewGetResponseDto getReviewsByUserId(String userId) throws CustomException;

    /**
     * 서비스 제공자 리뷰 조회 (답글 있으면 포함)
     */
    ReviewGetResponseDto getReviewsByProviderId(int providerId) throws CustomException;

    /**
     * 리뷰 답글
     */
    void addComment(CommentAddRequestDto commentAddRequestDto) throws CustomException;

    /**
     * 리뷰 답글 수정
     */
    void updateComment(CommentUpdateRequestDto commentUpdateRequestDto) throws CustomException;

    /**
     * 리뷰 답글 삭제
     */
    void deleteComment(int reviewId) throws CustomException;


}
