package com.example.vqshki.service;

import com.example.vqshki.models.BaseStation;
import com.example.vqshki.models.BaseStationReport;
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
    private final BaseStationRepository baseStationRepository;
    private final MobileStationRepository mobileStationRepository;

    @Autowired
    public BaseStationService(ReportRepository reportRepository,
                              BaseStationRepository baseStationRepository,
                              MobileStationRepository mobileStationRepository) {
        this.reportRepository = reportRepository;
        this.baseStationRepository = baseStationRepository;
        this.mobileStationRepository = mobileStationRepository;
    }

    public void handleReport(BaseStationReport report) {
        UUID bsId = report.getBaseStationId();
        report.getReports().forEach(rep -> {
            rep.setBaseStationId(bsId);
            reportRepository.save(rep);
            collectReportsAboutMsPositionInTimeWindow(rep);
        });
    }

    private void collectReportsAboutMsPositionInTimeWindow(Report report) {
        List<Report> coincidenceReportList = new ArrayList<>();
        List<UUID> reportList = reportRepository.getReportsInTimeWindow(report.getMobileStationId(), timeWindow(report.getTimeDetected()));
        reportList.forEach(bsId -> coincidenceReportList.add(reportRepository.getLatestReportByBsId(bsId)));

        if (coincidenceReportList.size() == 2) {
            coincidenceByTwoBsReports(coincidenceReportList);
        }
        if (coincidenceReportList.size() == 3) {
            coincidenceByThreeBsReports(coincidenceReportList);
        }
    }

    private void coincidenceByTwoBsReports(List<Report> reports) {
        UUID msId = reports.get(0).getMobileStationId();

        Optional<BaseStation> bsOne = baseStationRepository.findById(reports.get(0).getBaseStationId());
        double bsOneDetectedInRadius = reports.get(0).getDistance();

        Optional<BaseStation> bsTwo = baseStationRepository.findById(reports.get(1).getBaseStationId());
        double bsTwoDetectedInRadius = reports.get(1).getDistance();

        if (bsOne.isPresent() && bsTwo.isPresent()) {
            LocationDetermination.AnswerPoints coincidencePoints = LocationDetermination.getPointsOfIntersection(
                    bsOne.get().getCoordinateX(), bsOne.get().getCoordinateY(), bsOneDetectedInRadius, bsTwo.get().getCoordinateX(), bsTwo.get().getCoordinateY(), bsTwoDetectedInRadius);
            MobileStation ms = LocationDetermination.commonPointWithErrorRadius(coincidencePoints);
            ms.setMobileStationId(msId);
            mobileStationRepository.saveLastKnownPointKnownByTwoBS(ms.getMobileStationId(), ms.getLastKnownX(), ms.getLastKnownY(), ms.getErrorRadius());
        }
    }

    private void coincidenceByThreeBsReports(List<Report> reports) {
        UUID msId = reports.get(0).getMobileStationId();

        Optional<BaseStation> bsOne = baseStationRepository.findById(reports.get(0).getBaseStationId());
        double bsOneDetectedInRadius = reports.get(0).getDistance();

        Optional<BaseStation> bsTwo = baseStationRepository.findById(reports.get(1).getBaseStationId());
        double bsTwoDetectedInRadius = reports.get(1).getDistance();

        Optional<BaseStation> bsThree = baseStationRepository.findById(reports.get(2).getBaseStationId());
        double bsThreeDetectedInRadius = reports.get(2).getDistance();

        if (bsOne.isPresent() && bsTwo.isPresent() && bsThree.isPresent()) {
            LocationDetermination.AnswerPoints coincidencePoints = LocationDetermination.getPointsOfIntersection(
                    bsOne.get().getCoordinateX(), bsOne.get().getCoordinateY(), bsOneDetectedInRadius, bsTwo.get().getCoordinateX(), bsTwo.get().getCoordinateY(), bsTwoDetectedInRadius);
            LocationDetermination.AnswerPoints coincidencePoints2 = LocationDetermination.getPointsOfIntersection(
                    bsTwo.get().getCoordinateX(), bsTwo.get().getCoordinateY(), bsTwoDetectedInRadius, bsThree.get().getCoordinateX(), bsThree.get().getCoordinateY(), bsThreeDetectedInRadius);
            LocationDetermination.AnswerPoints finalAnswer = LocationDetermination.commonPoint(coincidencePoints, coincidencePoints2);

            // TODO: think about sending one common structure same with coincidenceByTwoBsReports();
            saveNewLocationOfMs(msId, finalAnswer.finalX, finalAnswer.finalY);
        }
    }

    private void saveNewLocationOfMs(UUID msId, double lastKnownX, double lastKnownY) {
        mobileStationRepository.saveLastKnownPoint(msId, lastKnownX, lastKnownY);
    }

    private Timestamp timeWindow(Timestamp time) {
        Calendar cal = Calendar.getInstance();
        cal.setTimeInMillis(time.getTime());
        cal.add(Calendar.SECOND, -7);
        return new Timestamp(cal.getTime().getTime());
    }

    //TODO: delete later when it is not needed anymore
    public List<BaseStation> findAllBaseStations() {
        return baseStationRepository.findAll();
    }
}

