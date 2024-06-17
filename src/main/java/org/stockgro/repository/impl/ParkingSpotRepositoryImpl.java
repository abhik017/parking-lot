package org.stockgro.repository.impl;

import org.springframework.stereotype.Repository;
import org.stockgro.model.ParkingSpot;
import org.stockgro.repository.IParkingSpotRepository;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.SortedMap;
import java.util.TreeMap;

@Repository
public class ParkingSpotRepositoryImpl implements IParkingSpotRepository {

    private final HashMap<String, SortedMap<String, ParkingSpot>> parkingLotToSpotMap = new HashMap<>();
    private final HashMap<String, HashMap<String, HashSet<String>>> parkingLotToColorToRegistrationNoMap = new HashMap<>();
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
        if (!parkingLotToColorToRegistrationNoMap.containsKey(parkingSpot.getParkingLotId())) {
            parkingLotToColorToRegistrationNoMap.put(parkingSpot.getParkingLotId(), new HashMap<>());
        }
        if (!parkingLotToRegistrationNumberMap.containsKey(parkingSpot.getParkingLotId())) {
            parkingLotToRegistrationNumberMap.put(parkingSpot.getParkingLotId(), new HashMap<>());
        }
        if (parkingSpot.isOccupied()) {
            if (!parkingLotToColorToRegistrationNoMap.get(parkingSpot.getParkingLotId()).containsKey(parkingSpot.getVehicle().getColor())) {
                parkingLotToColorToRegistrationNoMap.get(parkingSpot.getParkingLotId()).put(parkingSpot.getVehicle().getColor(), new HashSet<>());
            }
            parkingLotToColorToRegistrationNoMap.get(parkingSpot.getParkingLotId()).get(parkingSpot.getVehicle().getColor()).add(parkingSpot.getVehicle().getRegistrationNumber());
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
    public List<String> fetchRegistrationNosByLotIdAndColor(String parkingLotId, String color) {
        if (parkingLotToColorToRegistrationNoMap.containsKey(parkingLotId)) {
            if (parkingLotToColorToRegistrationNoMap.get(parkingLotId).containsKey(color)) {
                return new ArrayList<>(parkingLotToColorToRegistrationNoMap.get(parkingLotId).get(color));
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

    @Override
    public Optional<ParkingSpot> fetchByRegistrationNumber(String parkingLotId, String registrationNumber) {
        if (parkingLotToRegistrationNumberMap.containsKey(parkingLotId)) {
            if (parkingLotToRegistrationNumberMap.get(parkingLotId).containsKey(registrationNumber)) {
                return Optional.of(parkingLotToRegistrationNumberMap.get(parkingLotId).get(registrationNumber));
            }
        }
        return Optional.empty();
    }

    @Override
    public void removeSpotFromRegistrationNumber(String parkingLotId, String registrationNumber) {
        parkingLotToRegistrationNumberMap.get(parkingLotId).remove(registrationNumber);
    }

    @Override
    public void removeRegistrationNumberFromColor(String parkingLotId, String registrationNumber, String color) {
        parkingLotToColorToRegistrationNoMap.get(parkingLotId).get(color).remove(registrationNumber);
    }
}
