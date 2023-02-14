package com.example.vqshki.controller;

import com.example.vqshki.service.BaseStationService;
import com.example.vqshki.utils.BaseStationRequestMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;

@RestController
@RequestMapping(path = "api/v1/report")
public class BaseStationController {
    private final BaseStationService baseStationService;

    public BaseStationController(BaseStationService baseStationService) {
        this.baseStationService = baseStationService;
    }

    @PostMapping
    public ResponseEntity<Void> createReport(@RequestBody BaseStationRequestMessage report) {
        baseStationService.saveReports(report);

        URI location = ServletUriComponentsBuilder.fromCurrentRequest().buildAndExpand(report.getBaseStationId()).toUri();

        return ResponseEntity.created(location).build();
    }
}
