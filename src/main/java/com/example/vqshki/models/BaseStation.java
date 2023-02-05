package com.example.vqshki.models;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;

import java.util.UUID;

@Entity
@Table
public class BaseStation {
    @Id
    @SequenceGenerator(
            name = "base_station_sequence",
            sequenceName = "base_station_sequence",
            allocationSize = 1
    )
    @GeneratedValue(
            strategy = GenerationType.UUID,
            generator = "UUID"
    )
    private UUID baseStationId;
    private double coordinateX;
    private double coordinateY;
    private String name;
    private double detectionRadius;

    public BaseStation() {
    }

    public BaseStation(UUID baseStationId, String name, double coordinateX, double coordinateY, double detectionRadius) {
        this.baseStationId = baseStationId;
        this.name = name;
        this.coordinateX = coordinateX;
        this.coordinateY = coordinateY;
        this.detectionRadius = detectionRadius;
    }

    public BaseStation(String name, double coordinateX, double coordinateY, double detectionRadius) {
        this.name = name;
        this.coordinateX = coordinateX;
        this.coordinateY = coordinateY;
        this.detectionRadius = detectionRadius;
    }


    public UUID getBaseStationId() {
        return baseStationId;
    }

    public void setBaseStationId(UUID uuid) {
        this.baseStationId = uuid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getCoordinateX() {
        return coordinateX;
    }

    public void setCoordinateX(double x) {
        this.coordinateX = x;
    }

    public double getCoordinateY() {
        return coordinateY;
    }

    public void setCoordinateY(double y) {
        this.coordinateY = y;
    }

    public double getDetectionRadius() {
        return detectionRadius;
    }

    public void setDetectionRadius(double detectionRadius) {
        this.detectionRadius = detectionRadius;
    }

    @Override
    public String toString() {
        return "BaseStation{" +
                "uuid=" + baseStationId +
                ", x=" + coordinateX +
                ", y=" + coordinateY +
                ", name='" + name + '\'' +
                ", detectionRadius=" + detectionRadius +
                '}';
    }
}
