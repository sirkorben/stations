package com.example.vqshki.service;

import com.example.vqshki.models.Report;
import com.example.vqshki.repository.ReportRepository;
import com.example.vqshki.utils.BaseStationRequestMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
public class BaseStationService {
    private final ReportRepository reportRepository;

    @Autowired
    public BaseStationService(ReportRepository reportRepository) {
        this.reportRepository = reportRepository;
    }

    public List<Report> saveReports(BaseStationRequestMessage baseStationRequestMessage) {
        return reportRepository.saveAll(modifyReports(baseStationRequestMessage));
    }

    private List<Report> modifyReports(BaseStationRequestMessage bsRequestMsg) {
        UUID bsId = bsRequestMsg.getBaseStationId();
        List<Report> reports = new ArrayList<>();
        bsRequestMsg.getReports().forEach(rep -> {
            rep.setBaseStationId(bsId);
            reports.add(rep);
        });
        return reports;
    }
}
