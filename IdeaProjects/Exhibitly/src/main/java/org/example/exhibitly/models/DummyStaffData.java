 
package org.example.exhibitly.models;

import org.example.exhibitly.models.Staff;
import java.util.ArrayList;
import java.util.List;

public class DummyStaffData {

    private static List<Staff> allStaff = new ArrayList<>();

    static {
         
         
        allStaff.add(new Staff(1, "ardystaff", "password123", "stanislaus Ardy Bramantyo", "Senin-Jumat, 09:00-17:00"));
        allStaff.add(new Staff(2, "budistaff", "password123", "Budi Santoso", "Selasa & Kamis, 10:00-14:00"));
        allStaff.add(new Staff(3, "citrastaff", "password123", "Citra Dewi", "Setiap Hari, 08:00-16:00"));
        allStaff.add(new Staff(4, "dennystaff", "password123", "Denny Wijaya", "Rabu, 09:00-12:00"));
        allStaff.add(new Staff(5, "ekastaff", "password123", "Eka Putri", "Jumat, 13:00-17:00"));
        allStaff.add(new Staff(6, "sendistaff", "password123", "Sendi Putra Alicia", "Setiap Hari, 10:00-18:00"));
        allStaff.add(new Staff(7, "jokostaff", "password123", "Joko Santoso", "Senin-Rabu, 09:00-17:00"));
         
    }

    public static List<Staff> getAllStaff() {
        return new ArrayList<>(allStaff);
    }

    public static Staff getStaffByName(String name) {
        for (Staff staff : allStaff) {
            if (staff.getName().equalsIgnoreCase(name)) {
                return staff;
            }
        }
        return null;
    }

     
    public static Staff getStaffByUsername(String username) {
        for (Staff staff : allStaff) {
             
            if (staff.getUsername() != null && staff.getUsername().equalsIgnoreCase(username)) {
                return staff;
            }
        }
        return null;
    }
}