package org.stockgro.repository;

import org.stockgro.model.ParkingSpot;

import java.util.List;
import java.util.Optional;

public interface IParkingSpotRepository {
    Optional<ParkingSpot> fetchByLotIdAndSpotId(String parkingLotId, String parkingSpotId);

    void saveOrUpdateParkingSpot(ParkingSpot parkingSpot);

    List<ParkingSpot> fetchByLotId(String parkingLotId);

    List<String> fetchRegistrationNosByLotIdAndColor(String parkingLotId, String color);

    Optional<ParkingSpot> fetchByLotIdAndRegistrationNumber(String parkingLotId, String registrationNumber);

    void removeRegistrationNumberFromColor(String parkingLotId, String registrationNumber, String color);

    void removeSpotFromRegistrationNumber(String parkingLotId, String registrationNumber);

    Optional<ParkingSpot> fetchByRegistrationNumber(String parkingLotId, String registrationNumber);
}
