package com.plog.demo.repository;

import com.plog.demo.dto.workdate.DateListDto;
import com.plog.demo.model.ProviderTable;
import com.plog.demo.model.ReservationTimeTable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ReservationTimeTableRepository extends JpaRepository<ReservationTimeTable, Integer>{

    List<ReservationTimeTable> findByProviderId(ProviderTable providerId);
}
