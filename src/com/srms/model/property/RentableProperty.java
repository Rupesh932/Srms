package com.srms.model.property;

import com.srms.enums.BathroomType;
import com.srms.enums.PropertyStatus;
import com.srms.model.base.SrmsEntity;

import java.time.LocalDateTime;

public abstract class RentableProperty extends SrmsEntity {

   private String propertyNumber;
   private PropertyStatus propertyStatus ;
   private double baseRent;
   private BathroomType bathroomType;

   public abstract int calculateFixedMandatoryCharge();

   public RentableProperty(String id,String propertyNumber,PropertyStatus propertyStatus,double baseRent,BathroomType bathroomType){
      super(id);
      this.propertyNumber = propertyNumber;
      this.propertyStatus = propertyStatus;
      this.baseRent = baseRent;
      this.bathroomType = bathroomType;

   }


   public boolean isOccupied(){
      return this.propertyStatus == PropertyStatus.OCCUPIED;
   }

   public String getPropertyNumber() {
      return propertyNumber;
   }

   public void setPropertyNumber(String propertyNumber) {
      this.propertyNumber = propertyNumber;
   }

   public PropertyStatus getPropertyStatus() {
      return propertyStatus;
   }

   public void setPropertyStatus(PropertyStatus propertyStatus) {
      this.propertyStatus = propertyStatus;
      this.setUpdatedAt(LocalDateTime.now());
   }

   public double getBaseRent() {
      return baseRent;
   }

   public void setBaseRent(double baseRent) {
      this.baseRent = baseRent;
   }

   public BathroomType getBathroomType() {
      return bathroomType;
   }

   public void setBathroomType(BathroomType bathroomType) {
      this.bathroomType = bathroomType;
   }

   @Override
   public String toString() {
      return "RentableProperty{" +
              super.toString() +
              "propertyNumber='" + propertyNumber + '\'' +
              ", propertyStatus=" + propertyStatus +
              ", baseRent=" + baseRent +
              ", bathroomType=" + bathroomType +
              '}';
   }
}
