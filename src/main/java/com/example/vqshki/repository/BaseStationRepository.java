package com.example.vqshki.repository;

import com.example.vqshki.models.BaseStation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface BaseStationRepository
        extends JpaRepository<BaseStation, UUID> {

}
