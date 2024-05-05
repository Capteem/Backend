package com.plog.demo.repository;

import com.plog.demo.model.ProviderTable;
import com.plog.demo.model.ReviewTable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ReviewTableRepository extends JpaRepository<ReviewTable, Integer> {

    /**
     * ProviderId로 조회
     */
    List<ReviewTable> findByProviderId(ProviderTable providerTable);
}
