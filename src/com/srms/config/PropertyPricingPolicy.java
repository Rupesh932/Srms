package com.srms.config;

import com.srms.enums.BathroomType;
import com.srms.enums.RoomType;


public class PropertyPricingPolicy {
    public static final int BASE_WATER_CHARGE = 100;
    public static final int BASE_ELECTRICITY_CHARGE = 100;
    public static final int BASE_GARBAGE_CHARGE = 100;

    //room charge
    public static int getWaterChargeRoom(BathroomType bathroomType, RoomType roomType) {
        return roomType == RoomType.DELUXE_ROOM ? BASE_WATER_CHARGE + 300 :
                bathroomType == BathroomType.ATTACHED ? BASE_WATER_CHARGE + 100 :
                BASE_WATER_CHARGE;
    }

    public static int getGarbageChargeRoom(RoomType roomType) {
        return switch (roomType) {
            case OFFICE, SINGLE_ROOM -> BASE_GARBAGE_CHARGE;
            case HALL, SHUTTER, DOUBLE_ROOM -> BASE_GARBAGE_CHARGE + 100;
            case DELUXE_ROOM -> BASE_GARBAGE_CHARGE + 200;
        };
    }
    public static int getElectricityCharge(int roomCount){

        return BASE_ELECTRICITY_CHARGE + (50 * roomCount);
    }

    //flat charge
    public static int getWaterChargeFlat(int roomCount) {

        return BASE_WATER_CHARGE + (roomCount * 50);
    }

    public static int getGarbageChargeFlat(int roomCount) {

        return BASE_GARBAGE_CHARGE + (roomCount * 50);
    }



}
