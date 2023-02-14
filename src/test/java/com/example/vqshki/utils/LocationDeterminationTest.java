package com.example.vqshki.utils;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

import static com.example.vqshki.constants.TestConstants.*;
import static com.example.vqshki.utils.LocationDetermination.*;

class LocationDeterminationTest {

//    @Test
//    void getPointsOfICirclesIntersectionTest() {
//
//        CoincidentPoints expectedCoincidentPoints;
//        expectedCoincidentPoints = new CoincidentPoints();
//        expectedCoincidentPoints.x = 16.0;
//        expectedCoincidentPoints.y = 13.01;
//        expectedCoincidentPoints.x1 = 14.57;
//        expectedCoincidentPoints.y1 = 18.01;
//
//
//        CoincidentPoints coincidencePoints = getPointsOfICirclesIntersection(
//                TEST_BASE_STATION_A_COORDINATE_X,
//                TEST_BASE_STATION_A_COORDINATE_Y,
//                TEST_BASE_STATION_A_DETECTION_RADIUS,
//                TEST_BASE_STATION_B_COORDINATE_X,
//                TEST_BASE_STATION_B_COORDINATE_Y,
//                TEST_BASE_STATION_B_DETECTION_RADIUS
//        );
//
//        Assertions.assertThat(coincidencePoints).isEqualTo(expectedCoincidentPoints);
//
//    }
//
//    @Test
//    void calculateFinalCoordinatesTest() {
//        FinalCoordinates expectedFinalCoordinates;
//        expectedFinalCoordinates = new FinalCoordinates();
//        expectedFinalCoordinates.coordinateX = 16.0;
//        expectedFinalCoordinates.coordinateY = 13.01;
//
//        CoincidentPoints coincidentPoints1 = getPointsOfICirclesIntersection(
//                TEST_BASE_STATION_A_COORDINATE_X,
//                TEST_BASE_STATION_A_COORDINATE_Y,
//                TEST_BASE_STATION_A_DETECTION_RADIUS,
//                TEST_BASE_STATION_B_COORDINATE_X,
//                TEST_BASE_STATION_B_COORDINATE_Y,
//                TEST_BASE_STATION_B_DETECTION_RADIUS
//        );
//        CoincidentPoints coincidentPoints2 = getPointsOfICirclesIntersection(
//                TEST_BASE_STATION_B_COORDINATE_X,
//                TEST_BASE_STATION_B_COORDINATE_Y,
//                TEST_BASE_STATION_B_DETECTION_RADIUS,
//                TEST_BASE_STATION_C_COORDINATE_X,
//                TEST_BASE_STATION_C_COORDINATE_Y,
//                TEST_BASE_STATION_C_DETECTION_RADIUS
//        );
//
//        FinalCoordinates actualFinalCoordinates = calculateFinalCoordinates(coincidentPoints1, coincidentPoints2);
//
//        Assertions.assertThat(actualFinalCoordinates).isEqualTo(expectedFinalCoordinates);
//
//    }
//
//    @Test
//    void calculateFinalCoordinatesWithErrRadiusTest() {
//        FinalCoordinates expectedFinalCoordinates;
//        expectedFinalCoordinates = new FinalCoordinates();
//        expectedFinalCoordinates.coordinateX = 15.2;
//        expectedFinalCoordinates.coordinateY = 11.4;
//        expectedFinalCoordinates.errorRadius = 1.79;
//
//        CoincidentPoints coincidentPoints = getPointsOfICirclesIntersection(
//                TEST_BASE_STATION_A_COORDINATE_X,
//                TEST_BASE_STATION_A_COORDINATE_Y,
//                TEST_BASE_STATION_A_DETECTION_RADIUS,
//                TEST_BASE_STATION_C_COORDINATE_X,
//                TEST_BASE_STATION_C_COORDINATE_Y,
//                TEST_BASE_STATION_C_DETECTION_RADIUS
//        );
//
//        FinalCoordinates actualFinalCoordinates = calculateFinalCoordinatesWithErrRadius(coincidentPoints);
//
//        Assertions.assertThat(actualFinalCoordinates).isEqualTo(expectedFinalCoordinates);
//
//    }
}