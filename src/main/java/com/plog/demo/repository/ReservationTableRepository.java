package com.plog.demo.repository;

import com.plog.demo.model.ReservationTable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReservationTableRepository extends JpaRepository<ReservationTable, Integer> {

}
