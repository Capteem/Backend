package com.plog.demo.repository;

import com.plog.demo.model.CommentTable;
import com.plog.demo.model.ProviderTable;
import com.plog.demo.model.ReservationTable;
import com.plog.demo.model.ReviewTable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ReviewTableRepository extends JpaRepository<ReviewTable, Integer> {


    List<ReviewTable> findByUserIdOrderByReviewIdDesc(String userId);

    List<ReviewTable> findByProviderIdOrderByReviewIdDesc(ProviderTable providerTable);

    Optional<ReviewTable> findByUserIdAndAndProviderIdAndReservationId(String userId,
                                                                       ProviderTable providerId,
                                                                       ReservationTable reservationId);

    List<ReviewTable> findAllByReservationId(ReservationTable reservationTable);

    Optional<ReviewTable> findByCommentId(CommentTable commentId);


}
