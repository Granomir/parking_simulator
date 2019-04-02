package com.patrushev.parking_simulator;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Main {
    private static Logger logger = LoggerFactory.getLogger(Main.class);

    private static List<String> usedCarNumbers = new ArrayList<>();

    public static void main(String[] args) throws IOException {
        Parking parking = new Parking(5, 1000);
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        while (true) {
            System.out.println("Введите команду:");
            String command = br.readLine();
            if (command.startsWith("p")) {
                logger.info("вызвана команда для парковки машины");
                int carQuantity = Integer.parseInt(command.substring(2));
                for (int i = 0; i < carQuantity; i++) {
                    new Thread(() -> parking.park(new Car(getUniqueRandomCarNumber()))).start();
                }
            } else if (command.startsWith("u") && command.substring(2).startsWith("[")) {
                String[] ticketNumbers = command.substring(3, command.length() - 1).split(",");
                for (String ticketNumber : ticketNumbers) {
                    int tempTicketNumber = Integer.parseInt(ticketNumber);
                    new Thread(() -> parking.unpark(tempTicketNumber)).start();
                }
            } else if (command.startsWith("u")) {
                int tempTicketNumber = Integer.parseInt(command.substring(2));
                System.out.println("распарковка машины №" + tempTicketNumber);
                new Thread(() -> parking.unpark(tempTicketNumber)).start();
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

    private static String getUniqueRandomCarNumber() {
        byte[] array = new byte[6];
        new Random().nextBytes(array);
        String carNumber = new String(array, Charset.forName("UTF-8"));
        if (usedCarNumbers.contains(carNumber)) {
            carNumber = getUniqueRandomCarNumber();
        }
        usedCarNumbers.add(carNumber);
        return carNumber;
    }
}
