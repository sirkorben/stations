package com.example.vqshki.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;

import java.sql.Timestamp;
import java.util.UUID;

@Entity
@Table
public class Report {
    @Id
    @SequenceGenerator(
            name = "report_sequence",
            sequenceName = "report_sequence",
            allocationSize = 1
    )
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "report_sequence"
    )
    private Long id;
    private UUID baseStationId;
    @JsonProperty("mobile_station_id")
    private UUID mobileStationId;
    @JsonProperty("distance")

    private double distance;
    @JsonProperty("timestamp")
    private Timestamp timeDetected;

    public Report() {
    }

    public Report(Long id, UUID baseStationId, UUID msId, double distance, Timestamp timeDetected) {
        this.id = id;
        this.baseStationId = baseStationId;
        this.mobileStationId = msId;
        this.distance = distance;
        this.timeDetected = timeDetected;
    }

    public Report(UUID baseStationId, UUID msId, double distance, Timestamp timeDetected) {
        this.baseStationId = baseStationId;
        this.mobileStationId = msId;
        this.distance = distance;
        this.timeDetected = timeDetected;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public UUID getBaseStationId() {
        return baseStationId;
    }

    public void setBaseStationId(UUID bsId) {
        this.baseStationId = bsId;
    }

    public UUID getMobileStationId() {
        return mobileStationId;
    }

    public void setMobileStationId(UUID msId) {
        this.mobileStationId = msId;
    }

    public double getDistance() {
        return distance;
    }

    public void setDistance(double distance) {
        this.distance = distance;
    }

    public Timestamp getTimeDetected() {
        return timeDetected;
    }

    public void setTimeDetected(Timestamp timeDetected) {
        this.timeDetected = timeDetected;
    }

    @Override
    public String toString() {
        return "Report{" +
                "id=" + id +
                ", bsId='" + baseStationId + '\'' +
                ", msId='" + mobileStationId + '\'' +
                ", distance=" + distance +
                ", timeDetected=" + timeDetected +
                '}';
    }
}
