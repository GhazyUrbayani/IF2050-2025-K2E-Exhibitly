package org.example.exhibitly.controller;

import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

import org.example.exhibitly.models.Actor;
import org.example.exhibitly.models.Artefact;
import org.example.exhibitly.models.Maintenance;
import org.example.exhibitly.models.Staff;
import org.example.exhibitly.util.ArtefactRepository;
import org.example.exhibitly.util.DatabaseConnection;
import org.example.exhibitly.util.MaintenanceRepository;
import org.example.exhibitly.util.StaffRepository;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

public class MaintenanceController extends BaseController implements Initializable {

    // ... (Elemen FXML yang sama seperti sebelumnya) ...
    @FXML private Label userNameLabel;
    @FXML private Label userRoleLabel;
    @FXML private VBox routineScheduleSection;
    @FXML private Label jadwalPembersihanLabel;

    @FXML private Button requestTabButton;
    @FXML private Button historyTabButton;
    @FXML private Button logoButton;
    @FXML private VBox requestContentDisplay;
    @FXML private VBox historyContent;
    @FXML private VBox todayRequestsContainer;
    @FXML private VBox pastRequestsContainer;
    @FXML private VBox historyDataContainer;

    @FXML private Label todayDateLabel;

    @FXML private VBox addRequestForm;
    @FXML private ComboBox<String> artefactComboBox;
    @FXML private ComboBox<String> performedByStaffComboBox;
    @FXML private TextArea descriptionArea;
    @FXML private Button addRequestButton;

    @FXML private ImageView logoFooter;

    // ... (Elemen FXML header/footer lainnya) ...

    private List<Maintenance> allMaintenanceRecords;
    private Actor currentUser;
    private StaffRepository staffRepository;
    private MaintenanceRepository maintenanceRepository;
    private ArtefactRepository artefactRepository;

    private static final DateTimeFormatter TIME_FORMAT = DateTimeFormatter.ofPattern("HH:mm"); // Hapus 'new'
    private static final DateTimeFormatter DATE_FORMATTER_DISPLAY = DateTimeFormatter.ofPattern("dd MMMM yyyy"); // Hapus 'new', dan ganti '
    private static final DateTimeFormatter TODAY_DATE_FORMATTER = DateTimeFormatter.ofPattern("dd MMMM yyyy"); // Mengubah ini agar konsisten

    private List<Staff> allStaffs; // Pastikan ini ada dan diisi
    private List<Artefact> allArtefacts; // Pastikan ini ada dan diisi
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

//        if (!session.isLoggedIn() && !session.isKurator()) {
//            return;
//        }
        try {
            ImageView logoImageView = (ImageView) logoButton.getGraphic();
            logoImageView.setImage(new Image(getClass().getResourceAsStream("/images/logo.png")));
        } catch (Exception e) {
            System.err.println("Error loading logo for button: " + e.getMessage());
            e.printStackTrace();
        }

        try {
            logoFooter.setImage(new Image(getClass().getResourceAsStream("/images/logo2.png")));
        } catch (Exception e) {
            System.out.println("[Erorr] Couldn't load logo");
        }

