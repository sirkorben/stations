package com.example.vqshki.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;
import java.util.UUID;

@Entity
@Table
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MobileStation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;
    @Column(name = "mobile_station_id")
    private UUID mobileStationId;
    @Column(name = "last_known_x")
    private Double lastKnownX;
    @Column(name = "last_known_y")
    private Double lastKnownY;
    @Column(name = "error_radius")
    private Double errorRadius;
    @Column(name = "error_code")
    private Integer errorCode;
    @Column(name = "error_msg")
    private String errorMsg;
    @Column(name = "timestamp")
    private Timestamp timestamp;
}
