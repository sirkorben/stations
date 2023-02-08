package com.example.vqshki.repository;

import com.example.vqshki.models.Report;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.sql.Timestamp;
import java.util.List;
import java.util.UUID;

@Repository
public interface ReportRepository extends JpaRepository<Report, Long> {
    @Query(value = "SELECT DISTINCT r.baseStationId FROM Report r WHERE r.mobileStationId = ?1 AND r.timeDetected >= ?2")
    List<UUID> getReportsInTimeWindow(UUID msId, Timestamp timeWindow);

    @Query(value = "SELECT r FROM Report r WHERE r.baseStationId = ?1 AND r.mobileStationId = ?2 ORDER BY r.timeDetected DESC LIMIT 1")
    Report getLatestReportByBsId(UUID bsId, UUID msId);

    @Query(value = "SELECT DISTINCT r.mobileStationId FROM Report r WHERE r.timeDetected >= ?1")
    List<UUID> getLatestReportedMobileStationIds(Timestamp timeMinusGap);

    @Query(value = "SELECT r.timeDetected FROM Report r WHERE r.mobileStationId = ?1 ORDER BY r.timeDetected DESC LIMIT 1")
    Timestamp getLatestTimeDetectedByMobileStationId(UUID mobileStationId);
}

