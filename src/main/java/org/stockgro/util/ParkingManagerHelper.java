package org.stockgro.util;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.stockgro.model.ParkingLot;
import org.stockgro.model.VehicleSizeEnum;

import java.util.HashSet;
import java.util.Iterator;
import java.util.SortedSet;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ParkingManagerHelper {
    public static String getEmptySpotAndMarkOccupied(ParkingLot parkingLot, VehicleSizeEnum vehicleSize) {
        SortedSet<String> unoccupiedParkingSpotIds = parkingLot.getVehicleSizeToUnoccupiedSpotIds().get(vehicleSize);
        SortedSet<String> occupiedParkingSpotIds = parkingLot.getVehicleSizeToOccupiedSpotIds().get(vehicleSize);
        Iterator<String> iterator = unoccupiedParkingSpotIds.iterator();
        String nextParkingSpotId = iterator.next();
        unoccupiedParkingSpotIds.remove(nextParkingSpotId);
        occupiedParkingSpotIds.add(nextParkingSpotId);
        return nextParkingSpotId;
    }

    public static void markOccupiedSpotUnoccupied(ParkingLot parkingLot, String parkingSpotId, VehicleSizeEnum vehicleSize) {
        SortedSet<String> unoccupiedParkingSpotIds = parkingLot.getVehicleSizeToUnoccupiedSpotIds().get(vehicleSize);
        SortedSet<String> occupiedParkingSpotIds = parkingLot.getVehicleSizeToOccupiedSpotIds().get(vehicleSize);
        occupiedParkingSpotIds.remove(parkingSpotId);
        unoccupiedParkingSpotIds.add(parkingSpotId);
    }
}
