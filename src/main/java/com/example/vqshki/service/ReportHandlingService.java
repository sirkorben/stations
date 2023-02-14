package com.example.vqshki.service;


import com.example.vqshki.models.BaseStation;
import com.example.vqshki.models.MobileStation;
import com.example.vqshki.models.Report;
import com.example.vqshki.repository.BaseStationRepository;
import com.example.vqshki.repository.MobileStationRepository;
import com.example.vqshki.repository.ReportRepository;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.*;

import static com.example.vqshki.utils.ErrorCode.DETECTED_BY_TWO_BASE_STATIONS_WITH_ERROR;
import static com.example.vqshki.utils.ErrorCode.IMPOSSIBLE_TO_LOCATE_BY_ONE_BASE_STATION;
import static com.example.vqshki.utils.LocationDetermination.*;

@Service
public class ReportHandlingService {

    final Integer SCHEDULED_TIME_PERIOD = 10;
    final Integer COINCIDENCE_GAP = 7;
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

    private static Timestamp timeProvidedMinusGap(Timestamp time, Integer gap) {
        Calendar cal = Calendar.getInstance();
        cal.setTimeInMillis(time.getTime());
        cal.add(Calendar.SECOND, -gap);
        return new Timestamp(cal.getTime().getTime());
    }

    private static Timestamp timeNow() {
        return Timestamp.from(Instant.now());
    }

    @Scheduled(fixedRate = 10000)
    public void timeout() {
        Timestamp timeToFindReportsInPast = timeProvidedMinusGap(
                Timestamp.from(Instant.now()), SCHEDULED_TIME_PERIOD);
        List<UUID> mobileStationIdslist = reportRepository.getLatestReportedMobileStationIdsBetweenTime(
                timeToFindReportsInPast, timeNow());
        if (mobileStationIdslist.isEmpty()) {
            return;
        }
        handleReports(mobileStationIdslist);
    }

    private void handleReports(List<UUID> reportedMobileStationIds) {

        reportedMobileStationIds.forEach(mobileStationId -> {
            Timestamp latestTimeDetectedMinusCoincidenceGap = timeProvidedMinusGap(
                    reportRepository.getLatestTimeDetectedByMobileStationId(mobileStationId), COINCIDENCE_GAP);


            List<UUID> coincidenceBaseStationIds = reportRepository.getCoincidenceBaseStationIds(
                    mobileStationId, latestTimeDetectedMinusCoincidenceGap, timeNow());


            List<Report> coincidenceReportList = new ArrayList<>();
            coincidenceBaseStationIds.forEach(baseStationId ->
                    coincidenceReportList.add(reportRepository.getLatestReportByBsId(baseStationId, mobileStationId)));

            if (coincidenceReportList.size() > 3) {
                //TODO: how to get only three latest reports?
                coincidenceReportList.sort(Comparator.comparing(Report::getTimeDetected).reversed());
                coincidenceReportList.subList(3, coincidenceReportList.size()).clear();
            }

            switch (coincidenceReportList.size()) {
                case 1 -> {
                    Report report = coincidenceReportList.get(0);
                    BaseStation baseStation = getBaseStationById(report);
                    mobileStationRepository.save(
                            detectedByOneReading(mobileStationId, new Reading(report, baseStation)));
                }
                case 2 -> {
                    Report reportOne = coincidenceReportList.get(0);
                    Report reportTwo = coincidenceReportList.get(1);
                    mobileStationRepository.save(detectedByTwoBaseStations(
                            mobileStationId,
                            new Reading(reportOne, getBaseStationById(reportOne)),
                            new Reading(reportTwo, getBaseStationById(reportTwo))));
                }
                case 3 -> {
                    Report reportOne = coincidenceReportList.get(0);
                    Report reportTwo = coincidenceReportList.get(1);
                    Report reportThree = coincidenceReportList.get(2);
                    mobileStationRepository.save(detectedByThreeBaseStations(
                            mobileStationId,
                            new Reading(reportOne, getBaseStationById(reportOne)),
                            new Reading(reportTwo, getBaseStationById(reportTwo)),
                            new Reading(reportThree, getBaseStationById(reportThree))));
                }
            }
        });
    }

