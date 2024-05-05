package com.plog.demo.repository;

import com.plog.demo.model.ProviderCheckTable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProviderCheckTableRepository extends JpaRepository<ProviderCheckTable, Integer> {
}
