package com.plog.demo.repository;

import com.plog.demo.model.IdTable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IdTableRepository extends JpaRepository<IdTable, String> {
}
