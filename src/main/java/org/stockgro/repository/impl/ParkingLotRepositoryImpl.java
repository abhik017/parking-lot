package org.stockgro.repository.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;
import org.stockgro.model.ParkingLot;
import org.stockgro.repository.IParkingLotRepository;

import java.util.HashMap;
import java.util.Optional;

@Slf4j
@Repository
public class ParkingLotRepositoryImpl implements IParkingLotRepository {
    private final HashMap<String, ParkingLot> parkingLots = new HashMap<>();

    @Override
    public Optional<ParkingLot> fetchParkingLotById(String parkingLotId) {
        if (parkingLots.containsKey(parkingLotId)) {
            return Optional.of(parkingLots.get(parkingLotId));
        }
        return Optional.empty();
    }

    @Override
    public void saveOrUpdateParkingLot(ParkingLot parkingLot) {
        parkingLots.put(parkingLot.getId(), parkingLot);
    }

}
