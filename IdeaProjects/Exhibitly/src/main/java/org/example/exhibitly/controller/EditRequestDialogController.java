// src/main/java/org/example/exhibitly/controller/EditRequestDialogController.java
package org.example.exhibitly.controller;

import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import org.example.exhibitly.models.Maintenance; // Import kelas Maintenance Anda
import org.example.exhibitly.models.Actor; // Import Actor atau peran yang Anda gunakan
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
    private Maintenance currentEditingRequest; // Menggunakan objek Maintenance langsung
    private Actor currentUser; // Menggunakan Actor untuk peran
    private boolean saveClicked = false;

    // Method untuk mengatur Stage dari dialog
    public void setDialogStage(Stage dialogStage) {
        this.dialogStage = dialogStage;
    }

    // Method untuk mengatur request yang akan diedit dan peran pengguna
    public void setRequest(Maintenance request, Actor user) {
        this.currentEditingRequest = request;
        this.currentUser = user; // Simpan objek Actor, bukan hanya peran

        // Mengisi field dari objek Maintenance
        // Penting: ArtefactID adalah int, jadi tampilkan sebagai "Artefak [ID]"
        editArtefactNameField.setText("Artefak " + request.getArtefactID());

        // Parsing nama requester dari deskripsi (seperti di MaintenanceRequestItemController)
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
            requesterName = "N/A"; // Default jika format tidak sesuai
        }

        editRequesterNameField.setText(requesterName);
        editDescriptionArea.setText(actualDescription);

        editStatusComboBox.getItems().clear(); // Bersihkan item sebelumnya
        editStatusComboBox.getItems().addAll("Done", "Not Done");
        editStatusComboBox.setValue(request.getStatus());

        // --- ATUR HAK AKSES BERDASARKAN PERAN ---
        if (currentUser.getRole().toUpperCase().equalsIgnoreCase("KURATOR")) {
            // Kurator bisa mengedit semua field
            editArtefactNameField.setEditable(true);
            editRequesterNameField.setEditable(true);
            editDescriptionArea.setEditable(true);
            editStatusComboBox.setDisable(false);
        } else if (currentUser.getRole().toUpperCase().equalsIgnoreCase("STAFF")) {
            // Staff hanya bisa mengubah status ComboBox
            editArtefactNameField.setEditable(false);
            editRequesterNameField.setEditable(false);
            editDescriptionArea.setEditable(false);
            editStatusComboBox.setDisable(false); // Staff BISA mengubah status
        } else {
            // Peran lain atau default: tidak bisa edit apa-apa
            editArtefactNameField.setEditable(false);
            editRequesterNameField.setEditable(false);
            editDescriptionArea.setEditable(false);
            editStatusComboBox.setDisable(true);
        }
    }

    // Method untuk mendapatkan apakah tombol Save diklik
    public boolean isSaveClicked() {
        return saveClicked;
    }

    // Method untuk mendapatkan objek Maintenance yang sudah diedit
    public Maintenance getEditedRequest() {
        // Hanya update field yang bisa diedit oleh peran saat ini
        String newStatus = editStatusComboBox.getValue();
        currentEditingRequest.setStatus(newStatus);

        if (currentUser.getRole().equalsIgnoreCase("Kurator")) {
            // Kurator bisa mengubah requester name dan description
            String updatedRequesterName = editRequesterNameField.getText().trim();
            String updatedDescriptionText = editDescriptionArea.getText().trim();
            String fullUpdatedDescription = "Requester: " + updatedRequesterName + "\n" + updatedDescriptionText;
            currentEditingRequest.setDescription(fullUpdatedDescription);

            /*
            * Jika Artefact Name juga diubah oleh Kurator, Anda perlu logic untuk mengupdate artefactID
            * Misalnya: currentEditingRequest.setArtefactID(Integer.parseInt(editArtefactNameField.getText().replaceAll("[^0-9]", "")));
            * Ini akan tergantung bagaimana Anda mengelola ArtefactID yang diedit.
            * Untuk saat ini, kita asumsikan ArtefactID tidak berubah atau ditangani secara terpisah.
            */

        } else if (currentUser instanceof Staff) {
            /*
            * Staff hanya bisa mengubah status, jadi hanya update status
            * Field lain tetap seperti aslinya karena mereka tidak editable
            */
        }

        // Logic untuk setPerformedDate jika status diubah menjadi "Done"
        if ("Done".equalsIgnoreCase(editStatusComboBox.getValue())) {
            // Hanya set performedDate jika sebelumnya null (permintaan baru saja selesai)
            if (currentEditingRequest.getPerformedDate() == null) {
                currentEditingRequest.setPerformedDate(new Date()); // Set tanggal sekarang
            }
        } else { // Jika status menjadi "Not Done"
            currentEditingRequest.setPerformedDate(null); // Kosongkan performedDate
        }

        return currentEditingRequest;
    }


    @FXML
    private void onSave() {
        if (editStatusComboBox.getValue() == null) {
            showAlert(Alert.AlertType.WARNING, "Input Invalid", "Status harus dipilih.");
            return;
        }

        saveClicked = true;
        dialogStage.close(); // Tutup pop-up
    }

    @FXML
    private void onCancel() {
        saveClicked = false;
        dialogStage.close(); // Tutup pop-up
    }

    private void showAlert(Alert.AlertType type, String title, String message) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}