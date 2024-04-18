package com.plog.demo.repository;

import com.plog.demo.model.ReservationTable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ReservationTableRepository extends JpaRepository<ReservationTable, Integer> {

}
