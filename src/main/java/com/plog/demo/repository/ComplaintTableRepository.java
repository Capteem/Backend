package com.plog.demo.repository;

import com.plog.demo.model.ComplaintTable;
import com.plog.demo.model.IdTable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ComplaintTableRepository extends JpaRepository<ComplaintTable, Integer> {
    List<ComplaintTable> findAllByUserId(IdTable IdTable);
}
