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
public class MobileStation {
    @Id
    @SequenceGenerator(
            name = "mobile_station_sequence",
            sequenceName = "mobile_station_sequence",
            allocationSize = 1
    )
    @GeneratedValue(
            strategy = GenerationType.UUID,
            generator = "UUID"
    )
    private UUID mobileStationId;
    private double lastKnownX;
    private double lastKnownY;
    private double errorRadius = 0;

}
