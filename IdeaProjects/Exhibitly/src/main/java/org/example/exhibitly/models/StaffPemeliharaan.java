package org.example.exhibitly.models;

import java.util.Date;

public class StaffPemeliharaan extends Actor {
    private Date jadwalPemeliharaan;

    public StaffPemeliharaan() {
        super();
    }

    public StaffPemeliharaan(int actorID, String username, String password, String name, String role, Date jadwalPemeliharaan) {
        super(actorID, username, password, name, role);
        this.jadwalPemeliharaan = jadwalPemeliharaan;
    }

    public Date getJadwalPemeliharaan() {
        return jadwalPemeliharaan;
    }

    public void setJadwalPemeliharaan(Date jadwalPemeliharaan) {
        this.jadwalPemeliharaan = jadwalPemeliharaan;
    }
}