package org.stockgro.util;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.stockgro.model.ParkingLot;
import org.stockgro.model.ParkingSpot;
import org.stockgro.model.VehicleSizeEnum;

import java.util.Optional;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ServiceRequestValidator {

    public static void validateLotCreationRequest(Optional<ParkingLot> parkingLotOp, int capacity) {
        if (capacity <= 0) {
            throw new IllegalArgumentException("Capacity must be greater than 0");
        }
        if (parkingLotOp.isPresent()) {
            throw new IllegalArgumentException("Parking lot with given ID already exists");
        }
    }

    public static void validateSpotFillingRequest(Optional<ParkingLot> parkingLotOp, Optional<ParkingSpot> parkingSpotOp, VehicleSizeEnum vehicleSize) {
        if (parkingLotOp.isEmpty()) {
            throw new IllegalArgumentException("Parking lot with given ID does not exist");
        }
        if (parkingSpotOp.isPresent()) {
            throw new IllegalArgumentException("Registration number already exists");
        }
        ParkingLot parkingLot = parkingLotOp.get();
        if (parkingLot.getVehicleSizeToUnoccupiedSpotIds().get(vehicleSize).isEmpty()) {
            throw new RuntimeException("Parking is full, cannot park the vehicle");
        }
    }

    public static void validateSpotUnFillingRequest(Optional<ParkingLot> parkingLotOp, Optional<ParkingSpot> parkingSpotOp) {
        if (parkingLotOp.isEmpty()) {
            throw new IllegalArgumentException("Parking lot with given ID does not exist");
        }
        if (parkingSpotOp.isEmpty()) {
            throw new IllegalArgumentException("Parking spot with given ID does not exist");
        }
    }

    public static void validateStatusRequest(Optional<ParkingLot> parkingLot) {
        if (parkingLot.isEmpty()) {
            throw new IllegalArgumentException("Parking lot with given ID does not exist");
        }
    }
}
