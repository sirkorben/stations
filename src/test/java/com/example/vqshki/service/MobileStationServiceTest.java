package com.example.vqshki.service;

import com.example.vqshki.repository.MobileStationRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static com.example.vqshki.constants.TestConstants.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class MobileStationServiceTest {
    @Mock
    private MobileStationRepository mockedMobileStationRepository;
    @Test
    public void shouldReturnMobileStationInfo() {
        when(mockedMobileStationRepository.findMobileStationById(TEST_MOBILE_STATION_Z_ID)).thenReturn(TEST_MOBILE_STATION_Z);

        MobileStationService mobileStationService = new MobileStationService(mockedMobileStationRepository);

        assertEquals(TEST_MOBILE_STATION_Z, mobileStationService.getMobileStationInfo(TEST_MOBILE_STATION_Z_ID));
    }
}
