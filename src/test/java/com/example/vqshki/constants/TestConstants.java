package com.example.vqshki.constants;

import com.example.vqshki.models.MobileStation;
import com.example.vqshki.models.Report;
import com.example.vqshki.utils.BaseStationRequestMessage;

import java.util.List;
import java.util.UUID;

import static com.example.vqshki.service.ReportHandlingService.timeNow;

public class TestConstants {
    public static UUID TEST_MOBILE_STATION_Z_ID = UUID.fromString("2d1ebc5b-7d27-4197-9cf0-e84451c5bbb1");
    public static UUID TEST_MOBILE_STATION_Y_ID = UUID.fromString("8a7addd4-206a-441e-a415-a1d9500c625c");
    public static UUID TEST_BASE_STATION_A_ID = UUID.fromString("04d2f1e4-64b1-4268-a3ec-7a080b474f65");
    public static UUID TEST_BASE_STATION_B = UUID.fromString("dd4a0e89-fd6f-41a2-804e-5a4a1801ae8e");
    public static UUID TEST_BASE_STATION_C = UUID.fromString("5a540269-cb0c-42bd-8a12-aad9b47c869b");

    public static double TEST_BASE_STATION_A_COORDINATE_X = 10;
    public static double TEST_BASE_STATION_A_COORDINATE_Y = 14;
    public static double TEST_BASE_STATION_A_DETECTION_RADIUS = 6.08;

    public static double TEST_BASE_STATION_B_COORDINATE_X = 24;
    public static double TEST_BASE_STATION_B_COORDINATE_Y = 18;
    public static double TEST_BASE_STATION_B_DETECTION_RADIUS = 9.43;

    public static double TEST_BASE_STATION_C_COORDINATE_X = 20;
    public static double TEST_BASE_STATION_C_COORDINATE_Y = 9;
    public static double TEST_BASE_STATION_C_DETECTION_RADIUS = 5.66;

    public static Report TEST_REPORT_ONE_FROM_BS_A =
            new Report(0L, TEST_BASE_STATION_A_ID, TEST_MOBILE_STATION_Z_ID, 4.3, timeNow());
    public static Report TEST_REPORT_TWO_FROM_BS_A =
            new Report(1L, TEST_BASE_STATION_A_ID, TEST_MOBILE_STATION_Y_ID,4.5 , timeNow());
    public static List<Report> TEST_MODIFIED_REPORT_LIST = List.of(TEST_REPORT_ONE_FROM_BS_A, TEST_REPORT_TWO_FROM_BS_A);
    public static BaseStationRequestMessage TEST_BS_REQUEST_MSG =
            new BaseStationRequestMessage(TEST_BASE_STATION_A_ID, TEST_MODIFIED_REPORT_LIST);
    public static MobileStation TEST_MOBILE_STATION_Z = MobileStation.builder()
            .mobileStationId(TEST_MOBILE_STATION_Z_ID).build();

}
