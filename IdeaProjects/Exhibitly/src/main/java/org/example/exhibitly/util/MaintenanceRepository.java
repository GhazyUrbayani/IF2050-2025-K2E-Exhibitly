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
        String sql = """
            SELECT m.maintenanceid, m.kuratorid, m.staffid, m.artefactid, m.artefactName, 
                   m.requestdate, m.performeddate, m.status, m.description,
                   s.name as staffName
            FROM maintenance m
            LEFT JOIN actor s ON m.staffid = s.actorid
            ORDER BY m.maintenanceid DESC
            """;
        try (Connection connectDB = db.getConnection();
             PreparedStatement preparedStatement = connectDB.prepareStatement(sql);
             ResultSet resultSet = preparedStatement.executeQuery()) {

            while (resultSet.next()) {
                int id = resultSet.getInt("maintenanceid");
                int kuratorID = resultSet.getInt("kuratorid");
                Integer staffID = null;
                int staffIDValue = resultSet.getInt("staffid");
                if (!resultSet.wasNull()) {
                    staffID = staffIDValue;
                }
                int artefactID = resultSet.getInt("artefactid");
                String artefactName = resultSet.getString("artefactName");

                java.sql.Date requestDate = resultSet.getDate("requestDate");
                Date utilRequestDate = requestDate != null ? new java.util.Date(requestDate.getTime()) : new Date();

                java.sql.Date performedDate = resultSet.getDate("performeddate");
                Date utilPerformedDate = performedDate == null ? null : new java.util.Date(performedDate.getTime());

                String status = resultSet.getString("status");
                String description = resultSet.getString("description");

                String staffName = resultSet.getString("staffName");


                System.out.println("Loading maintenance ID: " + id + 
                             ", StaffID: " + staffID + 
                             ", StaffName from DB: '" + staffName + "'");

                String displayName;
                if (staffName != null && !staffName.trim().isEmpty()) {
                    displayName = staffName; 
                } else if (staffID == null) {
                    displayName = "Unassigned"; 
                } else {
                    displayName = "Staff ID: " + staffID; 
                }

                maintenances.add(new Maintenance(id, kuratorID, staffID, displayName, artefactID, artefactName, utilRequestDate, utilPerformedDate, status, description));
            }
        } catch (SQLException e) {
            System.err.println("Error fetching maintenances: " + e.getMessage());
            e.printStackTrace();
        }
        return maintenances;
    }
}
