package com.example.vqshki.utils;

import lombok.*;

import static java.lang.Math.*;


//TODO: remove comments when don't needed
public class LocationDetermination {

    public static class CoincidentPoints {
        double x, y;
        double x1, y1;
    }

    @Getter
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class FinalCoordinates {
        double coordinateX, coordinateY, errorRadius;
    }

    public static CoincidentPoints getPointsOfIntersection(double x10, double y10, double r1, double x20, double y20, double r2) {
        CoincidentPoints points = new CoincidentPoints();
        double x0, y0;      // координаты точки пересечения всех линий
        double d;           // расстояние между центрами окружностей
        double a;           // расстояние от r1 до точки пересечения всех линий
        double h;           // расстояние от точки пересеч окружностей до точки пересеч всех линий

        d = sqrt(pow(abs(x10 - x20), 2) + pow(abs(y10 - y20), 2));
        if (!(d > r1 + r2)) {
            // means no intersections at all
            a = (r1 * r1 - r2 * r2 + d * d) / (2 * d);
            h = sqrt(pow(r1, 2) - pow(a, 2));

            x0 = x10 + a * (x20 - x10) / d;
            y0 = y10 + a * (y20 - y10) / d;

            points.x = x0 + h * (y20 - y10) / d;
            points.y = y0 - h * (x20 - x10) / d;
            if (a != r1) {
                // means one point of intersection
                points.x1 = x0 - h * (y20 - y10) / d;
                points.y1 = y0 + h * (x20 - x10) / d;
            }
        }
        return points;
    }

    public static FinalCoordinates calculateFinalCoordinates(CoincidentPoints points1, CoincidentPoints points2) {
        double x;
        double y;

        if (Math.ceil(points1.x) != Math.ceil(points2.x) && Math.ceil(points1.x) != Math.ceil(points2.x1)) {
            if (Math.ceil(points1.x1) == Math.ceil(points2.x)) {
                x = points1.x1;
                y = points1.y1;
            } else {
                x = points2.x1;
                y = points2.y1;
            }
        } else {
            x = points1.x;
            y = points1.y;
        }

        return FinalCoordinates.builder().coordinateX(x).coordinateY(y).build();
    }

    public static FinalCoordinates calculateFinalCoordinatesWithErrRadius(CoincidentPoints points) {
        double x = (points.x + points.x1) / 2;
        double y = (points.y + points.y1) / 2;
        double errRadius = sqrt(pow((points.y1 - points.y), 2) + pow((points.x1 - points.x), 2)) / 2;

        return FinalCoordinates.builder().coordinateX(x).coordinateY(y).errorRadius(errRadius).build();
    }
}
