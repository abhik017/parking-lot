package org.stockgro.model;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public enum VehicleSizeEnum {
    SMALL("Hatchback"),
    MEDIUM("Sedan and SUVs"),
    LARGE("Trucks and Buses");

    private String description;
}
