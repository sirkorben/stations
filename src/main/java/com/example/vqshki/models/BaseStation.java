package com.example.vqshki.models;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
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
    @SequenceGenerator(
            name = "base_station_sequence",
            sequenceName = "base_station_sequence",
            allocationSize = 1
    )
    @GeneratedValue(
            strategy = GenerationType.UUID,
            generator = "UUID"
    )
    private UUID baseStationId;
    private Double coordinateX;
    private Double coordinateY;
    private String name;
    private Double detectionRadius;
}
