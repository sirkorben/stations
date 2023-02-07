package com.example.vqshki.repository;

import com.example.vqshki.models.MobileStation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface MobileStationRepository
        extends JpaRepository<MobileStation, Long> {

    @Query(value = "SELECT ms FROM MobileStation ms WHERE ms.mobileStationId = ?1 ORDER BY timestamp DESC LIMIT 1")
    Optional<MobileStation> findMobileStationByMobileStationId(UUID id);

}
