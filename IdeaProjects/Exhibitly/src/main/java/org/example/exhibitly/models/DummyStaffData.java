// File: org/example/exhibitly/models/DummyStaffData.java
package org.example.exhibitly.models;

import org.example.exhibitly.models.Staff;
import java.util.ArrayList;
import java.util.List;

public class DummyStaffData {

    private static List<Staff> allStaff = new ArrayList<>();

    static {
        // Inisialisasi beberapa dummy staff dengan jadwal lengkap
        // Staff(id, username, password, name, role, jadwalPemeliharaan)
        allStaff.add(new Staff(1, "ardystaff", "password123", "stanislaus Ardy Bramantyo", "Senin-Jumat, 09:00-17:00"));
        allStaff.add(new Staff(2, "budistaff", "password123", "Budi Santoso", "Selasa & Kamis, 10:00-14:00"));
        allStaff.add(new Staff(3, "citrastaff", "password123", "Citra Dewi", "Setiap Hari, 08:00-16:00"));
        allStaff.add(new Staff(4, "dennystaff", "password123", "Denny Wijaya", "Rabu, 09:00-12:00"));
        allStaff.add(new Staff(5, "ekastaff", "password123", "Eka Putri", "Jumat, 13:00-17:00"));
        allStaff.add(new Staff(6, "sendistaff", "password123", "Sendi Putra Alicia", "Setiap Hari, 10:00-18:00"));
        allStaff.add(new Staff(7, "jokostaff", "password123", "Joko Santoso", "Senin-Rabu, 09:00-17:00"));
        // Tambahkan staf lain jika diperlukan
    }

    /**
     * Mengembalikan daftar semua Staff dummy.
     * Mengembalikan salinan daftar untuk mencegah modifikasi eksternal.
     * @return List of Staff objects
     */
    public static List<Staff> getAllStaff() {
        return new ArrayList<>(allStaff);
    }

    /**
     * Mencari Staff berdasarkan nama.
     * @param name Nama Staff yang dicari
     * @return Objek Staff jika ditemukan, null jika tidak.
     */
    public static Staff getStaffByName(String name) {
        for (Staff staff : allStaff) {
            if (staff.getName().equalsIgnoreCase(name)) {
                return staff;
            }
        }
        return null;
    }

    /**
     * Mencari Staff berdasarkan username.
     * @param username Username Staff yang dicari
     * @return Objek Staff jika ditemukan, null jika tidak.
     */
    // Di DummyStaffData.java
    public static Staff getStaffByUsername(String username) {
        for (Staff staff : allStaff) {
            // Tambahkan pemeriksaan null di sini!
            if (staff.getUsername() != null && staff.getUsername().equalsIgnoreCase(username)) {
                return staff;
            }
        }
        return null;
    }
}