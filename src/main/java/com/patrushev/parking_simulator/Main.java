package com.patrushev.parking_simulator;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Main {
    private static Logger logger = LoggerFactory.getLogger(Main.class);

    private static ExecutorService executorService = Executors.newFixedThreadPool(2);

    private static List<String> usedCarNumbers = new ArrayList<>();

    public static void main(String[] args) throws IOException {
        Parking parking = new Parking(5, 3000);
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        while (true) {
            System.out.println("Введите команду:");
            String command = br.readLine();
            if (command.startsWith("p")) {
                parkCars(parking, command);
            } else if (command.startsWith("u") && command.substring(2).startsWith("[")) {
                unparkCars(parking, command);
            } else if (command.startsWith("u")) {
                unparkCar(parking, command);
            } else if (command.equals("l")) {
                parking.carsList();
            } else if (command.equals("c")) {
                System.out.println("Количество свободных мест на парковке - " + parking.getFreeSpace());
            } else if (command.equals("e")) {
                System.exit(0);
            } else {
                System.out.println("Введена неизвестная команда");
            }
        }
    }

    private static void parkCars(Parking parking, String command) {
        logger.info("вызвана команда для парковки машины");
        int carQuantity = Integer.parseInt(command.substring(2));
        for (int i = 0; i < carQuantity; i++) {
            String carNumber = getUniqueRandomCarNumber();
            executorService.execute(() -> parking.park(new Car(carNumber)));
        }
    }

    private static void unparkCars(Parking parking, String command) {
        String[] ticketNumbers = command.substring(3, command.length() - 1).split(",");
        for (String ticketNumber : ticketNumbers) {
            int tempTicketNumber = Integer.parseInt(ticketNumber);
            executorService.execute(() -> parking.unpark(tempTicketNumber));
        }
    }

    private static void unparkCar(Parking parking, String command) {
        int tempTicketNumber = Integer.parseInt(command.substring(2));
        System.out.println("распарковка машины №" + tempTicketNumber);
        executorService.execute(() -> parking.unpark(tempTicketNumber));
    }

    private static String getUniqueRandomCarNumber() {
        int wordLeftLimit = 1072;
        int wordRightLimit = 1103;
        int digitLeftLimit = 48;
        int digitRightLimit = 57;
        int targetStringLength = 6;
        Random random = new Random();
        StringBuilder buffer = new StringBuilder(targetStringLength);
        for (int i = 0; i < targetStringLength; i++) {
            if (i == 0 || i == 4 || i == 5) {
                int randomLimitedInt = wordLeftLimit + (int)
                        (random.nextFloat() * (wordRightLimit - wordLeftLimit + 1));
                buffer.append((char) randomLimitedInt);
            } else {
                int randomLimitedInt = digitLeftLimit + (int)
                        (random.nextFloat() * (digitRightLimit - digitLeftLimit + 1));
                buffer.append((char) randomLimitedInt);
            }
        }
        String carNumber = buffer.toString();
        if (usedCarNumbers.contains(carNumber)) {
            carNumber = getUniqueRandomCarNumber();
        }
        usedCarNumbers.add(carNumber);
        return carNumber;
    }
}
