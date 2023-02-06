package com.example.vqshki.controller;

import com.example.vqshki.models.MobileStation;
import com.example.vqshki.service.MobileStationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping(path = "api/v1/location")
public class MobileStationController {
    private final MobileStationService mobileStationService;

    @Autowired
    public MobileStationController(MobileStationService mobileStationService) {
        this.mobileStationService = mobileStationService;
    }

    @GetMapping(path = "{uuid}")
    public ResponseEntity<Optional<MobileStation>> getMobileStationInfo(@PathVariable("uuid") UUID msUuid) {
        return Optional.ofNullable(mobileStationService.getMobileStationInfo(msUuid))
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

}
