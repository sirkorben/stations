package com.example.vqshki.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;
import java.util.UUID;

@Entity
@Table
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Report {
    @Id
    @SequenceGenerator(name = "report_sequence", sequenceName = "report_sequence", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "report_sequence")
    @Column(name = "id")
    private Long id;
    @Column(name = "base_station_id")
    private UUID baseStationId;
    @Column(name = "mobile_station_id")
    @JsonProperty("mobile_station_id")
    private UUID mobileStationId;
    @Column(name = "distance")
    @JsonProperty("distance")
    private double distance;
    @Column(name = "time_detected")
    @JsonProperty("timestamp")
    private Timestamp timeDetected;
}
