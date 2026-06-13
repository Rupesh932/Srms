package com.srms.model.bill;

import com.srms.model.base.SrmsEntity;

import java.time.LocalDateTime;

public class UtilityReading extends SrmsEntity {
    private final double currentElectricityReading;
    private final double previousElectricityReading;

    public UtilityReading(String id,  double previousElectricityReading,double currentElectricityReading) {
        super(id);

        if (currentElectricityReading < 0 || previousElectricityReading < 0) {
            throw new IllegalArgumentException("Negative meter reading value detected.");
        }

        if (currentElectricityReading < previousElectricityReading) {
            throw new IllegalStateException("New reading must be greater or equals to previous reading");
        }
        this.currentElectricityReading = currentElectricityReading;
        this.previousElectricityReading = previousElectricityReading;

    }

    public double calculateConsumedUnits() {

        return currentElectricityReading - previousElectricityReading;
    }

    //getters only for immutability
    public double getCurrentElectricityReading() {
        return currentElectricityReading;
    }

    public double getPreviousElectricityReading() {
        return previousElectricityReading;
    }

    public LocalDateTime getReadingAt() {
        return getCreatedAt();
    }

    @Override
    public String toString() {
        return "UtilityReading{" +
                 super.toString() +
                ", previousElectricityReading=" + previousElectricityReading +
                ", currentElectricityReading=" + currentElectricityReading +
                ", consumedUnits=" + calculateConsumedUnits() +
                '}';
    }
}
