package com.patrushev.parking_simulator;

public class Car {
    private String number;
    private Ticket ticket;


    public void setTicketId(Ticket ticket) {
        this.ticket = ticket;
    }

    public Ticket getTicket() {
        return ticket;
    }
}
