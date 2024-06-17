package org.stockgro.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.stockgro.model.ParkingLot;
import org.stockgro.model.ParkingSpot;
import org.stockgro.model.Vehicle;
import org.stockgro.model.VehicleSizeEnum;
import org.stockgro.repository.IParkingLotRepository;
import org.stockgro.repository.IParkingSpotRepository;
import org.stockgro.service.IParkingManager;
import org.stockgro.util.ParkingManagerHelper;
import org.stockgro.util.ServiceRequestValidator;

import java.util.HashMap;
import java.util.List;
import java.util.Optional;
import java.util.SortedSet;
import java.util.TreeSet;
import java.util.stream.IntStream;

@Slf4j
@Service
@RequiredArgsConstructor
public class ParkingManager implements IParkingManager {
    private static final String UNDERSCORE = "_";
    private final IParkingLotRepository parkingLotRepository;
    private final IParkingSpotRepository parkingSpotRepository;

    @Override
    public void createParkingLot(String parkingLotId, int capacity) {
        try {
            Optional<ParkingLot> parkingLotOp = parkingLotRepository.fetchParkingLotById(parkingLotId);
            ServiceRequestValidator.validateLotCreationRequest(parkingLotOp, capacity);
            ParkingLot parkingLot = ParkingLot.builder()
                    .id(parkingLotId)
                    .totalSpots(capacity)
                    .build();
            SortedSet<String> parkingSpotIds = new TreeSet<>();
            SortedSet<String> occupiedSpotIds = new TreeSet<>();
            IntStream.range(1, capacity + 1).forEach(i -> {
                ParkingSpot parkingSpot = ParkingSpot.builder()
                        .id(parkingLotId + UNDERSCORE + String.valueOf(i))
                        .parkingLotId(parkingLotId)
                        .spotCapacity(VehicleSizeEnum.MEDIUM) // HARDCODED MEDIUM AS PER CURRENT REQUIREMENT
                        .build();
                parkingSpotRepository.saveOrUpdateParkingSpot(parkingSpot);
                parkingSpotIds.add(parkingSpot.getId());
            });
            parkingLot.setVehicleSizeToUnoccupiedSpotIds(new HashMap<>());
            parkingLot.getVehicleSizeToUnoccupiedSpotIds().put(VehicleSizeEnum.MEDIUM, parkingSpotIds); // HARDCODED MEDIUM AS PER CURRENT REQUIREMENT
            parkingLot.setVehicleSizeToOccupiedSpotIds(new HashMap<>());
            parkingLot.getVehicleSizeToOccupiedSpotIds().put(VehicleSizeEnum.MEDIUM, occupiedSpotIds); // HARDCODED MEDIUM AS PER CURRENT REQUIREMENT
            parkingLotRepository.saveOrUpdateParkingLot(parkingLot);
            log.info("Created parking lot {}", parkingLot.getId());
        } catch (Exception ex) {
            log.error(ex.getMessage());
        }
    }

    @Override
    public void fillParkingSpot(String parkingLotId, String registrationNumber, String color) {
        // NO NEED TO TAKE LOCK AS PARKING ENTRY MUST BE RESTRICTED WITH BOOM BARRIER
        try {
            Optional<ParkingLot> parkingLotOp = parkingLotRepository.fetchParkingLotById(parkingLotId);
            Optional<ParkingSpot> parkingSpotByRegNoOp = parkingSpotRepository.fetchByRegistrationNumber(parkingLotId, registrationNumber);
            ServiceRequestValidator.validateSpotFillingRequest(parkingLotOp, parkingSpotByRegNoOp, VehicleSizeEnum.MEDIUM); // HARDCODED
            ParkingLot parkingLot = parkingLotOp.get();

            String parkingSpotId = ParkingManagerHelper.getEmptySpotAndMarkOccupied(parkingLot, VehicleSizeEnum.MEDIUM); // HARDCODED MEDIUM AS PER CURRENT REQUIREMENT

            Optional<ParkingSpot> parkingSpotOp = parkingSpotRepository.fetchByLotIdAndSpotId(parkingLot.getId(), parkingSpotId);
            ParkingSpot parkingSpot = parkingSpotOp.get();

            parkingSpot.setOccupied(true);
            parkingSpot.setVehicle(Vehicle.builder()
                .registrationNumber(registrationNumber)
                .color(color)
                .build());

            parkingSpotRepository.saveOrUpdateParkingSpot(parkingSpot);
            parkingLotRepository.saveOrUpdateParkingLot(parkingLot);

            log.info("Parked at {}", parkingSpot.getId());
        } catch (Exception ex) {
            log.error(ex.getMessage());
        }
    }

