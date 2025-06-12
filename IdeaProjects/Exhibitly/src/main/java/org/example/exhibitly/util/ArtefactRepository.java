package org.example.exhibitly.util;

import org.example.exhibitly.models.Artefact;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ArtefactRepository {

    public List<Artefact> getAllArtefacts() {
        DatabaseConnection db = new DatabaseConnection();
        List<Artefact> artefacts = new ArrayList<>();
        String sql = "SELECT * FROM artefact";

        try (Connection connectDB = db.getConnection();
             PreparedStatement preparedStatement = connectDB.prepareStatement(sql);
             ResultSet resultSet = preparedStatement.executeQuery()) {

            while (resultSet.next()) {
                int id = resultSet.getInt("artefactID");
                String name = resultSet.getString("title");
                String region = resultSet.getString("region");
                int period = resultSet.getInt("period");
                String description = resultSet.getString("description");
                String mediaURL = "/images/arca3.png";

                artefacts.add(new Artefact(id, name, region, period, description, mediaURL));
            }
        } catch (SQLException e) {
            System.err.println("Error fetching artefacts: " + e.getMessage());
            e.printStackTrace();
        }
        return artefacts;
    }
}
