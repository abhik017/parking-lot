package org.stockgro.repository;

import org.stockgro.model.ParkingLot;

import java.util.Optional;

public interface IParkingLotRepository {
    Optional<ParkingLot> fetchParkingLotById(String parkingLotId);

    void saveOrUpdateParkingLot(ParkingLot parkingLot);
}
