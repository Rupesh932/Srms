package com.srms.model.property;

import com.srms.config.PropertyPricingPolicy;
import com.srms.enums.BathroomType;
import com.srms.enums.FloorLevel;
import com.srms.enums.PropertyStatus;

import java.util.ArrayList;
import java.util.List;

public class Flat extends RentableProperty {
    private final FloorLevel floorLevel;
    private List<Room> roomList;

    public Flat(String id, String propertyNumber, PropertyStatus propertyStatus, double baseRent,
                BathroomType bathroomType, FloorLevel floorLevel, List<Room> roomList) {
        super(id, propertyNumber, propertyStatus, baseRent, bathroomType);
        this.floorLevel = floorLevel;
        this.roomList = roomList;

        // sets parent flat to each room
        if(roomList != null){
            for(Room room : this.roomList){
                if(room != null){
                    room.setParentFlat(this);
                }
            }
        }
    }

    public FloorLevel getFloorLevel() {
        return floorLevel;
    }

    public List<Room> getRoomList() {
        return roomList == null? new ArrayList<>():new ArrayList<>(this.roomList);
    }


    // add room to flat and sets parent flat to that room
    public void addRoom(Room room){

        if(room == null){
            throw new IllegalArgumentException("Room cannot be null");
        }
        if(this.roomList == null){
            this.roomList = new ArrayList<>();
        }
        if(this.roomList.contains(room)){
            throw new IllegalStateException("Room already existed in flat");
        }

        this.roomList.add(room);
        room.setParentFlat(this);

    }
    // remove room from flat and sets parent flat to that room to null
    public void removeRoom(Room room){
        if(room == null){
            throw new IllegalArgumentException("Room cannot be null");
        }
        if(this.roomList == null || this.roomList.isEmpty()){
            throw new IllegalStateException("No rooms exist in this flat to remove.");
        }
        if(!this.roomList.contains(room)){
            throw new IllegalStateException("Room does not belong to this flat.");
        }

        this.roomList.remove(room);
        room.setParentFlat(null);
    }



    @Override
    public int calculateFixedMandatoryCharge() {
        int roomCount = (this.roomList != null) ? this.roomList.size(): 0 ;
        int water = PropertyPricingPolicy.getWaterChargeFlat(roomCount);
        int electricity = PropertyPricingPolicy.getElectricityCharge(roomCount);
        int garbage = PropertyPricingPolicy.getGarbageChargeFlat(roomCount);

        return water + electricity + garbage;
    }

    @Override
    public String toString() {
        int roomCount = (this.roomList != null) ? this.roomList.size(): 0 ;
        return "Flat{" +
                super.toString() +
                "floorLevel=" + floorLevel +
                ", room count=" + roomCount+
                '}';
    }
}
