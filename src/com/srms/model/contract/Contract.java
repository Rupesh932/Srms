package com.srms.model.contract;

import com.srms.enums.ContractStatus;
import com.srms.enums.PropertyStatus;
import com.srms.model.base.SrmsEntity;
import com.srms.model.property.RentableProperty;
import com.srms.model.tenant.Tenant;

import java.time.LocalDateTime;

public class Contract extends SrmsEntity {
    private final Tenant tenant;
    private final RentableProperty rentableProperty;
    private final double baseRent;
    private final double securityDeposit;
    private final LocalDateTime startAt;
    private final double startElectricity;
    private final String agreementDocPath;
    private ContractStatus contractStatus;
    private LocalDateTime endAt;
    private double refundedDeposit;//ide suggest final

    //1. make private constructor
    private Contract(Builder builder){
      super(builder.id);
      this.tenant = builder.tenant;
      this.rentableProperty = builder.rentableProperty;
      this.baseRent = builder.baseRent;
      this.securityDeposit = builder.securityDeposit;
      this.startAt = builder.startAt;
      this.startElectricity = builder.startElectricity;
      this.agreementDocPath = builder.agreementDocPath;
      this.contractStatus = builder.contractStatus;
      this.endAt = null;
      this.refundedDeposit = 0.0;
    }

    public void cancelContract(double damageDeduction){
        if(this.contractStatus != ContractStatus.ACTIVE){
            throw new IllegalStateException("Contract has not active record.");
        }
        if(damageDeduction < 0){
           throw new IllegalArgumentException("Damage deduction amount should be positive.");
        }
        this.contractStatus = ContractStatus.TERMINATED;
        this.endAt = LocalDateTime.now();
        this.refundedDeposit = this.securityDeposit - damageDeduction;
        if(this.refundedDeposit < 0){
            System.out.println("⚠️ Warning: Security deposit is insufficient. Tenant owes: " + Math.abs(this.refundedDeposit));
        }

        if(this.rentableProperty.getPropertyStatus() != PropertyStatus.AVAILABLE){
                    this.rentableProperty.setPropertyStatus(PropertyStatus.AVAILABLE);
        }


        System.out.println("Contract terminate successful");
        System.out.println();

    }
    //2. getter only
    public Tenant getTenant() {
        return tenant;
    }

    public RentableProperty getRentableProperty() {
        return rentableProperty;
    }

    public double getBaseRent() {
        return baseRent;
    }

    public double getSecurityDeposit() {
        return securityDeposit;
    }

    public LocalDateTime getStartAt() {
        return startAt;
    }

    public double getStartElectricity() {
        return startElectricity;
    }

    public String getAgreementDocPath() {
        return agreementDocPath;
    }

    public ContractStatus getContractStatus() {
        return contractStatus;
    }

    public LocalDateTime getEndAt() {
        return endAt;
    }

    public double getRefundedDeposit() {
        return refundedDeposit;
    }

    public static class Builder{
        private final String id;
        private final Tenant tenant;
        private final RentableProperty rentableProperty;
        private final LocalDateTime startAt;
        private final double baseRent;

        private double securityDeposit = 5000;
        private double startElectricity;
        private String agreementDocPath = "agreements/default.pdf";
        private final ContractStatus contractStatus = ContractStatus.ACTIVE;


        public Builder(String id,Tenant tenant,RentableProperty rentableProperty,double baseRent){
           this.id = id;
           this.tenant = tenant;
           this.rentableProperty = rentableProperty;
           this.baseRent = baseRent;
           this.startAt = LocalDateTime.now();

           this.startElectricity = 0.0;
        }

        public Builder securityDeposit(double securityDeposit){
            this.securityDeposit = securityDeposit;
            return this;
        }
        public Builder startElectricity(double startElectricity){
            this.startElectricity = startElectricity;
            return this;
        }
        public Builder agreementDocPath(String agreementDocPath){
            this.agreementDocPath = agreementDocPath;
            return this;
        }

        public Contract build(){
            return new Contract(this);
        }

    }

    @Override
    public String toString() {
        return "Contract{" +
                "metadata=" + super.toString() +
                ", tenantId=" + (tenant != null ? tenant.getId() : "null") +
                ", rentablePropertyId=" + (rentableProperty != null ? rentableProperty.getId() : "null") +
                ", baseRent=" + baseRent +
                ", securityDeposit=" + securityDeposit +
                ", startAt=" + startAt +
                ", startElectricity=" + startElectricity +
                ", agreementDocPath='" + agreementDocPath + '\'' +
                ", contractStatus=" + contractStatus +
                ", endAt=" + endAt +
                ", refundedDeposit=" + refundedDeposit +
                '}';
    }
}
