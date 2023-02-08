package com.example.vqshki.utils;

import com.example.vqshki.models.Report;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;
import java.util.UUID;

@Getter
@AllArgsConstructor
public class BaseStationRequestMessage {
    @JsonProperty("base_station_id")
    private UUID baseStationId;
    @JsonProperty("reports")
    private List<Report> reports;
}
