package com.example.vqshki.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Entity
@Table
@Data
@NoArgsConstructor
@AllArgsConstructor
public class BaseStation {
    @Id
    @SequenceGenerator(name = "base_station_sequence", sequenceName = "base_station_sequence", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.UUID, generator = "UUID")
    @Column(name = "base_station_id")
    private UUID baseStationId;
    @Column(name = "coordinate_x")
    private Double coordinateX;
    @Column(name = "coordinate_y")
    private Double coordinateY;
    @Column(name = "name")
    private String name;
    @Column(name = "detection_radius")
    private Double detectionRadius;
}
