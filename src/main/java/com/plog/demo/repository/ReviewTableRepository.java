package com.plog.demo.repository;

import com.plog.demo.model.CommentTable;
import com.plog.demo.model.ProviderTable;
import com.plog.demo.model.ReviewTable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ReviewTableRepository extends JpaRepository<ReviewTable, Integer> {

    /**
     * ProviderId로 조회
     */
//    @Query("SELECT r FROM ReviewTable r JOIN FETCH r.comment WHERE r.providerId = :providerId")
//    List<ReviewTable> findReviewTableWithCommentTableByProviderId(@Param("providerId") ProviderTable providerTable);

    List<ReviewTable> findByUserId(String userId);

    List<ReviewTable> findByProviderId(ProviderTable providerTable);

    Optional<ReviewTable> findByCommentId(CommentTable commentId);


}