    @Override
    public void unFillParkingSpot(String parkingLotId, String parkingSpotId) {
        try {
            Optional<ParkingLot> parkingLotOp = parkingLotRepository.fetchParkingLotById(parkingLotId);
            Optional<ParkingSpot> parkingSpotOp = parkingSpotRepository.fetchByLotIdAndSpotId(parkingLotId, parkingSpotId);

            ServiceRequestValidator.validateSpotUnFillingRequest(parkingLotOp, parkingSpotOp);

            ParkingLot parkingLot = parkingLotOp.get();
            ParkingSpot parkingSpot = parkingSpotOp.get();
            Vehicle vehicleToBeRemoved = parkingSpot.getVehicle();

            parkingSpot.setOccupied(false);
            parkingSpot.setVehicle(null);
            ParkingManagerHelper.markOccupiedSpotUnoccupied(parkingLot, parkingSpotId, VehicleSizeEnum.MEDIUM); // HARDCODED MEDIUM AS PER CURRENT REQUIREMENT

            parkingSpotRepository.removeSpotFromRegistrationNumber(parkingLotId, vehicleToBeRemoved.getRegistrationNumber());
            parkingSpotRepository.removeRegistrationNumberFromColor(parkingLotId, vehicleToBeRemoved.getRegistrationNumber(), vehicleToBeRemoved.getColor());
            parkingSpotRepository.saveOrUpdateParkingSpot(parkingSpot);
            parkingLotRepository.saveOrUpdateParkingLot(parkingLot);

            log.info("Car left {}", vehicleToBeRemoved.getRegistrationNumber());
        } catch (Exception ex) {
            log.error(ex.getMessage());
        }
    }

    @Override
    public void logStatusOfParkingLot(String parkingLotId) {
        try {
            Optional<ParkingLot> parkingLotOp = parkingLotRepository.fetchParkingLotById(parkingLotId);
            ServiceRequestValidator.validateStatusRequest(parkingLotOp);

            List<ParkingSpot> parkingSpots = parkingSpotRepository.fetchByLotId(parkingLotId);

            parkingSpots.forEach(spot -> {
                log.info("{} -> {}", spot.getId(), spot.isOccupied() ? spot.getVehicle().getRegistrationNumber() + " Car parked"
                        : "Empty");
            });

        } catch (Exception ex) {
            log.error(ex.getMessage());
        }
    }

    @Override
    public void logStatusOnColor(String parkingLotId, String color) {
        try {
            Optional<ParkingLot> parkingLotOp = parkingLotRepository.fetchParkingLotById(parkingLotId);
            ServiceRequestValidator.validateStatusRequest(parkingLotOp);

            List<String> parkingSpots = parkingSpotRepository.fetchRegistrationNosByLotIdAndColor(parkingLotId, color);
            if (parkingSpots.isEmpty()) {
                log.info("No cars with {} color are present", color);
                return ;
            }
            log.info("Cars with {} color: ", color);

            parkingSpots.forEach(registrationNumber -> {
                log.info("{}", registrationNumber);
            });
        } catch (Exception ex) {
            log.error(ex.getMessage());
        }
    }

    @Override
    public void logStatusOnRegistrationNumber(String parkingLotId, String registrationNumber) {
        try {
            Optional<ParkingLot> parkingLotOp = parkingLotRepository.fetchParkingLotById(parkingLotId);
            ServiceRequestValidator.validateStatusRequest(parkingLotOp);

            Optional<ParkingSpot> parkingSpotOp = parkingSpotRepository.fetchByLotIdAndRegistrationNumber(parkingLotId, registrationNumber);

            parkingSpotOp.ifPresentOrElse(parkingSpot -> {
                log.info("Car {} is parked at slot {}", registrationNumber, parkingSpot.getId());
            }, () -> {
                log.info("No car with registration number {} is found", registrationNumber);
            });
        } catch (Exception ex) {
            log.error(ex.getMessage());
        }
    }
}
