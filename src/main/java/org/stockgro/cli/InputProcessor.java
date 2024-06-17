package org.stockgro.cli;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.stockgro.service.IParkingManager;

import java.util.Arrays;
import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class InputProcessor {

    private final IParkingManager parkingManager;
    private static String parkingLotId = "";

    public void processInput(String input) {
        try {
            List<String> inputStrings = Arrays.stream(input.split(" ")).toList();
            if (inputStrings.isEmpty()) {
                throw new IllegalArgumentException("Cannot process empty input");
            }
            switch (inputStrings.get(0)) {
                case "create" -> { // create lot UID CAPACITY
                    if (inputStrings.size() != 4 || !inputStrings.get(1).equals("lot")) {
                        throw new IllegalArgumentException("Invalid input for lot creation: " +  input);
                    }
                    parkingManager.createParkingLot(inputStrings.get(2), Integer.parseInt(inputStrings.get(3)));
                    enterIntoParkingLot(inputStrings.get(2));
                }
                case "enter" -> { // ender UID
                    if (inputStrings.size() != 2) {
                        throw new IllegalArgumentException("Invalid input for entering into parking lot: " + input);
                    }
                    enterIntoParkingLot(inputStrings.get(1));
                }
                case "exit" -> {
                    log.info("Shutting down the application...");
                    System.exit(0);
                }
                case "park" -> {
                    validateParkingLotId();
                    if (inputStrings.size() != 3) {
                        throw new IllegalArgumentException("Invalid input for filling a spot: " + input);
                    }
                    parkingManager.fillParkingSpot(parkingLotId, inputStrings.get(1), inputStrings.get(2));
                }
                case "leave" -> {
                    validateParkingLotId();
                    if (inputStrings.size() != 2) {
                        throw new IllegalArgumentException("Invalid input for emptying a spot: " + input);
                    }
                    parkingManager.unFillParkingSpot(parkingLotId, inputStrings.get(1));
                }
                case "status" -> {
                    validateParkingLotId();
                    if (inputStrings.size() != 1) {
                        throw new IllegalArgumentException("Invalid input for status: {}" + input);
                    }
                    parkingManager.logStatusOfParkingLot(parkingLotId);
                }
                case "color" -> {
                    validateParkingLotId();
                    if (inputStrings.size() != 2) {
                        throw new IllegalArgumentException("Invalid input for status on color: {}" + input);
                    }
                    parkingManager.logStatusOnColor(parkingLotId, inputStrings.get(1));
                }
                case "registration" -> {
                    validateParkingLotId();
                    if (inputStrings.size() != 2) {
                        throw new IllegalArgumentException("Invalid input for status of vehicle: {}" + input);
                    }
                    parkingManager.logStatusOnRegistrationNumber(parkingLotId, inputStrings.get(1));
                }
                default -> throw new IllegalArgumentException("Invalid input: " + input);
            }
        } catch (Exception e) {
            log.error("Input cannot be processed: {}", e.getMessage());
        }
    }

    private void enterIntoParkingLot(String id) {
        parkingLotId = id;
        log.info("Entered into parking lot ID: " + parkingLotId);
    }

    private void validateParkingLotId() {
        if (parkingLotId.isEmpty()) {
            throw new IllegalArgumentException("Please enter into a parking lot");
        }
    }
}