        maintenanceRepository = new MaintenanceRepository();
        allMaintenanceRecords = new ArrayList<>();
        loadMaintenances();
//        Date todayDateUtil = new Date(); // java.util.Date untuk dummy data
//        Date yesterdayDateUtil = Date.from(LocalDate.now(ZoneId.systemDefault()).minusDays(1).atStartOfDay(ZoneId.systemDefault()).toInstant());
//        Date twoDaysAgoDateUtil = Date.from(LocalDate.now(ZoneId.systemDefault()).minusDays(2).atStartOfDay(ZoneId.systemDefault()).toInstant());
//        Date threeDaysAgoDateUtil = Date.from(LocalDate.now(ZoneId.systemDefault()).minusDays(3).atStartOfDay(ZoneId.systemDefault()).toInstant());
//
//        // Initial dummy data
//        // Untuk contoh ini, saya tambahkan beberapa tanggal berbeda di 'Past Requests'
//        allMaintenanceRecords.add(new Maintenance(1, null, null, UUID.randomUUID().toString().substring(0,8), 101, "Arca Ganesha", todayDateUtil, null, "Pembersihan", "Not Done", "Tolong dibersihin ya"));
//        allMaintenanceRecords.add(new Maintenance(2, null, null, UUID.randomUUID().toString().substring(0,8), 102, "Arca Jatinangor", todayDateUtil, todayDateUtil, "Perbaikan", "Done", "Perlu perbaikan kecil"));
//        allMaintenanceRecords.add(new Maintenance(3, null, null, UUID.randomUUID().toString().substring(0,8), 103, "Arca Cirebon", todayDateUtil, null, "Pembersihan", "Not Done", "Debu terlalu tebal"));
//
//        allMaintenanceRecords.add(new Maintenance(4, null, null, UUID.randomUUID().toString().substring(0,8), 104, "Arca Tamfest", yesterdayDateUtil, yesterdayDateUtil, "Rutin", "Done", "Pembersihan rutin kemarin"));
//        allMaintenanceRecords.add(new Maintenance(5, null, null, UUID.randomUUID().toString().substring(0,8), 105, "Arca Ganyang", yesterdayDateUtil, yesterdayDateUtil, "Inspeksi", "Done", "Cek stabilitas kemarin"));
//        allMaintenanceRecords.add(new Maintenance(6, null, null, UUID.randomUUID().toString().substring(0,8), 106, "Arca Cisitu", yesterdayDateUtil, null, "Pembersihan", "Not Done", "Perlu penanganan khusus kemarin"));
//
//        allMaintenanceRecords.add(new Maintenance(8, null, null, UUID.randomUUID().toString().substring(0,8), 108, "Arca Tubis", threeDaysAgoDateUtil, null, "Perbaikan", "Not Done", "Sensor tidak berfungsi"));
//        allMaintenanceRecords.add(new Maintenance(9, null, null, UUID.randomUUID().toString().substring(0,8), 109, "Arca Saraga", threeDaysAgoDateUtil, null, "Cek", "Not Done", "Lampu display mati"));
//
//        allMaintenanceRecords.add(new Maintenance(7, null, null, UUID.randomUUID().toString().substring(0,8), 107, "Arca Tamansari", twoDaysAgoDateUtil, twoDaysAgoDateUtil, "Perbaikan", "Done", "Display kusam (Done)"));

        // Dummy data untuk Artefak
        // --- MENGISI ARTEFAK COMBOBOX (SAMA) ---
        artefactRepository = new ArtefactRepository();
        allArtefacts = new ArrayList<>();
        loadArtefacts();
        //int artefactID, String title, String region, int period, String description, String mediaURL
//        allArtefacts.add(new Artefact(101, "Patung Ganesha", "Hell", 200, "x", "x"));
//        allArtefacts.add(new Artefact(102, "Lukisan Monalisa", "Hell", 200, "x", "x"));
//        allArtefacts.add(new Artefact(103, "Kendi Tanah Liat", "Hell", 200, "x", "x"));
//        artefactComboBox.getItems().clear();
//        for (Artefact artefact : allArtefacts) {
//            artefactComboBox.getItems().add(artefact.getTitle() + " (ID: " + artefact.getArtefactID() + ")");
//        }
        artefactComboBox.getItems().add("Select Artefact");
        artefactComboBox.setValue("Select Artefact");

        // Dummy data untuk Staff (Anda perlu mendapatkan ini dari daftar Staff Anda)
        // Jika Anda memiliki List<Staff> allStaffs, Anda bisa menggunakannya di sini
        staffRepository = new StaffRepository();
        allStaffs = new ArrayList<>();
        loadStaffs();
//        allStaffs.add(new Staff(1, "Staff", "staff_a_username", "Staff A", "s")); // Contoh: id, role, username, name
//        allStaffs.add(new Staff(2, "Staff", "staff_b_username", "Staff B", "s"));
//        allStaffs.add(new Staff(3, "Staff", "staff_c_username", "Staff C", "s"));
        // Tambahkan opsi "Unassigned" atau "Belum Ditugaskan"
        performedByStaffComboBox.getItems().add("Unassigned"); // Opsi default untuk Staff yang belum ditugaskan

