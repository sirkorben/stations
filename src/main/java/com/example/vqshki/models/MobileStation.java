package com.example.vqshki.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
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
//@JsonInclude(JsonInclude.Include.NON_NULL) // TODO: to verify how relevant it is
public class MobileStation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @JsonIgnore
    private Long id;
    private UUID mobileStationId;
    private Double lastKnownX;
    private Double lastKnownY;
    private Double errorRadius;
    private Integer errorCode;
    private String errorMsg;
    @JsonIgnore
    private Timestamp timestamp;
}
