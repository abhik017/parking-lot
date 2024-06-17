package org.stockgro.repository;

import org.stockgro.model.ParkingSpot;

import java.util.List;
import java.util.Optional;

public interface IParkingSpotRepository {
    Optional<ParkingSpot> fetchByLotIdAndSpotId(String parkingLotId, String parkingSpotId);

    void saveOrUpdateParkingSpot(ParkingSpot parkingSpot);

    List<ParkingSpot> fetchByLotId(String parkingLotId);

    List<ParkingSpot> fetchByLotIdAndColor(String parkingLotId, String color);

    Optional<ParkingSpot> fetchByLotIdAndRegistrationNumber(String parkingLotId, String registrationNumber);
}
