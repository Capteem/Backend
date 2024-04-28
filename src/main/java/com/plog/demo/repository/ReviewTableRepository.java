package com.plog.demo.repository;

import com.plog.demo.model.ReviewTable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ReviewTableRepository extends JpaRepository<ReviewTable, Integer> {

    /**
     * ProviderId로 조회
     */
    List<ReviewTable> findByProviderTable_ProviderId(int providerId);
}
