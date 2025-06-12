
package org.example.exhibitly.controller;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import org.example.exhibitly.models.Maintenance;

import org.example.exhibitly.models.Actor;

import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Date;

public class MaintenanceRequestItemController {

    @FXML
    private Label timeLabel;
    @FXML
    private Label artefactNameLabel;
    @FXML
    private Label requesterNameLabel;
    @FXML
    private Label descriptionLabel;
    @FXML
    private Label statusLabel;
    @FXML
    private Button editButton;
    @FXML
    private ImageView pensilicon;

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

        editButton.setOnAction(event -> {
            if (onEditAction != null) {
                onEditAction.run();
            }
        });
    }

    public void setMaintenanceRequest(Maintenance request, Actor user) {
        System.out.println("Set maintenance request: " + request.getDescription() + " for " + request.getArtefactID());
        this.maintenanceRequest = request;
        this.currentUser = user;

        SimpleDateFormat dateOnlyFormat = new SimpleDateFormat("dd MMM yyyy");
        timeLabel.setText(dateOnlyFormat.format(request.getRequestDate()));

        artefactNameLabel.setText(request.getArtefactID_Name());

        String staffName = request.getRequestName();

        if (staffName == null || staffName.trim().isEmpty()) {
            if (request.getStaffID() == null) {
                staffName = "Unassigned";
            } else {
                staffName = "Staff ID: " + request.getStaffID();
            }
        }

        requesterNameLabel.setText(staffName);
        descriptionLabel.setText(request.getDescription());

        statusLabel.setText(request.getStatus());
        if ("Done".equalsIgnoreCase(request.getStatus())) {
            statusLabel.setStyle("-fx-text-fill: green; -fx-font-weight: bold;");
        } else {
            statusLabel.setStyle("-fx-text-fill: orange; -fx-font-weight: bold;");
        }

        if (currentUser != null && "Kurator".equalsIgnoreCase(currentUser.getRole())
                || "Staff".equalsIgnoreCase(currentUser.getRole())) {
            editButton.setVisible(true);
            editButton.setManaged(true);
        } else {
            editButton.setVisible(false);
            editButton.setManaged(false);
        }
    }

    public void setOnEditAction(Runnable action) {
        System.out.println("setOnEditAction called.");
        this.onEditAction = action;
    }
}