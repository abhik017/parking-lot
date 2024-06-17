package org.stockgro.repository.impl;

import org.springframework.stereotype.Repository;
import org.stockgro.model.ParkingSpot;
import org.stockgro.repository.IParkingSpotRepository;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.SortedMap;
import java.util.TreeMap;

@Repository
public class ParkingSpotRepositoryImpl implements IParkingSpotRepository {

    private final HashMap<String, SortedMap<String, ParkingSpot>> parkingLotToSpotMap = new HashMap<>();
    private final HashMap<String, HashMap<String, HashSet<ParkingSpot>>> parkingLotToVehicleColorToSpots = new HashMap<>();
    private final HashMap<String, HashMap<String, ParkingSpot>> parkingLotToRegistrationNumberMap = new HashMap<>();

    @Override
    public Optional<ParkingSpot> fetchByLotIdAndSpotId(String parkingLotId, String parkingSpotId) {
        if (parkingLotToSpotMap.containsKey(parkingLotId)) {
            if (parkingLotToSpotMap.get(parkingLotId).containsKey(parkingSpotId)) {
                return Optional.of(parkingLotToSpotMap.get(parkingLotId).get(parkingSpotId));
            }
        }
        return Optional.empty();
    }

    @Override
    public void saveOrUpdateParkingSpot(ParkingSpot parkingSpot) {
        if (!parkingLotToSpotMap.containsKey(parkingSpot.getParkingLotId())) {
            parkingLotToSpotMap.put(parkingSpot.getParkingLotId(), new TreeMap<>());
        }
        if (!parkingLotToVehicleColorToSpots.containsKey(parkingSpot.getParkingLotId())) {
            parkingLotToVehicleColorToSpots.put(parkingSpot.getParkingLotId(), new HashMap<>());
        }
        if (!parkingLotToRegistrationNumberMap.containsKey(parkingSpot.getParkingLotId())) {
            parkingLotToRegistrationNumberMap.put(parkingSpot.getParkingLotId(), new HashMap<>());
        }
        if (parkingSpot.isOccupied()) {
            if (!parkingLotToVehicleColorToSpots.get(parkingSpot.getParkingLotId()).containsKey(parkingSpot.getVehicle().getColor())) {
                parkingLotToVehicleColorToSpots.get(parkingSpot.getParkingLotId()).put(parkingSpot.getVehicle().getColor(), new HashSet<>());
            }
            parkingLotToVehicleColorToSpots.get(parkingSpot.getParkingLotId()).get(parkingSpot.getVehicle().getColor()).add(parkingSpot);
            parkingLotToRegistrationNumberMap.get(parkingSpot.getParkingLotId()).put(parkingSpot.getVehicle().getRegistrationNumber(), parkingSpot);
        }
        parkingLotToSpotMap.get(parkingSpot.getParkingLotId()).put(parkingSpot.getId(), parkingSpot);
    }

    @Override
    public List<ParkingSpot> fetchByLotId(String parkingLotId) {
        if (parkingLotToSpotMap.containsKey(parkingLotId)) {
            return new ArrayList<>(parkingLotToSpotMap.get(parkingLotId).values());
        }
        return new ArrayList<>();
    }

    @Override
    public List<ParkingSpot> fetchByLotIdAndColor(String parkingLotId, String color) {
        if (parkingLotToVehicleColorToSpots.containsKey(parkingLotId)) {
            if (parkingLotToVehicleColorToSpots.get(parkingLotId).containsKey(color)) {
                return new ArrayList<>(parkingLotToVehicleColorToSpots.get(parkingLotId).get(color));
            }
        }
        return new ArrayList<>();
    }

    @Override
    public Optional<ParkingSpot> fetchByLotIdAndRegistrationNumber(String parkingLotId, String registrationNumber) {
        if (parkingLotToRegistrationNumberMap.containsKey(parkingLotId)) {
            if (parkingLotToRegistrationNumberMap.get(parkingLotId).containsKey(registrationNumber)) {
                return Optional.of(parkingLotToRegistrationNumberMap.get(parkingLotId).get(registrationNumber));
            }
        }
        return Optional.empty();
    }
}
