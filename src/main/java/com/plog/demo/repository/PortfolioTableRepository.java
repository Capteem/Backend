package com.plog.demo.repository;

import com.plog.demo.model.PortfolioTable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PortfolioTableRepository extends JpaRepository<PortfolioTable, Integer> {
}
