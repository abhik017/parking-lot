package org.stockgro.model;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ParkingSpot {
    private String id;
    private String parkingLotId;
    private VehicleSizeEnum spotCapacity;
    private boolean isOccupied;
    private Vehicle vehicle;
}
