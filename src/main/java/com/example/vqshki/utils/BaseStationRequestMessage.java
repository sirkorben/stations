package com.example.vqshki.utils;

import com.example.vqshki.models.Report;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;

import java.util.List;
import java.util.UUID;

// TODO: find better place for this class
public class BaseStationRequestMessage {
    @JsonProperty("base_station_id")
    @Getter
    private UUID baseStationId;
    @JsonProperty("reports")
    @Getter
    private List<Report> reports;

}
