package com.example.vqshki.repository;

import com.example.vqshki.models.BaseStation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface BaseStationRepository extends JpaRepository<BaseStation, UUID> {

    @Query("SELECT bs FROM BaseStation bs WHERE bs.baseStationId = :bsId")
    BaseStation findByUuid(@Param("bsId")UUID bsId);

}
