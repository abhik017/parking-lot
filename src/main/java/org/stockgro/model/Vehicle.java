package org.stockgro.model;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class Vehicle {
    private String registrationNumber;
    private String color;
}
