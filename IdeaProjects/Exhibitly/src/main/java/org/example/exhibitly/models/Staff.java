package org.example.exhibitly.models;

// Tidak perlu lagi import java.util.Date jika jadwalPemeliharaan menjadi String
// import java.util.Date;

public class Staff extends Actor {
    private String jadwalPemeliharaan; // Diubah menjadi String

    public Staff() {
        super();
    }

    public Staff(int actorID, String username, String password, String name, String jadwalPemeliharaan) {
        super(actorID, username, password, name, "Staff`");
        this.jadwalPemeliharaan = jadwalPemeliharaan;
    }

    // --- Getter ---
    public String getJadwalPemeliharaan() { // Perbarui tipe return
        return jadwalPemeliharaan;
    }

    // --- Setter ---
    public void setJadwalPemeliharaan(String jadwalPemeliharaan) { // Perbarui tipe parameter
        this.jadwalPemeliharaan = jadwalPemeliharaan;
    }
}