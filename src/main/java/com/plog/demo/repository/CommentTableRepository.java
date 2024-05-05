package com.plog.demo.repository;

import com.plog.demo.model.CommentTable;
import com.plog.demo.model.ReviewTable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CommentTableRepository extends JpaRepository<CommentTable, Integer> {

    Optional<CommentTable> findByReviewId(ReviewTable reviewTable);
}
