package com.example.vqshki.models;

import jakarta.persistence.*;

import java.util.UUID;

@Entity
@Table
public class MobileStation {
    @Id
    @SequenceGenerator(
            name = "mobile_station_sequence",
            sequenceName = "mobile_station_sequence",
            allocationSize = 1
    )
    @GeneratedValue(
            strategy = GenerationType.UUID,
            generator = "UUID"
    )
    private UUID mobileStationId;
    private double lastKnownX;
    private double lastKnownY;
    private double errorRadius = 0;
    public MobileStation() {
    }

    public MobileStation(double lastKnownX, double lastKnownY, double errorRadius) {
        this.lastKnownX = lastKnownX;
        this.lastKnownY = lastKnownY;
        this.errorRadius = errorRadius;
    }

    public MobileStation(UUID mobileStationId, double lastKnownX, double lastKnownY, double errorRadius) {
        this.mobileStationId = mobileStationId;
        this.lastKnownX = lastKnownX;
        this.lastKnownY = lastKnownY;
        this.errorRadius = errorRadius;

    }

    public UUID getMobileStationId() {
        return mobileStationId;
    }

    public void setMobileStationId(UUID uuid) {
        this.mobileStationId = uuid;
    }

    public double getLastKnownX() {
        return lastKnownX;
    }

    public void setLastKnownX(double lastKnownX) {
        this.lastKnownX = lastKnownX;
    }

    public double getLastKnownY() {
        return lastKnownY;
    }

    public void setLastKnownY(double lastKnownY) {
        this.lastKnownY = lastKnownY;
    }

    public double getErrorRadius() {
        return errorRadius;
    }

    public void setErrorRadius(double errorRadius) {
        this.errorRadius = errorRadius;
    }

    @Override
    public String toString() {
        return "MobileStation{" +
                "uuid=" + mobileStationId +
                ", lastKnownX=" + lastKnownX +
                ", lastKnownY=" + lastKnownY +
                ", errorRadius=" + errorRadius +
                '}';
    }
}
