package com.example.vqshki.controller;

import com.example.vqshki.models.BaseStation;
import com.example.vqshki.models.BaseStationReport;
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
    public void newReport(@RequestBody BaseStationReport report) {
        System.out.println(report);
        baseStationService.handleReport(report);
    }

    // TODO: for knowing available BSs Ids. should be deleted at the end
    @GetMapping
    public List<BaseStation> getAllBaseStations() {
        return baseStationService.findAllBaseStations();
    }
}
