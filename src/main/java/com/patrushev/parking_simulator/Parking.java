package com.patrushev.parking_simulator;

import java.util.*;

public class Parking {
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
    //очередь ожидающих въезда машин
    private List<Car> waitingCars;

    public Parking(int capacity) {
        parkedCars = new HashMap<>();
        this.capacity = capacity;
        busyTickets = new ArrayList<>();
        waitingCars = new ArrayList<>();
        freeTickets = new LinkedList<>();
        for (int i = 1; i <= capacity; i++) {
             freeTickets.add(new Ticket(i));
        }
    }

    public void park(Car car) {
        if (parkedCars.size() < capacity) {
            Ticket busyTicket = freeTickets.remove(0);
            car.setTicketId(busyTicket);
            busyTickets.add(busyTicket);
            parkedCars.put(++ordinalNumberOfLastCar, car);
        } else {
            waitingCars.add(car);
        }
    }

    public void unpark(int ticketId) {
        if (busyTickets.contains(new Ticket(ticketId))) {
            parkedCars.entrySet().removeIf(entry -> entry.getValue().getTicket().getId() == ticketId);
            if (waitingCars.size() > 0) {
                for (Car waitingCar : waitingCars) { //нифига так нельзя наверно, потому что каждый въезд/выезд должен быть в отдельном потоке - или прям здесь и запускать эти потоки
                    park(waitingCar);
                }
            }
        } else if (freeTickets.contains(new Ticket(ticketId))) {
            System.out.printf("Билет с номером %d еще не использован.", ticketId);
        } else {
            System.out.printf("Билета с номером %d не существует.", ticketId);
        }
    }

    public void unpark(int[] ticketsId) {
        for (int ticketId : ticketsId) {
            if (busyTickets.contains(new Ticket(ticketId))) {
                parkedCars.entrySet().removeIf(entry -> entry.getValue().getTicket().getId() == ticketId);
                if (waitingCars.size() > 0) { //наверно в этом методе это лучше вынести после цикла for - чтоб новые тачки заезжали только после того как выйдут все
                    for (Car waitingCar : waitingCars) { //нифига так нельзя наверно, потому что каждый въезд/выезд должен быть в отдельном потоке - или прям здесь и запускать эти потоки
                        park(waitingCar);
                    }
                }
            } else if (freeTickets.contains(new Ticket(ticketId))) {
                System.out.printf("Билет с номером %d еще не использован.", ticketId);
            } else {
                System.out.printf("Билета с номером %d не существует.", ticketId);
            }
        }
    }

    public Map<Integer, Car> carsList() {
        return parkedCars;
    }

    public int getFreeSpace() {
        return freeTickets.size();
    }
}
