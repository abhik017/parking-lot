package org.stockgro.service;

public interface IParkingManager {
    void createParkingLot(String parkingLotId, int capacity);

    void fillParkingSpot(String parkingLotId, String registrationNumber, String color);

    void unFillParkingSpot(String parkingId, String parkingSpotId);

    void logStatusOfParkingLot(String parkingLotId);

    void logStatusOnColor(String parkingLotId, String color);

    void logStatusOnRegistrationNumber(String parkingLotId, String registrationNumber);
}
