// src/main/java/org/example/exhibitly/model/Ticket.java
package org.example.exhibitly.models;

import java.util.Date;

public class Ticket {
    private String ticketID;
    private String customerName;
    private String customerEmail;
    private int quantity;
    private Date purchaseDate;

    public Ticket(String ticketID, String customerName, String customerEmail, int quantity, Date purchaseDate) {
        this.ticketID = ticketID;
        this.customerName = customerName;
        this.customerEmail = customerEmail;
        this.quantity = quantity;
        this.purchaseDate = purchaseDate;
    }

    // Getters
    public String getTicketID() { return ticketID; }
    public String getCustomerName() { return customerName; }
    public String getCustomerEmail() { return customerEmail; }
    public int getQuantity() { return quantity; }
    public Date getPurchaseDate() { return purchaseDate; }

    // Setters (jika diperlukan)
    public void setTicketID(String ticketID) { this.ticketID = ticketID; }
    public void setCustomerName(String customerName) { this.customerName = customerName; }
    public void setCustomerEmail(String customerEmail) { this.customerEmail = customerEmail; }
    public void setQuantity(int quantity) { this.quantity = quantity; }
    public void setPurchaseDate(Date purchaseDate) { this.purchaseDate = purchaseDate; }
}