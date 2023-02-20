package com.example.vqshki.dto;

import lombok.*;

import java.util.UUID;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MobileStationDTO {
    private UUID mobileStationId;
    private Double lastKnownX;
    private Double lastKnownY;
    private Double errorRadius;
    private Integer errorCode;
    private String errorMsg;
}
