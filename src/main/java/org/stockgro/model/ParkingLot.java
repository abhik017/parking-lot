package org.stockgro.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.HashMap;
import java.util.SortedSet;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ParkingLot {
    private String id;
    private int totalSpots;
    private HashMap<VehicleSizeEnum, SortedSet<String>> vehicleSizeToOccupiedSpotIds;
    private HashMap<VehicleSizeEnum, SortedSet<String>> vehicleSizeToUnoccupiedSpotIds;
}
