package com.plog.demo.service.review;

import com.plog.demo.dto.review.*;
import com.plog.demo.dto.review.comment.CommentAddRequestDto;
import com.plog.demo.dto.review.comment.CommentUpdateRequestDto;
import com.plog.demo.exception.CustomException;

public interface ReviewService {

    void canWriteReview(ReviewCheckWriteRequestDto reviewCheckWriteRequestDto) throws CustomException;

    void addReview(ReviewAddRequestDto reviewAddRequestDto) throws CustomException;

    ReviewUpdateResponseDto updateReview(ReviewUpdateRequestDto reviewUpdateRequestDto) throws CustomException;

    void deleteReview(int reviewId) throws CustomException;

    ReviewGetResponseDto getReviewsByUserId(String userId) throws CustomException;

    ReviewGetResponseDto getReviewsByProviderId(int providerId) throws CustomException;

    void addComment(CommentAddRequestDto commentAddRequestDto) throws CustomException;

    void updateComment(CommentUpdateRequestDto commentUpdateRequestDto) throws CustomException;

    void deleteComment(int reviewId) throws CustomException;


}
