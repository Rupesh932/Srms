package com.srms.model.bill;


import com.srms.config.PropertyPricingPolicy;
import com.srms.enums.BillStatus;
import com.srms.model.base.SrmsEntity;
import com.srms.model.contract.Contract;
import com.srms.model.property.Flat;
import com.srms.model.property.RentableProperty;
import com.srms.model.property.Room;

import java.time.LocalDateTime;

public class RentBill extends SrmsEntity {
    private final Contract contract;
    private final UtilityReading utilityReading;
    private final double rentAmount;
    private final double fixWaterCharge;
    private final double fixGarbageCharge;
    private final double ratePerUnit;
    private final double electricityCharge;
    private final double previousDue;
    private final double advancePaid;
    private final double totalPayableAmount;

    private BillStatus billStatus;
    private LocalDateTime paidAt;

    private RentBill(Builder builder) {
        super(builder.id);
        this.contract = builder.contract;
        this.utilityReading = builder.utilityReading;
        this.rentAmount = builder.rentAmount;
        this.fixWaterCharge = builder.fixWaterCharge;
        this.fixGarbageCharge = builder.fixGarbageCharge;
        this.ratePerUnit = builder.ratePerUnit;
        this.electricityCharge = builder.electricityCharge;
        this.previousDue = builder.previousDue;
        this.advancePaid = builder.advancePaid;

        this.totalPayableAmount = calculateTotalPayable();
        this.billStatus = BillStatus.UNPAID;

    }

    private double calculateTotalPayable() {
        return (this.rentAmount + this.electricityCharge + this.fixWaterCharge + this.fixGarbageCharge + this.previousDue) - this.advancePaid;
    }

    public void printBill() {

        System.out.println("\n=========================================");
        System.out.println("           🏠 SRMS RENT INVOICE          ");
        System.out.println("=========================================");
        System.out.println("Bill ID     : " + getId());
        System.out.println("Date        : " + getGeneratedDate());
        System.out.println("Contract ID : " + contract.getId());
        System.out.println("Bill Status : " + billStatus);
        System.out.println("-----------------------------------------");
        System.out.printf("1. Room/Flat Rent       : Rs. %.2f%n", rentAmount);
        System.out.printf("2. Water Charge (Fix)   : Rs. %.2f%n", fixWaterCharge);
        System.out.printf("3. Garbage Charge (Fix) : Rs. %.2f%n", fixGarbageCharge);
        System.out.printf("4. Electricity Charge   : Rs. %.2f (Rate: Rs. %.2f)%n", electricityCharge, ratePerUnit);
        if (utilityReading != null) {
            System.out.printf("   [Units Consumed: %.2f (%.2f -> %.2f)]%n",
                    utilityReading.calculateConsumedUnits(),
                    utilityReading.getPreviousElectricityReading(),
                    utilityReading.getCurrentElectricityReading());
        }
        System.out.printf("5. Previous Due/Arrears : Rs. %.2f%n", previousDue);
        System.out.printf("6. Advance Paid (-)     : Rs. %.2f%n", advancePaid);
        System.out.println("-----------------------------------------");
        System.out.printf("💸 Total Payable Amount  : Rs. %.2f%n", totalPayableAmount);
        if (billStatus == BillStatus.PAID) {
            System.out.println("Paid At     : " + paidAt);
        }
        System.out.println("=========================================\n");
    }

    public void markAsPaid() {

        if (this.billStatus == BillStatus.PAID) {
            throw new IllegalStateException("This bill has already been paid. ");
        }
        this.billStatus = BillStatus.PAID;
        this.paidAt = LocalDateTime.now();
        System.out.println("Bill ID: " + getId() + "  =>  Status: PAID");
    }

    public LocalDateTime getGeneratedDate() {
        return getCreatedAt();
    }

    public Contract getContract() {
        return contract;
    }

    public UtilityReading getUtilityReading() {
        return utilityReading;
    }

    public double getRentAmount() {
        return rentAmount;
    }

    public double getFixWaterCharge() {
        return fixWaterCharge;
    }

    public double getFixGarbageCharge() {
        return fixGarbageCharge;
    }

    public double getRatePerUnit() {
        return ratePerUnit;
    }

    public double getElectricityCharge() {
        return electricityCharge;
    }

    public double getPreviousDue() {
        return previousDue;
    }

    public double getAdvancePaid() {
        return advancePaid;
    }

    public double getTotalPayableAmount() {
        return totalPayableAmount;
    }

    public BillStatus getBillStatus() {
        return billStatus;
    }

    public LocalDateTime getPaidAt() {
        return paidAt;
    }

    public static class Builder {
        private final String id;
        private final Contract contract;
        private final UtilityReading utilityReading;
        private final double ratePerUnit;

        private final double rentAmount;
        private final double fixWaterCharge;
        private final double fixGarbageCharge;
        private double electricityCharge = 0.0;
        private double previousDue = 0.0;
        private double advancePaid = 0.0;


        public Builder(String id, Contract contract, UtilityReading utilityReading, double ratePerUnit) {
            if (contract == null) {
                throw new IllegalStateException("No contract => no Bill ");
            }
            if (ratePerUnit <= 0) {
                throw new IllegalStateException("rate per unit must be positive");
            }
            this.id = id;
            this.contract = contract;
            this.utilityReading = utilityReading;
            this.ratePerUnit = ratePerUnit;
            this.rentAmount = contract.getBaseRent();

            RentableProperty prop = contract.getRentableProperty();
            if (prop instanceof Flat flat) {
                int rooms = flat.getRoomList().size();
                this.fixWaterCharge = PropertyPricingPolicy.getWaterChargeFlat(rooms);
                this.fixGarbageCharge = PropertyPricingPolicy.getGarbageChargeFlat(rooms);
            } else if (prop instanceof Room room) {
                this.fixWaterCharge = PropertyPricingPolicy.getWaterChargeRoom(room.getBathroomType(), room.getRoomType());
                this.fixGarbageCharge = PropertyPricingPolicy.getGarbageChargeRoom(room.getRoomType());
            } else {
                this.fixWaterCharge = 0.0;
                this.fixGarbageCharge = 0.0;
            }

            if (utilityReading != null) {
                this.electricityCharge = utilityReading.calculateConsumedUnits() * ratePerUnit;
            }

            if (contract.getRefundedDeposit() < 0) {
                this.previousDue = Math.abs(contract.getRefundedDeposit());
            }
        }

        public Builder previousDue(double previousDue) {
            if (previousDue < 0) {
                throw new IllegalArgumentException("Previous due should not be negative. ");
            }
            this.previousDue = previousDue;
            return this;
        }

        public Builder advancePaid(double advancePaid) {
            if (advancePaid < 0) {
                throw new IllegalArgumentException("Advance paid should not be negative.");
            }
            this.advancePaid = advancePaid;
            return this;
        }

        public RentBill build() {
            return new RentBill(this);
        }
    }

    @Override
    public String toString() {
        return "RentBill{" +
                 super.toString() +
                ", contractId=" + (contract != null ? contract.getId() : "null") + // Circular Loop फिक्स गरियो
                ", rentAmount=" + rentAmount +
                ", fixWaterCharge=" + fixWaterCharge +
                ", fixGarbageCharge=" + fixGarbageCharge +
                ", ratePerUnit=" + ratePerUnit +
                ", electricityCharge=" + electricityCharge +
                ", previousDue=" + previousDue +
                ", advancePaid=" + advancePaid +
                ", totalPayableAmount=" + totalPayableAmount +
                ", billStatus=" + billStatus +
                ", paidAt=" + paidAt +
                '}';
    }
}
