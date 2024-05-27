package com.plog.demo.repository;

import com.plog.demo.dto.workdate.DateListDto;
import com.plog.demo.model.ProviderTable;
import com.plog.demo.model.WorkdateTable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDateTime;
import java.util.List;

public interface WorkdateTableRepository extends JpaRepository<WorkdateTable, Long> {

    List<WorkdateTable> findByProviderId(ProviderTable providerId);
    WorkdateTable findByProviderIdAndWorkDate(ProviderTable providerId, LocalDateTime workDate);

    @Query("delete from WorkdateTable w where w.providerId = :providerId AND w.workDate >= :workStart AND w.workDate <= :workEnd")
    void deleteByProviderIdAndWorkTime(ProviderTable providerId, String workStart, String workEnd);

}
