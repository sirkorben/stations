package com.example.vqshki.service;


import com.example.vqshki.models.BaseStation;
import com.example.vqshki.models.MobileStation;
import com.example.vqshki.models.Report;
import com.example.vqshki.repository.BaseStationRepository;
import com.example.vqshki.repository.MobileStationRepository;
import com.example.vqshki.repository.ReportRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.*;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import static com.example.vqshki.utils.ErrorCode.DETECTED_BY_TWO_BASE_STATIONS_WITH_ERROR;
import static com.example.vqshki.utils.ErrorCode.IMPOSSIBLE_TO_LOCATE_BY_ONE_BASE_STATION;
import static com.example.vqshki.utils.LocationDetermination.*;

@Component
public class ReportHandlingService implements ApplicationListener<ApplicationReadyEvent> {

    private final ReportRepository reportRepository;
    private final BaseStationRepository baseStationRepository;
    private final MobileStationRepository mobileStationRepository;

    Integer SCHEDULED_EXECUTOR_TIME_PERIOD = 10;
    Integer TIME_GAP_FROM_LATEST_REPORT_TIME = 5;

    Runnable handleReportsRunnable = this::handleReports;

    @Autowired
    public ReportHandlingService(ReportRepository reportRepository,
                                 BaseStationRepository baseStationRepository,
                                 MobileStationRepository mobileStationRepository) {
        this.reportRepository = reportRepository;
        this.baseStationRepository = baseStationRepository;
        this.mobileStationRepository = mobileStationRepository;
    }

    private static Timestamp getTimeWindow(Timestamp time, Integer gap) {
        Calendar cal = Calendar.getInstance();
        cal.setTimeInMillis(time.getTime());
        cal.add(Calendar.SECOND, -gap);
        return new Timestamp(cal.getTime().getTime());
    }

    @Override
    public void onApplicationEvent(final ApplicationReadyEvent event) {
        ScheduledExecutorService executor = Executors.newScheduledThreadPool(1);
        executor.scheduleAtFixedRate(handleReportsRunnable, 0, SCHEDULED_EXECUTOR_TIME_PERIOD, TimeUnit.SECONDS);
    }

    private void handleReports() {

        List<UUID> mobileStationIdslist = reportRepository.getLatestReportedMobileStationIds(getTimeWindow(new Timestamp((new Date()).getTime()), SCHEDULED_EXECUTOR_TIME_PERIOD));

        if (!mobileStationIdslist.isEmpty()) {
            mobileStationIdslist.forEach(mobileStationId -> {
                Timestamp latestDetectionTime = reportRepository.getLatestTimeDetectedByMobileStationId(mobileStationId);
                Timestamp latestDetectionTimeMinusGap = getTimeWindow(latestDetectionTime, TIME_GAP_FROM_LATEST_REPORT_TIME);
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
        Report report = reports.get(0);

        Optional<BaseStation> baseStation = getBaseStationById(report);
        double detectedWithRadius = report.getDistance();

        baseStation.ifPresent(baseStation1 -> mobileStationRepository.save(MobileStation.builder()
                .mobileStationId(mobileStationId)
                .lastKnownX(baseStation1.getCoordinateX())
                .lastKnownY(baseStation1.getCoordinateY())
                .errorRadius(detectedWithRadius)
                .errorCode(IMPOSSIBLE_TO_LOCATE_BY_ONE_BASE_STATION.getErrorCode())
                .errorMsg(IMPOSSIBLE_TO_LOCATE_BY_ONE_BASE_STATION.getErrorMessage())
                .timestamp(Timestamp.from(Instant.now()))
                .build()));
    }

    private void detectedByTwoBaseStations(List<Report> reports, UUID mobileStationId) {

        Optional<BaseStation> baseStationOne = getBaseStationById(reports.get(0));
        double baseStationOneDetectedInRadius = reports.get(0).getDistance();

        Optional<BaseStation> baseStationTwo = getBaseStationById(reports.get(1));
        double baseStationTwoDetectedInRadius = reports.get(1).getDistance();

        if (baseStationOne.isPresent() && baseStationTwo.isPresent()) {
            CoincidentPoints coincidencePoints = getPointsOfIntersection(
                    baseStationOne.get().getCoordinateX(),
                    baseStationOne.get().getCoordinateY(),
                    baseStationOneDetectedInRadius,
                    baseStationTwo.get().getCoordinateX(),
                    baseStationTwo.get().getCoordinateY(),
                    baseStationTwoDetectedInRadius);

            FinalCoordinates finalCoordinates = calculateFinalCoordinatesWithErrRadius(coincidencePoints);
            mobileStationRepository.save(MobileStation.builder().
                    mobileStationId(mobileStationId)
                    .lastKnownX(finalCoordinates.getCoordinateX())
                    .lastKnownY(finalCoordinates.getCoordinateY())
                    .errorRadius(finalCoordinates.getErrorRadius())
                    .errorMsg(DETECTED_BY_TWO_BASE_STATIONS_WITH_ERROR.getErrorMessage())
                    .errorCode(DETECTED_BY_TWO_BASE_STATIONS_WITH_ERROR.getErrorCode())
                    .timestamp(Timestamp.from(Instant.now())).build());
        }
    }

    private void detectedByThreeBaseStations(List<Report> reports, UUID mobileStationId) {
        Optional<BaseStation> baseStationOne = getBaseStationById(reports.get(0));
        double baseStationOneDetectedInRadius = reports.get(0).getDistance();

        Optional<BaseStation> baseStationTwo = getBaseStationById(reports.get(1));
        double baseStationTwoDetectedInRadius = reports.get(1).getDistance();

        Optional<BaseStation> baseStationThree = getBaseStationById(reports.get(2));
        double baseStationThreeDetectedInRadius = reports.get(2).getDistance();

        if (baseStationOne.isPresent() && baseStationTwo.isPresent() && baseStationThree.isPresent()) {
            CoincidentPoints coincidencePoints = getPointsOfIntersection(
                    baseStationOne.get().getCoordinateX(),
                    baseStationOne.get().getCoordinateY(),
                    baseStationOneDetectedInRadius,
                    baseStationTwo.get().getCoordinateX(),
                    baseStationTwo.get().getCoordinateY(),
                    baseStationTwoDetectedInRadius);
            CoincidentPoints coincidencePoints2 = getPointsOfIntersection(
                    baseStationTwo.get().getCoordinateX(),
                    baseStationTwo.get().getCoordinateY(),
                    baseStationTwoDetectedInRadius,
                    baseStationThree.get().getCoordinateX(),
                    baseStationThree.get().getCoordinateY(),
                    baseStationThreeDetectedInRadius);

            FinalCoordinates finalCoordinates = calculateFinalCoordinates(coincidencePoints, coincidencePoints2);
            mobileStationRepository.save(MobileStation.builder()
                    .mobileStationId(mobileStationId)
                    .lastKnownX(finalCoordinates.getCoordinateX())
                    .lastKnownY(finalCoordinates.getCoordinateY())
                    .timestamp(Timestamp.from(Instant.now())).build());
        }
    }

    private Optional<BaseStation> getBaseStationById(Report report) {
        return baseStationRepository.findById(report.getBaseStationId());
    }
}
