package com.plog.demo.repository;

import com.plog.demo.model.IdTable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface userRepository extends JpaRepository<IdTable, String> {
}
