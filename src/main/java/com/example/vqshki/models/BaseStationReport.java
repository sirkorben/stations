package com.example.vqshki.models;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;
import java.util.UUID;

// TODO: find better place for this class
public class BaseStationReport {
    @JsonProperty("base_station_id")
    private UUID baseStationId;
    @JsonProperty("reports")
    private List<Report> reports;

    public UUID getBaseStationId() {
        return baseStationId;
    }
    public void setBaseStationId(UUID baseStationId) {
        this.baseStationId = baseStationId;
    }

    public List<Report> getReports() {
        return reports;
    }

    public void setReports(List<Report> reports) {
        this.reports = reports;
    }

}