    private MobileStation detectedByOneReading(UUID mobileStationId, Reading reading) {

        if (reading == null) {
            throw new IllegalArgumentException("reading cannot be null");
        }

        return MobileStation.builder()
                .mobileStationId(mobileStationId)
                .lastKnownX(reading.getCoordinateX())
                .lastKnownY(reading.getCoordinateY())
                .errorRadius(reading.getDistance())
                .errorCode(IMPOSSIBLE_TO_LOCATE_BY_ONE_BASE_STATION.getErrorCode())
                .errorMsg(IMPOSSIBLE_TO_LOCATE_BY_ONE_BASE_STATION.getErrorMessage())
                .timestamp(Timestamp.from(Instant.now()))
                .build();
    }

    private MobileStation detectedByTwoBaseStations(UUID mobileStationId, Reading firstReading, Reading secondReading) {

        if (firstReading == null || secondReading == null) {
            throw new IllegalArgumentException("reading cannot be null");
        }

        CoincidentPoints coincidencePoints = getPointsOfICirclesIntersection(
                firstReading.getCoordinateX(),
                firstReading.getCoordinateY(),
                firstReading.getDistance(),
                secondReading.getCoordinateX(),
                secondReading.getCoordinateY(),
                secondReading.getDistance());

        FinalCoordinates finalCoordinates = calculateFinalCoordinatesWithErrRadius(coincidencePoints);

        return MobileStation.builder().
                mobileStationId(mobileStationId)
                .lastKnownX(finalCoordinates.getCoordinateX())
                .lastKnownY(finalCoordinates.getCoordinateY())
                .errorRadius(finalCoordinates.getErrorRadius())
                .errorMsg(DETECTED_BY_TWO_BASE_STATIONS_WITH_ERROR.getErrorMessage())
                .errorCode(DETECTED_BY_TWO_BASE_STATIONS_WITH_ERROR.getErrorCode())
                .timestamp(Timestamp.from(Instant.now())).build();
    }

    private MobileStation detectedByThreeBaseStations(UUID mobileStationId, Reading firstReading, Reading secondReading, Reading thirdReading) {

        if (firstReading == null || secondReading == null || thirdReading == null) {
            throw new IllegalArgumentException("reading cannot be null");
        }

        CoincidentPoints coincidencePointsOne = getPointsOfICirclesIntersection(
                firstReading.getCoordinateX(),
                firstReading.getCoordinateY(),
                firstReading.getDistance(),
                secondReading.getCoordinateX(),
                secondReading.getCoordinateY(),
                secondReading.getDistance());
        CoincidentPoints coincidencePointsTwo = getPointsOfICirclesIntersection(
                secondReading.getCoordinateX(),
                secondReading.getCoordinateY(),
                secondReading.getDistance(),
                thirdReading.getCoordinateX(),
                thirdReading.getCoordinateY(),
                thirdReading.getDistance());

        FinalCoordinates finalCoordinates = calculateFinalCoordinates(coincidencePointsOne, coincidencePointsTwo);
        return MobileStation.builder()
                .mobileStationId(mobileStationId)
                .lastKnownX(finalCoordinates.getCoordinateX())
                .lastKnownY(finalCoordinates.getCoordinateY())
                .timestamp(Timestamp.from(Instant.now())).build();
    }

    private BaseStation getBaseStationById(Report report) {
        return baseStationRepository.findByUuid(report.getBaseStationId());
    }

    @Getter
    private static class Reading {
        private final double coordinateX;
        private final double coordinateY;
        private final double distance;

        public Reading(Report report, BaseStation baseStation) {
            coordinateX = baseStation.getCoordinateX();
            coordinateY = baseStation.getCoordinateY();
            distance = report.getDistance();
        }
    }
}
