package com.example.vqshki.controller;

import com.example.vqshki.dto.ReportDTO;
import com.example.vqshki.mappers.ReportMapper;
import com.example.vqshki.models.Report;
import com.example.vqshki.service.BaseStationService;
import com.example.vqshki.utils.BaseStationRequestMessage;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping(path = "api/v1/report")
public class BaseStationController {
    private final BaseStationService baseStationService;
    private ReportMapper reportMapper;

    public BaseStationController(BaseStationService baseStationService, ReportMapper reportMapper) {
        this.baseStationService = baseStationService;
        this.reportMapper = reportMapper;
    }

    @PostMapping
    public ResponseEntity<List<ReportDTO>> createReport(@RequestBody BaseStationRequestMessage report) {
        List<Report> reportList = baseStationService.saveReports(report);
        return Optional.ofNullable(reportMapper.reportToReportDTO(reportList))
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.internalServerError().build());
    }
}
