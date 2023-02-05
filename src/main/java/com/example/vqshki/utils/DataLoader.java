package com.example.vqshki.utils;

import com.example.vqshki.models.BaseStation;
import com.example.vqshki.models.MobileStation;
import com.example.vqshki.repository.BaseStationRepository;
import com.example.vqshki.repository.MobileStationRepository;
import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.UUID;

@Component
public class DataLoader {

    //TODO: find the way to keep them same
    UUID BASE_STATION_A_UUID = UUID.fromString("32c6b0cd-2ada-49c1-8715-eca43b673474");
    UUID BASE_STATION_A1_UUID = UUID.fromString("0631d7f7-693a-4d45-87d4-27334c6b0bf3");
    UUID BASE_STATION_B_UUID = UUID.fromString("69ac855e-de8d-45db-9423-b2c835908daa");
    UUID MOBILE_STATION_Z_UUID = UUID.fromString("343b24bf-73e5-4e30-a437-9c83de708f2a");

    private final BaseStationRepository baseStationRepository;
    private final MobileStationRepository mobileStationRepository;

    public DataLoader(BaseStationRepository baseStationRepository, MobileStationRepository mobileStationRepository) {
        this.baseStationRepository = baseStationRepository;
        this.mobileStationRepository = mobileStationRepository;
    }

    @PostConstruct
    public void loadData() {
        baseStationRepository.saveAll(List.of(
                new BaseStation(
                        BASE_STATION_A_UUID,
                        11,
                        11,
                        "stationA",
                        6
                ),
                new BaseStation(
                        BASE_STATION_A1_UUID,
                        18.83,
                        7.44,
                        "stationA1",
                        6
                ),
                new BaseStation(
                        BASE_STATION_B_UUID,
                        18,
                        16,
                        "stationB",
                        6
                )
        ));
        mobileStationRepository.save(
                new MobileStation(
                        MOBILE_STATION_Z_UUID,
                        29,
                        6,
                        0
                ));
    }
}
