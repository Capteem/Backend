package com.plog.demo.repository;

import com.plog.demo.model.PortfolioTable;
import com.plog.demo.model.ProviderTable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PortfolioTableRepository extends JpaRepository<PortfolioTable, Integer> {

    List<PortfolioTable> findByProviderId(ProviderTable providerId);

    @Query(value = "SELECT * FROM portfolio_table ORDER BY RAND() LIMIT :limit OFFSET :offset", nativeQuery = true)
    List<PortfolioTable> findRandomPortfolio(@Param("limit") int limit, @Param("offset") int offset);
}
