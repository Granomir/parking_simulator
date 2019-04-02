package com.patrushev.parking_simulator;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;

public class Parking {
    private Logger logger = LoggerFactory.getLogger(Parking.class);

    //общее кол-во мест
    private int capacity;
    //порядковый номер последней заехавшей машины
    private int ordinalNumberOfLastCar = 0;
    //реестр припаркованных машин
    private Map<Integer, Car> parkedCars;  //номер по порядку въезда на парковку + объект машины
    //список свободных билетов
    private List<Ticket> freeTickets;
    //список выданных билетов
    private List<Ticket> busyTickets;
    //длительность въезда/выезда
    private int duration;

    public Parking(int capacity, int duration) {
        this.duration = duration;
        parkedCars = new HashMap<>();
        this.capacity = capacity;
        busyTickets = new ArrayList<>();
        freeTickets = new LinkedList<>();
        for (int i = 1; i <= capacity; i++) {
            freeTickets.add(new Ticket(i));
        }
        logger.info("Создана парковка на {} мест", capacity);
    }

    public synchronized void park(Car car) {
        logger.info("Машина с госномером {} собирается припарковаться", car.getNumber());
        if (parkedCars.size() < capacity) {
            try {
                Thread.sleep(duration);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            Ticket busyTicket = freeTickets.remove(0);
            car.setTicketId(busyTicket);
            busyTickets.add(busyTicket);
            parkedCars.put(++ordinalNumberOfLastCar, car);
            logger.info("На парковке есть место. Машина получила въездной билет с id - {}. Порядковый номер машины на парковке - {}", busyTicket.getId(), ordinalNumberOfLastCar);
            logger.info("Теперь на парковке {} свободных мест", getFreeSpace());
        } else {
            logger.info("На парковке закончилось место - машина с госномером {} не попадает на парковку", car.getNumber());
        }
    }

    public synchronized void unpark(int ticketId) {
        logger.info("Машина с билетом с id - {} собирается покинуть парковку", ticketId);
        for (Ticket busyTicket : busyTickets) {
            if (busyTicket.getId() == ticketId) {
                try {
                    Thread.sleep(duration);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                parkedCars.entrySet().removeIf(entry -> entry.getValue().getTicket().getId() == ticketId);
                freeTickets.add(busyTicket);
                busyTickets.remove(busyTicket);
                logger.info("Машина с билетом с id - {} вернула билет и покинула парковку", ticketId);
                logger.info("Теперь на парковке {} свободных мест", getFreeSpace());
                return;
            }
        }
        System.out.printf("Билет с номером %d еще не использован.", ticketId);
    }

    public void carsList() {
        if (parkedCars.size() > 0) {
            System.out.println("В данный момент на парковке находятся автомобили:");
            for (Map.Entry<Integer, Car> entry : parkedCars.entrySet()) {
                System.out.println("авто №" + entry.getKey() + " с билетом №" + entry.getValue().getTicket().getId());
            }
        } else {
            System.out.println("Парковка пуста");
        }
    }

    public int getFreeSpace() {
        return freeTickets.size();
    }
}
