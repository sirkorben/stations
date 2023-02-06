package com.example.vqshki.service;


import com.example.vqshki.models.BaseStation;
import com.example.vqshki.models.MobileStation;
import com.example.vqshki.models.Report;
import com.example.vqshki.repository.BaseStationRepository;
import com.example.vqshki.repository.MobileStationRepository;
import com.example.vqshki.repository.ReportRepository;
import com.example.vqshki.utils.LocationDetermination;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

import java.sql.Timestamp;
import java.util.*;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

@Component
public class ReportHandlingService implements ApplicationListener<ApplicationReadyEvent> {

    Integer TIME_PERIOD = 3;

    private final ReportRepository reportRepository;
    private final BaseStationRepository baseStationRepository;
    private final MobileStationRepository mobileStationRepository;

    @Autowired
    public ReportHandlingService(ReportRepository reportRepository,
                                 BaseStationRepository baseStationRepository,
                                 MobileStationRepository mobileStationRepository) {
        this.reportRepository = reportRepository;
        this.baseStationRepository = baseStationRepository;
        this.mobileStationRepository = mobileStationRepository;
    }
    Runnable helloRunnable = new Runnable() {
        public void run() {
            handleReports();
        }
    };
    @Override
    public void onApplicationEvent(final ApplicationReadyEvent event) {
        System.out.println("SpringBootApplication has been initialized");

        ScheduledExecutorService executor = Executors.newScheduledThreadPool(1);
        executor.scheduleAtFixedRate(helloRunnable, 0, TIME_PERIOD, TimeUnit.SECONDS);
    }

    private void handleReports() {
        List<UUID> mobileStationIdslist = reportRepository.getReportedMobileStationIds(timeWindow());

        if (!mobileStationIdslist.isEmpty()) {
            List<Report> coincidenceReportList = new ArrayList<>();
            mobileStationIdslist.forEach(mobileStationId -> {
                List<UUID> reportList = reportRepository.getReportsInTimeWindow(mobileStationId, timeWindow());
                reportList.forEach(bsId -> coincidenceReportList.add(reportRepository.getLatestReportByBsId(bsId)));
                if (coincidenceReportList.size() == 1) {
                    detectedByOneBaseStation(coincidenceReportList, coincidenceReportList.get(0).getMobileStationId());
                }
                if (coincidenceReportList.size() == 2) {
                    detectedByTwoBaseStations(coincidenceReportList);
                }
                if (coincidenceReportList.size() == 3) {
                    detectedByThreeBaseStations(coincidenceReportList);
                }
            });
        }
    }

    private void detectedByOneBaseStation(List<Report> reports, UUID mobileStationId) {
        double detectedWithRadius = reports.get(0).getDistance();
        Optional<BaseStation> baseStation = baseStationRepository.findById(reports.get(0).getBaseStationId());
        baseStation.ifPresent(station -> mobileStationRepository.saveLastKnownPointKnownByOneBaseStation(mobileStationId, station.getCoordinateX(), station.getCoordinateY(), detectedWithRadius));
    }

    private void detectedByTwoBaseStations(List<Report> reports) {
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
            mobileStationRepository.saveLastKnownPointKnownByTwoBaseStations(ms.getMobileStationId(), ms.getLastKnownX(), ms.getLastKnownY(), ms.getErrorRadius());
        }
    }

    private void detectedByThreeBaseStations(List<Report> reports) {
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
            mobileStationRepository.saveLastKnownPointKnownByThreeBaseStations(msId, finalAnswer.finalX, finalAnswer.finalY);
        }
    }

    private Timestamp timeWindow() {
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.SECOND, -TIME_PERIOD);
        return new Timestamp(cal.getTime().getTime());
    }
}
