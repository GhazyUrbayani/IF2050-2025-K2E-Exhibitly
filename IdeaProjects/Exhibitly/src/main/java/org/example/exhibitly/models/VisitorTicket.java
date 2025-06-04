package org.example.exhibitly.models;

public class VisitorTicket {
    private int ticketNumber;
    private String name;
    private String email;
    private int ticketQuantity;

    public VisitorTicket() {
    }

    public VisitorTicket(int ticketNumber, String name, String email, int ticketQuantity) {
        this.ticketNumber = ticketNumber;
        this.name = name;
        this.email = email;
        this.ticketQuantity = ticketQuantity;
    }

    public int getTicketNumber() {
        return ticketNumber;
    }

    public void setTicketNumber(int ticketNumber) {
        this.ticketNumber = ticketNumber;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public int getTicketQuantity() {
        return ticketQuantity;
    }

    public void setTicketQuantity(int ticketQuantity) {
        this.ticketQuantity = ticketQuantity;
    }
}