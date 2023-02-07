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

    private final ReportRepository reportRepository;
    private final BaseStationRepository baseStationRepository;
    private final MobileStationRepository mobileStationRepository;

    Integer TIME_PERIOD_FOR_THREAD = 10;
    Integer TIME_FOR_REPORTS_COINCIDENCE = 5;

    Runnable handleReportsRunnable = new Runnable() {
        public void run() {
            handleReports();
        }
    };

    @Autowired
    public ReportHandlingService(ReportRepository reportRepository,
                                 BaseStationRepository baseStationRepository,
                                 MobileStationRepository mobileStationRepository) {
        this.reportRepository = reportRepository;
        this.baseStationRepository = baseStationRepository;
        this.mobileStationRepository = mobileStationRepository;
    }

    @Override
    public void onApplicationEvent(final ApplicationReadyEvent event) {
        ScheduledExecutorService executor = Executors.newScheduledThreadPool(1);
        executor.scheduleAtFixedRate(handleReportsRunnable, 0, TIME_PERIOD_FOR_THREAD, TimeUnit.SECONDS);
    }

    private void handleReports() {

        List<UUID> mobileStationIdslist = reportRepository.getLatestReportedMobileStationIds(timeWindow(new Timestamp((new Date()).getTime()), TIME_PERIOD_FOR_THREAD));

        if (!mobileStationIdslist.isEmpty()) {
            mobileStationIdslist.forEach(mobileStationId -> {
                Timestamp latestDetectionTime = reportRepository.getLatestTimeDetectedByMobileStationId(mobileStationId);
                Timestamp latestDetectionTimeMinusGap = timeWindow(latestDetectionTime, TIME_FOR_REPORTS_COINCIDENCE);
                List<UUID> detectedByBaseStations = reportRepository.getReportsInTimeWindow(mobileStationId, latestDetectionTimeMinusGap);
                List<Report> coincidenceReportList = new ArrayList<>();

                detectedByBaseStations.forEach(baseStationId ->
                        coincidenceReportList.add(reportRepository.getLatestReportByBsId(baseStationId, mobileStationId)));
                if (coincidenceReportList.size() == 1) {
                    detectedByOneBaseStation(coincidenceReportList, mobileStationId);
                }
                if (coincidenceReportList.size() == 2) {
                    detectedByTwoBaseStations(coincidenceReportList, mobileStationId);
                }
                if (coincidenceReportList.size() == 3) {
                    detectedByThreeBaseStations(coincidenceReportList, mobileStationId);
                }
            });
        }
    }

    private void detectedByOneBaseStation(List<Report> reports, UUID mobileStationId) {
        double detectedWithRadius = reports.get(0).getDistance();
        Optional<BaseStation> baseStation = baseStationRepository.findById(reports.get(0).getBaseStationId());
        baseStation.ifPresent(baseStation1 -> {

            MobileStation mobileStation = new MobileStation();
            mobileStation.setMobileStationId(mobileStationId);
            mobileStation.setLastKnownX(baseStation1.getCoordinateX());
            mobileStation.setLastKnownY(baseStation1.getCoordinateY());
            mobileStation.setErrorRadius(detectedWithRadius);
            mobileStation.setErrorCode(5001);
            mobileStation.setErrorMsg("Impossible to accurately locate mobile station by 1 Base Station");
            mobileStationRepository.save(mobileStation);
        });
    }

    private void detectedByTwoBaseStations(List<Report> reports, UUID mobileStationId) {

        Optional<BaseStation> baseStationOne = baseStationRepository.findById(reports.get(0).getBaseStationId());
        double baseStationOneDetectedInRadius = reports.get(0).getDistance();

        Optional<BaseStation> baseStationTwo = baseStationRepository.findById(reports.get(1).getBaseStationId());
        double baseStationTwoDetectedInRadius = reports.get(1).getDistance();

        if (baseStationOne.isPresent() && baseStationTwo.isPresent()) {
            LocationDetermination.AnswerPoints coincidencePoints = LocationDetermination.getPointsOfIntersection(
                    baseStationOne.get().getCoordinateX(),
                    baseStationOne.get().getCoordinateY(),
                    baseStationOneDetectedInRadius,
                    baseStationTwo.get().getCoordinateX(),
                    baseStationTwo.get().getCoordinateY(),
                    baseStationTwoDetectedInRadius);

            MobileStation ms = LocationDetermination.commonPointWithErrorRadius(coincidencePoints);
            ms.setMobileStationId(mobileStationId);
            mobileStationRepository.save(ms);
        }
    }

    private void detectedByThreeBaseStations(List<Report> reports, UUID mobileStationId) {
        Optional<BaseStation> baseStationOne = baseStationRepository.findById(reports.get(0).getBaseStationId());
        double baseStationOneDetectedInRadius = reports.get(0).getDistance();

        Optional<BaseStation> baseStationTwo = baseStationRepository.findById(reports.get(1).getBaseStationId());
        double baseStationTwoDetectedInRadius = reports.get(1).getDistance();

        Optional<BaseStation> baseStationThree = baseStationRepository.findById(reports.get(2).getBaseStationId());
        double baseStationThreeDetectedInRadius = reports.get(2).getDistance();

        if (baseStationOne.isPresent() && baseStationTwo.isPresent() && baseStationThree.isPresent()) {
            LocationDetermination.AnswerPoints coincidencePoints = LocationDetermination.getPointsOfIntersection(
                    baseStationOne.get().getCoordinateX(),
                    baseStationOne.get().getCoordinateY(),
                    baseStationOneDetectedInRadius,
                    baseStationTwo.get().getCoordinateX(),
                    baseStationTwo.get().getCoordinateY(),
                    baseStationTwoDetectedInRadius);
            LocationDetermination.AnswerPoints coincidencePoints2 = LocationDetermination.getPointsOfIntersection(
                    baseStationTwo.get().getCoordinateX(),
                    baseStationTwo.get().getCoordinateY(),
                    baseStationTwoDetectedInRadius,
                    baseStationThree.get().getCoordinateX(),
                    baseStationThree.get().getCoordinateY(),
                    baseStationThreeDetectedInRadius);

            MobileStation mobileStation = LocationDetermination.commonPoint(coincidencePoints, coincidencePoints2);

            mobileStation.setMobileStationId(mobileStationId);
            mobileStationRepository.save(mobileStation);

        }
    }

    private Timestamp timeWindow(Timestamp time, Integer gap) {
        Calendar cal = Calendar.getInstance();
        cal.setTimeInMillis(time.getTime());
        cal.add(Calendar.SECOND, -gap);
        return new Timestamp(cal.getTime().getTime());
    }
}
