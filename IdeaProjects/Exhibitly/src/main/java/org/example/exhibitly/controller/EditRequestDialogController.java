package org.example.exhibitly.controller;

import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import org.example.exhibitly.models.Maintenance;
import org.example.exhibitly.models.Actor;
import javafx.scene.control.Alert;
import java.util.Date;
import org.example.exhibitly.models.Staff;
import org.example.exhibitly.models.Actor;


public class EditRequestDialogController {

    @FXML
    private TextField editArtefactNameField;
    @FXML
    private TextField editRequesterNameField;
    @FXML
    private TextArea editDescriptionArea;
    @FXML
    private ComboBox<String> editStatusComboBox;

    private Stage dialogStage;
    private Maintenance currentEditingRequest;
    private Actor currentUser;
    private boolean saveClicked = false;

    public void setDialogStage(Stage dialogStage) {
        this.dialogStage = dialogStage;
    }

    public void setRequest(Maintenance request, Actor user) {
        this.currentEditingRequest = request;
        this.currentUser = user;

        String artefactName = request.getArtefactName();
        editArtefactNameField.setText(artefactName);

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

        editRequesterNameField.setText(requesterName);
        editDescriptionArea.setText(actualDescription);

        editStatusComboBox.getItems().clear();
        editStatusComboBox.getItems().addAll("Done", "Not Done");
        editStatusComboBox.setValue(request.getStatus());

        // --- ATUR HAK AKSES BERDASARKAN PERAN ---
        if (currentUser.getRole().toUpperCase().equalsIgnoreCase("KURATOR")) {

            editArtefactNameField.setEditable(true);
            editRequesterNameField.setEditable(true);
            editDescriptionArea.setEditable(true);
            editStatusComboBox.setDisable(false);
        } else if (currentUser.getRole().toUpperCase().equalsIgnoreCase("STAFF")) {

            editArtefactNameField.setEditable(false);
            editRequesterNameField.setEditable(false);
            editDescriptionArea.setEditable(false);
            editStatusComboBox.setDisable(false);
        } else {

            editArtefactNameField.setEditable(false);
            editRequesterNameField.setEditable(false);
            editDescriptionArea.setEditable(false);
            editStatusComboBox.setDisable(true);
        }
    }

    public boolean isSaveClicked() {
        return saveClicked;
    }

    public Maintenance getEditedRequest() {
        String newArtefactName = editArtefactNameField.getText();
        String newRequesterName = editRequesterNameField.getText();
        String newDescriptionArea = editDescriptionArea.getText();
        String newStatus = editStatusComboBox.getValue();
        Date newRequestDate = new Date();

        currentEditingRequest.setArtefactName(newArtefactName);
        currentEditingRequest.setRequestName(newRequesterName);

        currentEditingRequest.setDescription("Requester: " + newRequesterName + "\n" + newDescriptionArea);
        currentEditingRequest.setStatus(newStatus);
        currentEditingRequest.setRequestDate(newRequestDate);

        if ("Done".equalsIgnoreCase(newStatus)) {
            if (currentEditingRequest.getPerformedDate() == null) {
                currentEditingRequest.setPerformedDate(new Date());
            }
        } else {
            currentEditingRequest.setPerformedDate(null);
        }

        return currentEditingRequest;
    }

    @FXML
    private void onSave() {
        if (editStatusComboBox.getValue() == null) {
            showAlert(Alert.AlertType.WARNING, "Input Invalid", "Status harus dipilih.");
            saveClicked = false;
            return;
        }
        saveClicked = true;
        dialogStage.close();
    }

    @FXML
    private void onCancel() {
        saveClicked = false;
        dialogStage.close();
    }

    private void showAlert(Alert.AlertType type, String title, String message) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}