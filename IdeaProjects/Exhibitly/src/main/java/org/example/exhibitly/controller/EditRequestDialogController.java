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

        editArtefactNameField.setText(request.getArtefactName());
        editRequesterNameField.setText(request.getRequestName());
        editDescriptionArea.setText(request.getDescription());

        editStatusComboBox.getItems().clear();
        editStatusComboBox.getItems().addAll("Done", "Not Done");
        editStatusComboBox.setValue(request.getStatus());

        String userRole = user.getRole().toUpperCase();

        // --- ATUR HAK AKSES BERDASARKAN PERAN ---
        if ("KURATOR".equals(userRole)) {
            editArtefactNameField.setEditable(true);
            editRequesterNameField.setEditable(true);
            editDescriptionArea.setEditable(true);
            editStatusComboBox.setDisable(false);
        } else if ("STAFF".equals(userRole)) {
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
        String userRole = currentUser.getRole().toUpperCase();

        if ("KURATOR".equals(userRole)) {
            currentEditingRequest.setArtefactName(editArtefactNameField.getText());
            currentEditingRequest.setRequestName(editRequesterNameField.getText());
            currentEditingRequest.setDescription(editDescriptionArea.getText());
        }
        String newStatus = editStatusComboBox.getValue();
        currentEditingRequest.setStatus(newStatus);

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