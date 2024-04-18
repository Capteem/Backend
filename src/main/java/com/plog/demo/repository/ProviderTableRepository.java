package com.plog.demo.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.plog.demo.model.ProviderTable;

@Repository
public interface ProviderTableRepository extends JpaRepository<ProviderTable, Integer> {
}
