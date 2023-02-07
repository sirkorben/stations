package com.example.vqshki.models;

import com.fasterxml.jackson.annotation.JsonInclude;
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
@JsonInclude(JsonInclude.Include.NON_NULL)
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
    private Double lastKnownX;
    private Double lastKnownY;
    private Double errorRadius = null;
    private Integer errorCode = null;
    private String errorMsg;

}
