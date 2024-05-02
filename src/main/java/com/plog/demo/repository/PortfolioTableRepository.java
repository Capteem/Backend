package com.plog.demo.repository;

import com.plog.demo.model.PortfolioTable;
import com.plog.demo.model.ProviderTable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PortfolioTableRepository extends JpaRepository<PortfolioTable, Integer> {

    List<PortfolioTable> findByProviderId(ProviderTable providerId);
}
