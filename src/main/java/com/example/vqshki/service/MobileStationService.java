package com.example.vqshki.service;

import com.example.vqshki.models.MobileStation;
import com.example.vqshki.repository.MobileStationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class MobileStationService {
    private final MobileStationRepository mobileStationRepository;

    @Autowired
    public MobileStationService(MobileStationRepository mobileStationRepository) {
        this.mobileStationRepository = mobileStationRepository;
    }

    public MobileStation getMobileStationInfo(UUID uuid) {
        return mobileStationRepository.findMobileStationById(uuid);
    }
}
