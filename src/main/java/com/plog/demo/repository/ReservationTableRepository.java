package com.plog.demo.repository;

import com.plog.demo.model.ProviderTable;
import com.plog.demo.model.ReservationTable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ReservationTableRepository extends JpaRepository<ReservationTable, Integer> {
}
