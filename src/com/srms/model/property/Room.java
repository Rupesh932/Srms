package com.srms.model.property;

import com.srms.config.PropertyPricingPolicy;
import com.srms.enums.BathroomType;
import com.srms.enums.FloorLevel;
import com.srms.enums.PropertyStatus;
import com.srms.enums.RoomType;

public class Room extends RentableProperty{

    private RoomType roomType ;
    private final FloorLevel floorLevel;
    private Flat parentFlat;

    public Room(String id, String propertyNumber, PropertyStatus propertyStatus, double baseRent,
                BathroomType bathroomType, RoomType roomType,FloorLevel floorLevel){
        super(id,propertyNumber,propertyStatus,baseRent,bathroomType);
        this.roomType = roomType;
        this.floorLevel = floorLevel;
        this.parentFlat = null;
    }


    public RoomType getRoomType() {
        return roomType;
    }

    public void setRoomType(RoomType roomType) {
        this.roomType = roomType;
    }

    public FloorLevel getFloorLevel() {
        return floorLevel;
    }



    public Flat getParentFlat() {
        return parentFlat;
    }

    public void setParentFlat(Flat parentFlat) {
        this.parentFlat = parentFlat;
    }

    @Override
    public int calculateFixedMandatoryCharge() {
        int water = PropertyPricingPolicy.getWaterChargeRoom(this.getBathroomType(),this.roomType);
        int electricity = PropertyPricingPolicy.getElectricityCharge(1);
        int garbage = PropertyPricingPolicy.getGarbageChargeRoom(this.roomType);
        return water + electricity + garbage;
    }

    @Override
    public String toString() {
        return "Room{" +
                super.toString()+
                "roomType=" + roomType +
                ", floorLevel=" + floorLevel +
                '}';
    }
}
