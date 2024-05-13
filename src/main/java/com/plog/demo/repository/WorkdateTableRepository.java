package com.plog.demo.repository;

import com.plog.demo.model.WorkdateTable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface WorkdateTableRepository extends JpaRepository<WorkdateTable, Long> {
}
