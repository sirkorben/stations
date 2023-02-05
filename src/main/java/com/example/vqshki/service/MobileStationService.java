package com.example.vqshki.service;

import com.example.vqshki.models.MobileStation;
import com.example.vqshki.repository.MobileStationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class MobileStationService {
    private final MobileStationRepository mobileStationRepository;

    @Autowired
    public MobileStationService(MobileStationRepository mobileStationRepository) {
        this.mobileStationRepository = mobileStationRepository;
    }

    public Optional<MobileStation> getMobileStationInfo(UUID uuid) {
        return mobileStationRepository.findById(uuid);
    }

    //TODO: delete later when it is not needed anymore
    public List<MobileStation> findAllMobileStations(){
        return mobileStationRepository.findAll();
    }
}
