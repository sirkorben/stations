package com.example.vqshki.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;

import java.util.List;
import java.util.UUID;

// TODO: find better place for this class
public class BaseStationReport {
    @JsonProperty("base_station_id")
    @Getter
    private UUID baseStationId;
    @JsonProperty("reports")
    @Getter
    private List<Report> reports;

}