        for (Staff staff : allStaffs) {
            performedByStaffComboBox.getItems().add(staff.getName()); // Tampilkan hanya nama staff
        }
        // Set "Unassigned" sebagai nilai default saat pertama kali dimuat
        performedByStaffComboBox.setValue("Unassigned");

        // Simulate user login
        // currentUser = new Staff(1, "ardystaff", "password123", "Stanislaus Ardy Bramantyo", "Setiap Hari, 09.00 - 15.00");
        currentUser = session.getCurrentActor();
        updateUserInfo();

//        loadRequests();
//        loadHistory();

        todayDateLabel.setText("Today - " + LocalDate.now(ZoneId.systemDefault()).format(TODAY_DATE_FORMATTER));

        requestTabButton.setStyle("-fx-background-color: transparent; -fx-border-color: black; -fx-border-width: 0 0 2 0;");
        historyTabButton.setStyle("-fx-background-color: transparent; -fx-border-color: transparent; -fx-border-width: 0 0 0 0;");

        requestContentDisplay.setVisible(true);
        requestContentDisplay.setManaged(true);

        historyContent.setVisible(false);
        historyContent.setManaged(false);

        addRequestForm.setVisible(false);
        addRequestForm.setManaged(false);
    }

    private void loadStaffs() {
        Task<List<Staff>> loadTask = new Task<List<Staff>>() {
            protected List<Staff> call () throws  Exception{
                return staffRepository.getAllStaffs();
            }
        };

        loadTask.setOnSucceeded(event -> {
            allStaffs = loadTask.getValue();
            performedByStaffComboBox.getItems().clear();
            for (Staff staff : allStaffs) {
                performedByStaffComboBox.getItems().add(staff.getName());
            }
            System.out.println("Loaded " + allStaffs.size() + " staffs");
        });

        loadTask.setOnFailed(event -> {
            System.err.println("Error loading staffs: " + loadTask.getException().getMessage());
        });

        new Thread(loadTask).start();
    }


    private void loadMaintenances() {
        Task<List<Maintenance>> loadTask = new Task<List<Maintenance>>() {
            protected List<Maintenance> call () throws  Exception{
                return maintenanceRepository.getAllMaintenances();
            }
        };

        loadTask.setOnSucceeded(event -> {
            allMaintenanceRecords = loadTask.getValue();
            loadRequests();
            loadHistory();
            System.out.println("Loaded " + allMaintenanceRecords.size() + " maintenances");
        });

        loadTask.setOnFailed(event -> {
            System.err.println("Error loading maintenances: " + loadTask.getException().getMessage());
        });

        new Thread(loadTask).start();
    }

    private void loadArtefacts() {
        Task<List<Artefact>> loadTask = new Task<List<Artefact>>() {
            protected List<Artefact> call () throws  Exception{
                return artefactRepository.getAllArtefacts();
            }
        };

        loadTask.setOnSucceeded(event -> {
            allArtefacts = loadTask.getValue();
            artefactComboBox.getItems().clear();
            for (Artefact artefact : allArtefacts) {
                artefactComboBox.getItems().add(artefact.getTitle() + " (ID: " + artefact.getArtefactID() + ")");
            }
            System.out.println("Loaded " + allArtefacts.size() + " artefacts");
        });

        loadTask.setOnFailed(event -> {
            System.err.println("Error loading artefacts: " + loadTask.getException().getMessage());
        });

        new Thread(loadTask).start();
    }


    private void updateUserInfo() {
        if (currentUser != null) {
            userNameLabel.setText(currentUser.getName() + "!");
            userRoleLabel.setText("(" + currentUser.getRole() + ")");

            if ("Staff".equalsIgnoreCase(currentUser.getRole())) {
                routineScheduleSection.setVisible(true);
                addRequestButton.setVisible(false);

                if (currentUser instanceof Staff) {
                    Staff staff = (Staff) currentUser;
                    if (jadwalPembersihanLabel != null) {
                        jadwalPembersihanLabel.setText(staff.getJadwalPemeliharaan());
                    }
                }
            } else {
                routineScheduleSection.setVisible(false);
                addRequestButton.setVisible(true);
            }
        }
    }

    private void loadRequests() {
        todayRequestsContainer.getChildren().clear();
        pastRequestsContainer.getChildren().clear();
        LocalDate todayLocalDate = LocalDate.now(ZoneId.systemDefault());

        List<Maintenance> openRequests = allMaintenanceRecords.stream()
                .filter(req -> !"Done".equalsIgnoreCase(req.getStatus()))
                .sorted(Comparator
                    .comparing((Maintenance req) -> req.getRequestDate().toInstant().atZone(ZoneId.systemDefault()).toLocalDate())
                    .thenComparing((Maintenance req) -> {
                        String staffName = req.getRequestName();
                        if (staffName == null || staffName.trim().isEmpty() || "Unassigned".equals(staffName)) {
                            return "ZZZ_Unassigned";
                        }
                        return staffName;
                    })
                    .thenComparing((Maintenance req) -> req.getRequestDate().toInstant().atZone(ZoneId.systemDefault()).toLocalTime()))
                .collect(Collectors.toList());

        LocalDate lastRequestDate = null;
        boolean todayRequestsFound = false;
        boolean pastRequestsFound = false;

        for (Maintenance request : openRequests) {
            LocalDate currentRequestDate = request.getRequestDate().toInstant().atZone(ZoneId.systemDefault()).toLocalDate();

            try {
                FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/org/example/exhibitly/Maintenance_Request_Item.fxml"));
                GridPane requestItem = fxmlLoader.load();
                MaintenanceRequestItemController itemController = fxmlLoader.getController(); // Dapatkan controller item
                itemController.setMaintenanceRequest(request, currentUser); // Set data dan peran

                // Set callback untuk tombol edit di item controller

                itemController.setOnEditAction(() -> showEditRequestDialog(request));

                if (currentRequestDate.isEqual(todayLocalDate)) {
                    todayRequestsContainer.getChildren().add(requestItem);
                    todayRequestsFound = true;
                } else {
                    if (lastRequestDate == null || !currentRequestDate.isEqual(lastRequestDate)) {
                        Label dateHeader = new Label(currentRequestDate.format(DATE_FORMATTER_DISPLAY));
                        dateHeader.setStyle("-fx-font-size: 16px; -fx-font-weight: bold; -fx-padding: 10 0 5 0;");
                        pastRequestsContainer.getChildren().add(dateHeader);
                        lastRequestDate = currentRequestDate;
                    }
                    pastRequestsContainer.getChildren().add(requestItem);
                    pastRequestsFound = true;
                }
            } catch (IOException e) {
                e.printStackTrace();
                showAlert(Alert.AlertType.ERROR, "Error", "Failed to load request item: " + e.getMessage());
            }
        }

        if (!todayRequestsFound) {
            todayRequestsContainer.getChildren().add(new Label("Tidak ada permintaan maintenance untuk hari ini."));
        }
        if (!pastRequestsFound) {
            pastRequestsContainer.getChildren().add(new Label("Tidak ada permintaan maintenance sebelumnya."));
        }
    }
    // ... di bagian bawah kelas MaintenanceController, di luar metode lain ...

    // PASTIKAN METODE INI ADA
    private void showAlert(Alert.AlertType type, String title, String message) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

