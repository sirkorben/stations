package com.example.vqshki.service;

import com.example.vqshki.models.MobileStation;
import com.example.vqshki.repository.MobileStationRepository;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class MobileStationServiceTest {
    private final MobileStationRepository mobileStationRepository = mock(MobileStationRepository.class);
    MobileStationService mobileStationService = new MobileStationService(mobileStationRepository);

    @Test
    public void getMobileStationById() {

        UUID uuid = UUID.randomUUID();
        MobileStation expectedMobileStation = new MobileStation();
        expectedMobileStation.setMobileStationId(uuid);

        when(mobileStationRepository.findMobileStationById(uuid)).thenReturn(expectedMobileStation);

        MobileStation actualMobileStation = mobileStationService.getMobileStationInfo(uuid);

        assertEquals(expectedMobileStation, actualMobileStation);
    }
}
