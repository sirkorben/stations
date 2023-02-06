package com.example.vqshki.service;

import com.example.vqshki.models.BaseStation;
import com.example.vqshki.utils.BaseStationRequestMessage;
import com.example.vqshki.models.MobileStation;
import com.example.vqshki.models.Report;
import com.example.vqshki.repository.BaseStationRepository;
import com.example.vqshki.repository.MobileStationRepository;
import com.example.vqshki.repository.ReportRepository;
import com.example.vqshki.utils.LocationDetermination;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.*;

@Service
public class BaseStationService {
    private final ReportRepository reportRepository;

    @Autowired
    public BaseStationService(ReportRepository reportRepository) {
        this.reportRepository = reportRepository;
    }

    public void saveReports(BaseStationRequestMessage report) {
        UUID bsId = report.getBaseStationId();
        List<Report> reports = new ArrayList<>();
        report.getReports().forEach(rep -> {
            rep.setBaseStationId(bsId);
            reports.add(rep);
        });
        reportRepository.saveAll(reports);

    }

}