// ...

    private void loadHistory() {
        historyDataContainer.getChildren().clear();

        List<Maintenance> historyRecords = allMaintenanceRecords.stream()
                .filter(req -> "Done".equalsIgnoreCase(req.getStatus()))
                .filter(req -> req.getPerformedDate() != null)
                .sorted(Comparator
                    .comparing((Maintenance req) -> req.getRequestDate().toInstant().atZone(ZoneId.systemDefault()).toLocalDate())
                    .thenComparing((Maintenance req) -> {
                        String staffName = req.getRequestName();
                        if (staffName == null || staffName.trim().isEmpty() || "Unassigned".equals(staffName)) {
                            return "ZZZ_Unassigned";
                        }
                        return staffName;
                    })
                    .thenComparing((Maintenance req) -> req.getRequestDate().toInstant().atZone(ZoneId.systemDefault()).toLocalTime()))
                .collect(Collectors.toList());

        LocalDate lastPerformedDate = null;

        if (historyRecords.isEmpty()) {
            historyDataContainer.getChildren().add(new Label("Tidak ada riwayat maintenance."));
            return;
        }

        for (Maintenance record : historyRecords) {
            LocalDate currentPerformedDate = record.getPerformedDate().toInstant().atZone(ZoneId.systemDefault()).toLocalDate();

            if (lastPerformedDate == null || !currentPerformedDate.isEqual(lastPerformedDate)) {
                Label dateHeader = new Label(currentPerformedDate.format(DATE_FORMATTER_DISPLAY));
                dateHeader.setStyle("-fx-font-size: 16px; -fx-font-weight: bold; -fx-padding: 10 0 5 0;");
                historyDataContainer.getChildren().add(dateHeader);
                lastPerformedDate = currentPerformedDate;
            }
            try {
                FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/org/example/exhibitly/Maintenance_Request_Item.fxml"));
                GridPane historyItem = fxmlLoader.load();
                MaintenanceRequestItemController itemController = fxmlLoader.getController();
                itemController.setMaintenanceRequest(record, currentUser);

                // Callback edit juga untuk history jika Kurator bisa edit history
                itemController.setOnEditAction(() -> showEditRequestDialog(record));

                historyDataContainer.getChildren().add(historyItem);
            } catch (IOException e) {
                e.printStackTrace();
                showAlert(Alert.AlertType.ERROR, "Error", "Failed to load history item: " + e.getMessage());
            }
        }
    }

    @FXML
    private void onSubmitNewRequest(ActionEvent event) {
        String selectedArtefactDisplayName = artefactComboBox.getValue();
        // --- PERUBAHAN DI SINI: Requester name diambil dari currentUser ---
        String requesterNameInput = (currentUser != null) ? currentUser.getName() : "Unknown Requester";
        String selectedPerformedByStaffName = performedByStaffComboBox.getValue();
        String description = descriptionArea.getText().trim();

        // Validasi input: requesterNameInput tidak perlu divalidasi emptiness karena diambil otomatis
        if (selectedArtefactDisplayName == null || selectedPerformedByStaffName == null || description.isEmpty()) {
            showAlert(Alert.AlertType.WARNING, "Input Tidak Lengkap", "Artefak, Performed By, dan Deskripsi harus diisi!");
            return;
        }

        // ... (logika ekstraksi Artefact ID dan Nama Artefak tetap sama) ...
        int artefactID = 0;
        String artefactNameForMaintenance = "";
        try {
            int startIndex = selectedArtefactDisplayName.indexOf("(ID: ") + "(ID: ".length();
            int endIndex = selectedArtefactDisplayName.indexOf(")");
            artefactID = Integer.parseInt(selectedArtefactDisplayName.substring(startIndex, endIndex));
            artefactNameForMaintenance = selectedArtefactDisplayName.substring(0, startIndex - "(ID: ".length()).trim();
        } catch (Exception e) {
            showAlert(Alert.AlertType.ERROR, "Error Parsing Artefak ID/Nama", "Format artefak tidak valid. Pilih artefak dari daftar.");
            e.printStackTrace();
            return;
        }

        // ... (logika mencari performedByActorID tetap sama) ...
        Integer performedByActorID = null;
        if (!"Unassigned".equalsIgnoreCase(selectedPerformedByStaffName)) {
            Optional<Staff> chosenStaff = allStaffs.stream()
                    .filter(s -> s.getName().equalsIgnoreCase(selectedPerformedByStaffName))
                    .findFirst();
            if (chosenStaff.isPresent()) {
                performedByActorID = chosenStaff.get().getActorID();
            }
        }

        // ... (logika newMaintenanceID, newRequestID, currentDateTime tetap sama) ...
        int newMaintenanceID = getNextMaintenanceId();
        String newRequestID = UUID.randomUUID().toString().substring(0, 8);
        Date currentDateTime = new Date();

        // --- Buat objek Maintenance (SAMA) ---
        Maintenance newMaintenanceRequest = new Maintenance(
                newMaintenanceID,
                currentUser.getActorID(),
                performedByActorID,
                newRequestID,
                artefactID,
                artefactNameForMaintenance,
                currentDateTime,
                null,
                "Not Done",
                description
        );

        if (saveMaintenanceToDatabase(newMaintenanceRequest)) {
            allMaintenanceRecords.add(newMaintenanceRequest);
            showAlert(Alert.AlertType.INFORMATION, "Sukses", "Permintaan maintenance baru berhasil ditambahkan!");
            
            // ... (kembali ke tampilan permintaan dan bersihkan form) ...
            setAllContentInvisible();
            requestContentDisplay.setVisible(true);
            requestContentDisplay.setManaged(true);
            loadRequests();

            // Bersihkan form:
            artefactComboBox.getSelectionModel().select("Select Artefact");
            // requesterNameField.clear(); <-- HAPUS BARIS INI
            performedByStaffComboBox.getSelectionModel().select("Unassigned");
            descriptionArea.clear();
        } else {
            showAlert(Alert.AlertType.ERROR, "Error", "Gagal menyimpan permintaan maintenance ke database!");
        }
    }

    @FXML
    private void onRequestTabClick(ActionEvent event) {
        requestTabButton.setStyle("-fx-background-color: transparent; -fx-border-color: black; -fx-border-width: 0 0 2 0;");
        historyTabButton.setStyle("-fx-background-color: transparent; -fx-border-color: transparent; -fx-border-width: 0 0 0 0;");

        requestContentDisplay.setVisible(true);
        requestContentDisplay.setManaged(true);

        historyContent.setVisible(false);
        historyContent.setManaged(false);

        addRequestForm.setVisible(false);
        addRequestForm.setManaged(false);
    }

    @FXML
    private void onHistoryTabClick(ActionEvent event) {
        historyTabButton.setStyle("-fx-background-color: transparent; -fx-border-color: black; -fx-border-width: 0 0 2 0;");
        requestTabButton.setStyle("-fx-background-color: transparent; -fx-border-color: transparent; -fx-border-width: 0 0 0 0;");
        requestContentDisplay.setVisible(false);
        requestContentDisplay.setManaged(false);

        historyContent.setVisible(true);
        historyContent.setManaged(true);

        addRequestForm.setVisible(false);
        addRequestForm.setManaged(false);
    }

    @FXML
    private void onAddRequestButtonClick(ActionEvent event) {
        setAllContentInvisible();
        addRequestForm.setVisible(true);
        addRequestForm.setManaged(true);

        // Reset form fields:
        artefactComboBox.getSelectionModel().select("Select Artefact"); // Membersihkan pilihan Artefak ComboBox
        descriptionArea.clear(); // Membersihkan TextArea

        // --- Perubahan untuk Performed By Staff ComboBox (TETAP SAMA) ---
        performedByStaffComboBox.getSelectionModel().select("Unassigned"); // Set default ke Unassigned

        // Hanya Kurator yang bisa memilih staff untuk 'Performed By'
        if (currentUser != null && "Kurator".equalsIgnoreCase(currentUser.getRole())) {
            performedByStaffComboBox.setDisable(false); // Kurator bisa memilih
        } else {
            performedByStaffComboBox.setDisable(true);  // Selain Kurator (termasuk Staff), tidak bisa memilih
        }
    }

    @FXML
    private void onCancelAddRequest(ActionEvent event) {
        setAllContentInvisible();
        requestContentDisplay.setVisible(true);
        requestContentDisplay.setManaged(true);

        // Bersihkan form:
        artefactComboBox.getSelectionModel().select("Select Artefact");
        performedByStaffComboBox.getSelectionModel().select("Unassigned");
        descriptionArea.clear();
    }
    // ... di dalam kelas MaintenanceController, mungkin setelah loadHistory() atau di bagian helper methods ...

    private void setAllContentInvisible() {

        requestContentDisplay.setVisible(false);
        requestContentDisplay.setManaged(false);
        historyContent.setVisible(false);
        historyContent.setManaged(false);
        addRequestForm.setVisible(false);
        addRequestForm.setManaged(false);
    }
    // PASTIKAN METODE INI ADA
    private void showEditRequestDialog(Maintenance requestToEdit) {
        try {
            // Pastikan path FXML ini benar!
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/org/example/exhibitly/EditRequestDialog.fxml"));
            Parent page = loader.load();

            // Create the dialog Stage
            Stage dialogStage = new Stage();
            dialogStage.setTitle("Edit Maintenance Request");
            dialogStage.initModality(Modality.WINDOW_MODAL);
            // Mengatur owner ke primary stage (opsional, tapi bagus untuk fokus)
            // Pastikan 'requestTabButton' sudah diinisialisasi melalui @FXML
            dialogStage.initOwner(((Node) requestTabButton).getScene().getWindow());

            Scene scene = new Scene(page);
            dialogStage.setScene(scene);
            dialogStage.setResizable(false);

            // Get the controller of the dialog and pass data
            EditRequestDialogController requestDialogController = loader.getController();
            requestDialogController.setDialogStage(dialogStage);
            requestDialogController.setRequest(requestToEdit, currentUser); // Mengirim objek Maintenance dan Actor

            // Show the dialog and wait until it is closed
            dialogStage.showAndWait();

            // Setelah dialog ditutup, cek apakah perubahan disimpan
            if (requestDialogController.isSaveClicked()) {
                Maintenance updatedMaintenanceRecords = requestDialogController.getEditedRequest();

                if (updateMaintenanceInDatabase(updatedMaintenanceRecords)) {
                    for (int i = 0; i < allMaintenanceRecords.size(); i++) {
                        if (allMaintenanceRecords.get(i).getMaintenanceID() == updatedMaintenanceRecords.getMaintenanceID()) {           
                                // Set the new maintenance to the existing records.
                                allMaintenanceRecords.set(i, updatedMaintenanceRecords);
                                break; //Keluar loop jikalau maintenance tersebut sudah terupdate (karena hanya 1)
                            }
                    }

                    showAlert(Alert.AlertType.INFORMATION, "Success", "Permintaan berhasil diperbarui.");
                    loadRequests(); // Refresh tampilan "Request"
                    loadHistory(); // Refresh tampilan "History" (jika ada perubahan status ke Done)
                } else {
                    showAlert(Alert.AlertType.ERROR, "Error", "Gagal memperbarui maintenance di database");
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Error", "Gagal membuka form edit: " + e.getMessage());
        }
    }

// ...



    // ===============================================
    // Metode Navigasi Umum (dari header)
    // ===============================================
    @FXML
    private void onLogoButtonClick(ActionEvent event) {
        navigateToPage(event, "/org/example/exhibitly/LandingPage.fxml");
    }

    @FXML
    private void onExhibitButtonClick(ActionEvent event) {
        navigateToPage(event, "/org/example/exhibitly/Exhibit.fxml");
    }

    @FXML
    private void onArtefactButtonClick(ActionEvent event) {
        navigateToPage(event, "/org/example/exhibitly/Artefact.fxml");
    }

    @FXML
    private void onTicketsButtonClick(ActionEvent event) {
        navigateToPage(event, "/org/example/exhibitly/Ticket.fxml");        // loadScene(event, "/org/example/exhibitly/tickets_page.fxml");
    }

    @FXML
    private void onLogoutButtonClick(ActionEvent event) {
        handleLogout(event);
    }

    @FXML
    private void onMaintenanceButtonClick(ActionEvent event) {
        // Sudah di halaman maintenance, tidak perlu ganti scene
        // System.out.println("Already on Maintenance Page");
    }

    // private Maintenance getNewMaintenance() {
        
    //     return newMaintenance;
    // }

    /* -------------------------------------------------------------------------- */
    /*                           New Maintenance Logics                           */
    /* -------------------------------------------------------------------------- */

    private int getNextMaintenanceId() {
        try (Connection connection = new DatabaseConnection().getConnection()) {
            int nextId = 1;
            String maxIDSQL = "SELECT MAX(maintenanceID) FROM Maintenance";

            try (Statement statement = connection.createStatement()) {
                ResultSet resultSet = statement.executeQuery(maxIDSQL);
                if (resultSet.next()) {
                    nextId = resultSet.getInt(1) + 1;
                }
            }
            return nextId;
        } catch (SQLException e) {
            System.err.println("Error getting new maintenance ID: " + e.getMessage());
            e.printStackTrace();

            return allMaintenanceRecords.stream()
                    .mapToInt(Maintenance::getMaintenanceID)
                    .max()
                    .orElse(0) + 1;
        }
    }

    private boolean saveMaintenanceToDatabase(Maintenance maintenance) {
        try (Connection connection = new DatabaseConnection().getConnection()) {
        String entrySQL = """
            INSERT INTO Maintenance (maintenanceid, artefactid, kuratorID, staffID, artefactName, requestDate, performedDate, status, description) 
            VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)
            """;

            try (PreparedStatement preparedStatement = connection.prepareStatement(entrySQL)) {
                preparedStatement.setInt(1, maintenance.getMaintenanceID());
                preparedStatement.setInt(2, maintenance.getArtefactID());
                preparedStatement.setInt(3, currentUser.getActorID());

                if (maintenance.getStaffID() != null) {
                    preparedStatement.setInt(4, maintenance.getStaffID());
                } else {
                    preparedStatement.setNull(4, java.sql.Types.INTEGER);
                }
                
                preparedStatement.setString(5, maintenance.getArtefactName());
                preparedStatement.setDate(6, new java.sql.Date(maintenance.getRequestDate().getTime()));
               
                if (maintenance.getPerformedDate() != null) {
                    preparedStatement.setDate(7, new java.sql.Date(maintenance.getPerformedDate().getTime()));
                } else {
                    preparedStatement.setNull(7, java.sql.Types.DATE);
                }

                preparedStatement.setString(8, maintenance.getStatus());
                preparedStatement.setString(9, maintenance.getDescription());

                int affectedRows = preparedStatement.executeUpdate();
                return affectedRows > 0;
            } 
        } catch (SQLException e) {
            System.err.println("Error saving maintenance to database: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    } 

    private boolean updateMaintenanceInDatabase(Maintenance maintenance) {
        try (Connection connection = new DatabaseConnection().getConnection()) {
        String entrySQL = """
            UPDATE Maintenance 
            SET artefactid = ?, kuratorID = ?, staffID = ?, artefactName = ?, requestDate = ?, performedDate = ?, status = ?, description = ? 
            WHERE maintenanceid = ?
            """;

            try (PreparedStatement preparedStatement = connection.prepareStatement(entrySQL)) {
                preparedStatement.setInt(1, maintenance.getArtefactID());
                preparedStatement.setInt(2, currentUser.getActorID());

                if (maintenance.getStaffID() != null) {
                    preparedStatement.setInt(3, maintenance.getStaffID());
                } else {
                    preparedStatement.setNull(3, java.sql.Types.INTEGER);
                }
                
                preparedStatement.setString(4, maintenance.getArtefactName());
                preparedStatement.setDate(5, new java.sql.Date(maintenance.getRequestDate().getTime()));
               
                if (maintenance.getPerformedDate() != null) {
                    preparedStatement.setDate(6, new java.sql.Date(maintenance.getPerformedDate().getTime()));
                } else {
                    preparedStatement.setNull(6, java.sql.Types.DATE);
                }

                preparedStatement.setString(7, maintenance.getStatus());
                preparedStatement.setString(8, maintenance.getDescription());
                
                preparedStatement.setInt(9, maintenance.getMaintenanceID());

                int affectedRows = preparedStatement.executeUpdate();
                return affectedRows > 0;
            } 
        } catch (SQLException e) {
            System.err.println("Error saving maintenance to database: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }
}