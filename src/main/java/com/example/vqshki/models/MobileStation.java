package com.example.vqshki.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.sql.Date;
import java.sql.Timestamp;
import java.time.Instant;
import java.util.UUID;

@Entity
@Table
@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class MobileStation {
    @Id
    @SequenceGenerator(
            name = "mobile_station_sequence",
            sequenceName = "mobile_station_sequence",
            allocationSize = 1
    )
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "mobile_station_sequence"
    )
    @JsonIgnore
    private Long id;
    private UUID mobileStationId;
    private Double lastKnownX;
    private Double lastKnownY;
    private Double errorRadius = null;
    private Integer errorCode = null;
    private String errorMsg;

    @JsonIgnore
    private Timestamp timestamp;

    public MobileStation(UUID mobileStationId, Double lastKnownX, Double lastKnownY, Double errorRadius, Integer errorCode, String errorMsg, Timestamp timestamp) {
        this.mobileStationId = mobileStationId;
        this.lastKnownX = lastKnownX;
        this.lastKnownY = lastKnownY;
        this.errorRadius = errorRadius;
        this.errorCode = errorCode;
        this.errorMsg = errorMsg;
        this.timestamp = timestamp;
    }
}
