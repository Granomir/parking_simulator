package com.patrushev.parking_simulator;

import java.util.Objects;

public class Ticket {
    private int id; //думаю диапазон равен от 1 до capacity паркинга

    public Ticket(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Ticket ticket = (Ticket) o;
        return id == ticket.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
