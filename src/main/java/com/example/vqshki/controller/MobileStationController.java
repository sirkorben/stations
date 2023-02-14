package com.example.vqshki.controller;

import com.example.vqshki.dto.MobileStationDTO;
import com.example.vqshki.mappers.MobileStationMapper;
import com.example.vqshki.models.MobileStation;
import com.example.vqshki.service.MobileStationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping(path = "/api/v1/location")
public class MobileStationController {
    private final MobileStationService mobileStationService;
    private MobileStationMapper mobileStationMapper;

    public MobileStationController(MobileStationService mobileStationService, MobileStationMapper mobileStationMapper) {
        this.mobileStationMapper = mobileStationMapper;
        this.mobileStationService = mobileStationService;
    }

    @GetMapping(path = "/{uuid}")
    public ResponseEntity<MobileStationDTO> getMobileStationInfo(@PathVariable("uuid") UUID msUuid) {
        MobileStation ms = mobileStationService.getMobileStationInfo(msUuid);
        return Optional.ofNullable(mobileStationMapper.mobileStationToDto(ms))
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
}
