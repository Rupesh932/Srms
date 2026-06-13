package com.srms.model.bill;

import com.srms.model.base.SrmsEntity;

import java.time.LocalDateTime;

public class UtilityReading extends SrmsEntity {
    private final double currentElectricityReading;
    private final double previousElectricityReading;

    public UtilityReading(Builder builder) {
        super(builder.id);

        if (builder.currentElectricityReading < 0 || builder.previousElectricityReading < 0) {
            throw new IllegalArgumentException("Negative meter reading value detected.");
        }

        if (builder.currentElectricityReading < builder.previousElectricityReading) {
            throw new IllegalStateException("New reading must be greater or equals to previous reading");
        }
        this.currentElectricityReading = builder.currentElectricityReading;
        this.previousElectricityReading = builder.previousElectricityReading;

    }

    public double calculateConsumedUnits() {

        return this.currentElectricityReading - this.previousElectricityReading;
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

    public static class Builder {
        private final String id;
        private double currentElectricityReading;
        private double previousElectricityReading;

        public Builder(String id) {
            this.id = id;
            this.previousElectricityReading = -1;
            this.currentElectricityReading = -1;
        }

        public Builder currentReading(double val){
            this.currentElectricityReading  = val;
            return this;
        }
        public Builder previousReading(double val){
            this.previousElectricityReading = val;
            return this;
        }

        public UtilityReading build() {
            if(this.currentElectricityReading < 0 || this.previousElectricityReading < 0){
                throw new IllegalStateException("Both readings must be set before building.");
            }
            return new UtilityReading(this);
        }
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
