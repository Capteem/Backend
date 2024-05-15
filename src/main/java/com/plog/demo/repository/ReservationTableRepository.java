package com.plog.demo.repository;

import com.plog.demo.dto.reservation.ReservationResponseDto;
import com.plog.demo.model.IdTable;
import com.plog.demo.model.ReservationTable;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import com.plog.demo.model.ProviderTable;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface ReservationTableRepository extends JpaRepository<ReservationTable, Integer> {


    List<ReservationTable> findByUserId(IdTable userId);


    /**
     * 예약 날짜 겹치는 경우
     */
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("SELECT r FROM ReservationTable r WHERE (r.reservation_camera = :cameraId " +
            "OR r.reservation_hair = :hairId " +
            "OR r.reservation_studio = :studioId) " +
            "AND (r.reservation_start_date < :endDate AND r.reservation_end_date > :startDate)")
    List<ReservationTable> findReservationTableWithLock(
            @Param("cameraId") int cameraId,
            @Param("hairId") int hairId,
            @Param("studioId") int studioId,
            @Param("startDate") LocalDateTime startDate,
            @Param("endDate") LocalDateTime endDate
            );

    List<ReservationTable> findAllByUserId(IdTable idTable);
    List<ReservationTable> findAllByProviderId(ProviderTable providerId);

}

