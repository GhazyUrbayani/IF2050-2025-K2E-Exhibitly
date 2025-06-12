package org.example.exhibitly.util;

import org.example.exhibitly.models.Maintenance;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class MaintenanceRepository {
    DatabaseConnection db = new DatabaseConnection();
    public List<Maintenance> getAllMaintenances() {
        List<Maintenance> maintenances = new ArrayList<>();
        String sql = "SELECT * FROM maintenance";

        try (Connection connectDB = db.getConnection();
             PreparedStatement preparedStatement = connectDB.prepareStatement(sql);
             ResultSet resultSet = preparedStatement.executeQuery()) {

            while (resultSet.next()) {
                int id = resultSet.getInt("maintenanceID");
                int kuratorID = resultSet.getInt("kuratorID");
                int staffID = resultSet.getInt("staffID");
                String artefactName = resultSet.getString("artefactName");

                // Format requestDate
                Date requestDate = resultSet.getDate("requestDate");
                Date utilRequestDate = new java.util.Date(requestDate.getTime());

                // Format performedDate
                Date performedDate = resultSet.getDate("performedDate");
                Date utilPerformedDate = new java.util.Date(performedDate.getTime());

                String status = resultSet.getString("status");
                String description = resultSet.getString("description");
                String type = resultSet.getString("type");

                maintenances.add(new Maintenance(id, kuratorID, staffID, "Someone", 0, artefactName, utilRequestDate, utilPerformedDate, type, status, description));
            }
        } catch (SQLException e) {
            System.err.println("Error fetching maintenances: " + e.getMessage());
            e.printStackTrace();
        }
        return maintenances;
    }
}
