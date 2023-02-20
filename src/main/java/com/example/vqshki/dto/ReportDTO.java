package com.example.vqshki.dto;


import lombok.*;

import java.sql.Timestamp;
import java.util.UUID;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ReportDTO {
    private UUID baseStationId;
    private UUID mobileStationId;
    private double distance;
    private Timestamp timeDetected;
}
