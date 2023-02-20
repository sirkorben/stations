package com.example.vqshki.repository;

import com.example.vqshki.models.Report;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.sql.Timestamp;
import java.util.List;
import java.util.UUID;

@Repository
public interface ReportRepository extends JpaRepository<Report, Long> {
    @Query(value = "SELECT DISTINCT r.mobileStationId FROM Report r WHERE r.timeDetected BETWEEN :timeInPast AND :timeNow")
    List<UUID> getLatestReportedMobileStationIdsBetweenTime(@Param("timeInPast")Timestamp timeInPast, @Param("timeNow")Timestamp timeNow);

    @Query(value = "SELECT r.timeDetected FROM Report r WHERE r.mobileStationId = :msId ORDER BY r.timeDetected DESC LIMIT 1")
    Timestamp getLatestTimeDetectedByMobileStationId(@Param("msId")UUID msId);

    @Query("SELECT  DISTINCT r.baseStationId FROM Report r WHERE r.mobileStationId = :msId AND r.timeDetected BETWEEN :timeInPast AND :timeNow")
    List<UUID> getCoincidenceBaseStationIds(@Param("msId")UUID msId, @Param("timeInPast")Timestamp timeInPast, @Param("timeNow")Timestamp timeNow);

    @Query(value = "SELECT r FROM Report r WHERE r.baseStationId = :bsId AND r.mobileStationId = :msId ORDER BY r.timeDetected DESC LIMIT 1")
    Report getLatestReportByBsId(@Param("bsId")UUID bsId, @Param("msId")UUID msId);
}
