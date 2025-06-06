package org.example.exhibitly.models;

public class Kurator extends Actor {
    private String bidang;

    public Kurator() {
        super();
    }

    public Kurator(int actorID, String username, String password, String name, String role, String bidang) {
        super(actorID, username, password, name, role);
        this.bidang = bidang;
    }

    public String getBidang() {
        return bidang;
    }

    public void setBidang(String bidang) {
        this.bidang = bidang;
    }
}