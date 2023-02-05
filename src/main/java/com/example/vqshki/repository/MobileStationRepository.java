package com.example.vqshki.repository;

import com.example.vqshki.models.MobileStation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Repository
public interface MobileStationRepository
        extends JpaRepository<MobileStation, UUID> {
        @Transactional
        @Modifying(clearAutomatically = true)
        @Query(value = "UPDATE MobileStation ms SET ms.lastKnownX = ?2, ms.lastKnownY = ?3, ms.errorRadius = 0 WHERE ms.mobileStationId = ?1")
        void saveLastKnownPoint(UUID uuid, double x, double y);

        @Transactional
        @Modifying(clearAutomatically = true)
        @Query(value = "UPDATE MobileStation ms SET ms.lastKnownX = :lastKnownX, ms.lastKnownY = :lastKnownY, ms.errorRadius = :errorRadius WHERE ms.mobileStationId = :mobileStationId")
        void saveLastKnownPointKnownByTwoBS(@Param("mobileStationId") UUID mobileStationId, @Param("lastKnownX") double x, @Param("lastKnownY") double y, @Param("errorRadius") double errorRadius);

}
