package com.plog.demo.repository;

import com.plog.demo.model.ProviderTable;
import com.plog.demo.model.WorkdateTable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface WorkdateTableRepository extends JpaRepository<WorkdateTable, Long> {

    List<WorkdateTable> findByProviderId(ProviderTable providerId);
}
