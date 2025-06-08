package org.example.exhibitly.controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.CheckBox;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import javafx.util.Callback;
import java.util.Date; // Tambahkan baris ini

import org.example.exhibitly.models.Maintenance; // Asumsi Maintenance model sudah ada
import org.example.exhibitly.models.Staff; // Asumsi Staff model sudah ada
import org.example.exhibitly.models.DummyStaffData; // Akan kita buat ini

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

public class AddMaintenanceRequestPopupController implements Initializable {

    @FXML private TextField artefactNameField;
    @FXML private TextField requesterNameField;
    @FXML private TextArea descriptionArea;
    @FXML private ComboBox<Staff> staffComboBox; // Staff object, not just String

    private Stage dialogStage;
    private Maintenance newMaintenanceRequest;
    private List<Staff> availableStaff;
    private ObservableList<Staff> selectedStaff = FXCollections.observableArrayList(); // Untuk melacak staff yang dipilih

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        // Ambil data staff dummy
        availableStaff = DummyStaffData.getAllStaff();

        // Setup ComboBox untuk menampilkan multi-select checkbox
        staffComboBox.setItems(FXCollections.observableArrayList(availableStaff));
        staffComboBox.setCellFactory(new Callback<ListView<Staff>, ListCell<Staff>>() {
            @Override
            public ListCell<Staff> call(ListView<Staff> param) {
                return new ListCell<Staff>() {
                    private CheckBox checkBox;
                    private Label staffNameLabel;
                    private HBox hbox;

                    {
                        checkBox = new CheckBox();
                        staffNameLabel = new Label();
                        hbox = new HBox(5, checkBox, staffNameLabel); // Spacing 5px
                        hbox.setPadding(new javafx.geometry.Insets(0, 0, 0, 5)); // Padding kiri untuk visual
                    }

                    @Override
                    protected void updateItem(Staff item, boolean empty) {
                        super.updateItem(item, empty);
                        if (empty || item == null) {
                            setGraphic(null);
                            setText(null);
                        } else {
                            staffNameLabel.setText(item.getName());
                            checkBox.setSelected(selectedStaff.contains(item)); // Set selected state
                            checkBox.setOnAction(event -> {
                                if (checkBox.isSelected()) {
                                    if (!selectedStaff.contains(item)) {
                                        selectedStaff.add(item);
                                    }
                                } else {
                                    selectedStaff.remove(item);
                                }
                                updateComboBoxButtonText(); // Update teks tombol ComboBox
                            });
                            setGraphic(hbox);
                        }
                    }
                };
            }
        });

        // Set prompt text atau custom button cell untuk ComboBox
        staffComboBox.setButtonCell(new ListCell<Staff>() {
            @Override
            protected void updateItem(Staff item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setText("Select staff...");
                } else {
                    updateComboBoxButtonText();
                }
            }
        });

        // Event listener saat pop-up ComboBox ditutup untuk memperbarui teks tombol
        staffComboBox.showingProperty().addListener((obs, wasShowing, isShowing) -> {
            if (!isShowing) { // Ketika pop-up ditutup
                updateComboBoxButtonText();
            }
        });
    }

    // Metode untuk memperbarui teks pada tombol ComboBox
    private void updateComboBoxButtonText() {
        if (selectedStaff.isEmpty()) {
            staffComboBox.getButtonCell().setText("Select staff...");
        } else if (selectedStaff.size() == availableStaff.size()) {
            staffComboBox.getButtonCell().setText("All staff selected");
        } else {
            staffComboBox.getButtonCell().setText(selectedStaff.size() + " selected");
        }
    }


    public void setDialogStage(Stage dialogStage) {
        this.dialogStage = dialogStage;
    }

    public Maintenance getNewMaintenanceRequest() {
        return newMaintenanceRequest;
    }

    @FXML
    private void onSubmit(ActionEvent event) {
        String artefactName = artefactNameField.getText().trim();
        String requesterName = requesterNameField.getText().trim();
        String description = descriptionArea.getText().trim();

        if (artefactName.isEmpty() || requesterName.isEmpty() || description.isEmpty() || selectedStaff.isEmpty()) {
            // Tampilkan pesan error jika ada field kosong
            System.err.println("Error: Semua field, termasuk Staff Tertuju, harus diisi!");
            return;
        }

        // Kumpulkan ID staff yang dipilih
        String targetedStaffIds = selectedStaff.stream()
                .map(staff -> String.valueOf(staff.getActorID()))
                .collect(Collectors.joining(","));

        // Buat objek Maintenance baru (dengan asumsi constructor Anda sesuai)
        // Sesuaikan constructor Maintenance Anda jika perlu untuk menampung targetedStaffIds
        // Saya asumsikan Anda ingin menyimpan ini di salah satu field yang ada, misal 'description'
        // atau Anda perlu menambahkan field baru di model Maintenance.
        // Untuk demo, kita akan tambahkan ke deskripsi.

        // Menambahkan prefix "Requester: " dan "Targeted Staff: " ke deskripsi
        String fullDescription = "Requester: " + requesterName + "\n"
                + "Targeted Staff: " + selectedStaff.stream().map(Staff::getName).collect(Collectors.joining(", ")) + "\n"
                + description;

        newMaintenanceRequest = new Maintenance(
                // Asumsi constructor Maintenance Anda sesuai:
                // (id, null, null, requestID, artefactID, requestDate, performedDate, type, status, description)
                // Sesuaikan parameter id dan artefactID jika Anda memiliki logic database
                null, // ID (biarkan null jika auto-generated oleh DB atau set UUID di luar sini)
                null, // PerformedBy (null karena belum selesai)
                null, // VerifiedBy (null karena belum selesai)
                java.util.UUID.randomUUID().toString().substring(0,8), // requestID
                0, // artefactID - perlu logic untuk ini
                new Date(), // requestDate
                null, // performedDate (null karena Not Done)
                "Request", // type
                "Not Done", // status (otomatis Not Done)
                fullDescription // deskripsi lengkap
        );

        dialogStage.close(); // Tutup pop-up setelah submit
    }

    @FXML
    private void onCancel(ActionEvent event) {
        newMaintenanceRequest = null; // Set null jika dibatalkan
        dialogStage.close(); // Tutup pop-up
    }
}