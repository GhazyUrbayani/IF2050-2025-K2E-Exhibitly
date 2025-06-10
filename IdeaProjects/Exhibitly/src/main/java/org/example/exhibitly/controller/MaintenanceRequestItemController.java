// src/main/java/org/example/exhibitly/controller/MaintenanceRequestItemController.java
package org.example.exhibitly.controller;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import org.example.exhibitly.models.Maintenance; // Import model Maintenance
// Import Actor jika Anda ingin logika tampil/sembunyi tombol edit berdasarkan peran
import org.example.exhibitly.models.Actor;

import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Date;

public class MaintenanceRequestItemController {

    @FXML private Label timeLabel;
    @FXML private Label artefactNameLabel;
    @FXML private Label requesterNameLabel;
    @FXML private Label descriptionLabel;
    @FXML private Label statusLabel;
    @FXML private Button editButton;
    @FXML private ImageView pensilicon; // Ini fx:id untuk ImageView Anda



    private Maintenance maintenanceRequest;
    private Actor currentUser;
    private Runnable onEditAction;

    private static final SimpleDateFormat TIME_FORMAT = new SimpleDateFormat("HH:mm");

    @FXML
    public void initialize() {
        try {
            ImageView pensilicon = (ImageView) editButton.getGraphic();
            pensilicon.setImage(new Image(getClass().getResourceAsStream("/images/pensil.png")));
        } catch (Exception e) {
            System.err.println("Error loading edit icon: " + e.getMessage());
            editButton.setText("Edit");
            pensilicon.setVisible(false);
            pensilicon.setManaged(false);
        }

        // Setup aksi tombol edit (akan dipanggil dari MaintenanceController)
        editButton.setOnAction(event -> {
            if (onEditAction != null) {
                onEditAction.run();
            }
        });
    }

    // Metode untuk mengatur data Maintenance ke dalam elemen UI
    public void setMaintenanceRequest(Maintenance request, Actor user) {
        System.out.println("Set maintenance request: " + request.getDescription() + " for " + request.getArtefactID());
        this.maintenanceRequest = request;
        this.currentUser = user;

        timeLabel.setText(TIME_FORMAT.format(request.getRequestDate()));
        artefactNameLabel.setText(request.getArtefactID_Name());

        // Parse requester name from description, if applicable
        String fullDescription = request.getDescription();
        String requesterName = "Unknown";
        String actualDescription = fullDescription;

        if (fullDescription != null && fullDescription.startsWith("Requester: ")) {
            String[] parts = fullDescription.split("\n", 2);
            requesterName = parts[0].replace("Requester: ", "").trim();
            if (parts.length > 1) {
                actualDescription = parts[1].trim();
            } else {
                actualDescription = "";
            }
        } else {
            requesterName = "N/A";
        }

        requesterNameLabel.setText(requesterName);
        descriptionLabel.setText(actualDescription);

        statusLabel.setText(request.getStatus());
        if ("Done".equalsIgnoreCase(request.getStatus())) {
            statusLabel.setStyle("-fx-text-fill: green; -fx-font-weight: bold;");
        } else {
            statusLabel.setStyle("-fx-text-fill: orange; -fx-font-weight: bold;");
        }

        // Atur visibilitas tombol edit berdasarkan peran pengguna
        if (currentUser != null && "Kurator".equalsIgnoreCase(currentUser.getRole()) || "Staff".equalsIgnoreCase(currentUser.getRole())) {
            editButton.setVisible(true);
            editButton.setManaged(true);
        } else {
            editButton.setVisible(false);
            editButton.setManaged(false);
        }
    }

    // Metode untuk mengatur callback saat tombol edit diklik
    public void setOnEditAction(Runnable action) {
        System.out.println("setOnEditAction called.");
        this.onEditAction = action;
    }
}