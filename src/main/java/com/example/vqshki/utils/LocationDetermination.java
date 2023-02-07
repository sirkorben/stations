package com.example.vqshki.utils;

import com.example.vqshki.models.MobileStation;

import static java.lang.Math.*;


//TODO: remove comments when don't needed
public class LocationDetermination {
    public static class AnswerPoints {
        public double x, y;
        public double x1, y1;
        public double finalX, finalY;
        public void setFinalX(double finalX) {
            this.finalX = finalX;
        }
        public void setFinalY(double finalY) {
            this.finalY = finalY;
        }

        @Override
        public String toString() {
            return "AnswerPoints{" +
                    "x=" + x +
                    ", y=" + y +
                    ", x1=" + x1 +
                    ", y1=" + y1 +
                    ", finalX=" + finalX +
                    ", finalY=" + finalY +
                    '}';
        }
    }
    public static AnswerPoints getPointsOfIntersection(double x10, double y10, double r1, double x20, double y20, double r2) {
        AnswerPoints points = new AnswerPoints();
        double x0, y0;      // координаты точки пересечения всех линий
        double d;           // расстояние между центрами окружностей
        double a;           // расстояние от r1 до точки пересечения всех линий
        double h;           // расстояние от точки пересеч окружностей до точки пересеч всех линий

        d = sqrt(pow(abs(x10 - x20), 2) + pow(abs(y10 - y20), 2));
        if (d > r1 + r2) {
            // means no intersections at all
        } else {
            a = (r1 * r1 - r2 * r2 + d * d) / (2 * d);
            h = sqrt(pow(r1, 2) - pow(a, 2));

            x0 = x10 + a * (x20 - x10) / d;
            y0 = y10 + a * (y20 - y10) / d;
            points.x = x0 + h * (y20 - y10) / d;
            points.y = y0 - h * (x20 - x10) / d;
            if (a == r1) {
                // means one point of intersection
            } else {
                points.x1 = x0 - h * (y20 - y10) / d;
                points.y1 = y0 + h * (x20 - x10) / d;
            }
        }
        return points;
    }
    public static MobileStation commonPoint(AnswerPoints points1, AnswerPoints points2) {
        MobileStation mobileStation = new MobileStation();
        if  (Math.ceil(points1.x) != Math.ceil(points2.x) && Math.ceil(points1.x) != Math.ceil(points2.x1)) {
            if (Math.ceil(points1.x1) == Math.ceil(points2.x)) {
                mobileStation.setLastKnownX(points1.x1);
                mobileStation.setLastKnownY(points1.y1);
            } else {
                mobileStation.setLastKnownX(points2.x1);
                mobileStation.setLastKnownY(points2.y1);
            }
        } else {
            mobileStation.setLastKnownX(points1.x);
            mobileStation.setLastKnownY(points1.y);
        }

        return mobileStation;
    }
    public static MobileStation commonPointWithErrorRadius(AnswerPoints points) {
        MobileStation mobileStation = new MobileStation();
        mobileStation.setLastKnownX((points.x + points.x1) / 2);
        mobileStation.setLastKnownY((points.y + points.y1) / 2);
        double d = sqrt(pow((points.y1 - points.y), 2) + pow((points.x1 - points.x), 2));

        mobileStation.setErrorRadius(d / 2);

        return mobileStation;
    }
}

