package com.plog.demo.repository;

import com.plog.demo.model.ReservationTable;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface ReservationTableRepository extends JpaRepository<ReservationTable, Integer> {


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
}

