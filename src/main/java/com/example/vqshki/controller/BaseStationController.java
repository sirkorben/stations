package com.example.vqshki.controller;

import com.example.vqshki.models.BaseStation;
import com.example.vqshki.utils.BaseStationRequestMessage;
import com.example.vqshki.service.BaseStationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(path = "api/v1/report")
public class BaseStationController {
    private final BaseStationService baseStationService;

    @Autowired
    public BaseStationController(BaseStationService baseStationService) {
        this.baseStationService = baseStationService;
    }

    @PostMapping
    public void newReport(@RequestBody BaseStationRequestMessage report) {
        System.out.println(report);
        baseStationService.saveReports(report);
    }
}
