package org.example.exhibitly.util;

import org.example.exhibitly.models.Artefact;
import org.example.exhibitly.models.DummyStaffData;
import org.example.exhibitly.models.Staff;

import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

public class StaffRepository {
    DatabaseConnection db = new DatabaseConnection();
    public List<Staff> getAllStaffs() {
        List<Staff> staffs = new ArrayList<>();
        String sql = "SELECT *  FROM actor JOIN staffpemeliharaan ON actor.actorID = staffpemeliharaan.actorID WHERE role = 'Staff'";

        try (Connection connectDB = db.getConnection();
             PreparedStatement preparedStatement = connectDB.prepareStatement(sql);
             ResultSet resultSet = preparedStatement.executeQuery()) {

            while (resultSet.next()) {
                int id = resultSet.getInt("actorID");
                String username = resultSet.getString("username");
                String password = resultSet.getString("password");
                String name = resultSet.getString("name");
                String role = resultSet.getString("role");
                String date = resultSet.getString("jadwalPemeliharaan");

                staffs.add(new Staff(id, username, password, name, date));
            }
        } catch (SQLException e) {
            System.err.println("Error fetching staffs: " + e.getMessage());
            e.printStackTrace();
        }
        return staffs;
    }
}