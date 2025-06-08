package org.example.exhibitly.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
// import javafx.scene.layout.Region; // Hapus atau biarkan terkomentar jika Region tidak lagi digunakan
import javafx.stage.Modality;
import javafx.stage.Stage;

import org.example.exhibitly.models.Actor;
import org.example.exhibitly.models.Maintenance;
import org.example.exhibitly.models.Staff;
import org.example.exhibitly.models.DummyStaffData; // Pastikan ini diimpor jika SessionMangement dihapus dan diganti dummy
// import org.example.exhibitly.models.SessionMangement; // Hapus atau komentari jika tidak digunakan

import java.io.IOException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.ResourceBundle;
import java.util.UUID;
import java.util.stream.Collectors;

public class MaintenanceController implements Initializable {

    @FXML private Label userNameLabel;
    @FXML private Label userRoleLabel;
    @FXML private VBox routineScheduleSection;
    @FXML private Label jadwalPembersihanLabel;
    // @FXML private Region rightSpacer; // Biarkan ini dikomentari atau hapus jika memang tidak ada di FXML

    @FXML private Button requestTabButton;
    @FXML private Button historyTabButton;
    @FXML private VBox requestContentDisplay;
    @FXML private VBox historyContent;
    @FXML private VBox todayRequestsContainer;
    @FXML private VBox pastRequestsContainer;
    @FXML private VBox historyDataContainer;

    @FXML private Label todayDateLabel;

    @FXML private Button addRequestButton;

    @FXML private Button LogoutButton;
    @FXML private Button MaintenanceButton;
    @FXML private Button logoButton;

    private List<Maintenance> allMaintenanceRecords;
    private Actor currentUser;

    private static final SimpleDateFormat TIME_FORMAT = new SimpleDateFormat("HH:mm");
    private static final DateTimeFormatter TODAY_DATE_FORMATTER = DateTimeFormatter.ofPattern("dd MMMM yyyy"); // Format disamakan

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        allMaintenanceRecords = new ArrayList<>();
        // ... (Dummy data tetap seperti sebelumnya) ...
        Date todayDateUtil = new Date();
        Date yesterdayDateUtil = Date.from(LocalDate.now(ZoneId.systemDefault()).minusDays(1).atStartOfDay(ZoneId.systemDefault()).toInstant());
        Date twoDaysAgoDateUtil = Date.from(LocalDate.now(ZoneId.systemDefault()).minusDays(2).atStartOfDay(ZoneId.systemDefault()).toInstant());
        Date threeDaysAgoDateUtil = Date.from(LocalDate.now(ZoneId.systemDefault()).minusDays(3).atStartOfDay(ZoneId.systemDefault()).toInstant());

        allMaintenanceRecords.add(new Maintenance(1, null, null, UUID.randomUUID().toString().substring(0,8), 101, todayDateUtil, null, "Pembersihan", "Not Done", "Requester: Sendi Putra Alicia\nTargeted Staff: Stanislaus Ardy Bramantyo\nTolong dibersihin ya"));
        allMaintenanceRecords.add(new Maintenance(2, null, null, UUID.randomUUID().toString().substring(0,8), 102, todayDateUtil, todayDateUtil, "Perbaikan", "Done", "Requester: Joko Santoso\nTargeted Staff: Budi Santoso\nPerlu perbaikan kecil"));
        allMaintenanceRecords.add(new Maintenance(3, null, null, UUID.randomUUID().toString().substring(0,8), 103, todayDateUtil, null, "Pembersihan", "Not Done", "Requester: Rina Dewi\nTargeted Staff: Citra Dewi\nDebu terlalu tebal"));
        allMaintenanceRecords.add(new Maintenance(4, null, null, UUID.randomUUID().toString().substring(0,8), 104, yesterdayDateUtil, yesterdayDateUtil, "Rutin", "Done", "Requester: Bambang Wijaya\nTargeted Staff: Denny Wijaya\nPembersihan rutin kemarin"));
        allMaintenanceRecords.add(new Maintenance(5, null, null, UUID.randomUUID().toString().substring(0,8), 105, yesterdayDateUtil, yesterdayDateUtil, "Inspeksi", "Done", "Requester: Siska Putri\nTargeted Staff: Eka Putri\nCek stabilitas kemarin"));
        allMaintenanceRecords.add(new Maintenance(6, null, null, UUID.randomUUID().toString().substring(0,8), 106, yesterdayDateUtil, null, "Pembersihan", "Not Done", "Requester: Andi Setiawan\nTargeted Staff: Stanislaus Ardy Bramantyo, Budi Santoso\nPerlu penanganan khusus kemarin"));
        allMaintenanceRecords.add(new Maintenance(8, null, null, UUID.randomUUID().toString().substring(0,8), 108, threeDaysAgoDateUtil, null, "Perbaikan", "Not Done", "Requester: Citra Ayu\nTargeted Staff: Citra Dewi\nSensor tidak berfungsi"));
        allMaintenanceRecords.add(new Maintenance(9, null, null, UUID.randomUUID().toString().substring(0,8), 109, threeDaysAgoDateUtil, null, "Cek", "Not Done", "Requester: Bayu Firmansyah\nTargeted Staff: Denny Wijaya\nLampu display mati"));
        allMaintenanceRecords.add(new Maintenance(7, null, null, UUID.randomUUID().toString().substring(0,8), 107, twoDaysAgoDateUtil, twoDaysAgoDateUtil, "Perbaikan", "Done", "Requester: Dedi Kusuma\nTargeted Staff: Eka Putri\nDisplay kusam (Done)"));

        // Gunakan DummyStaffData untuk mendapatkan currentUser (Stanislaus Ardy Bramantyo)
        currentUser = new Staff(1, "ardystaff", "password123", "Stanislaus Ardy Bramantyo", "Setiap Hari, 09.00 - 15.00");
        // Jika Anda ingin menguji role staff, bisa diinisialisasi di sini atau pastikan login sebagai Staff
        // currentUser = new Staff(1, "ardystaff", "password123", "Stanislaus Ardy Bramantyo", "Staff", "Setiap Hari, 09.00 - 15.00");
        // currentUser = new Actor(10, "curator", "pass", "Jane Doe", "Curator"); // Untuk role lain

        updateUserInfo();
        loadRequests();
        loadHistory();

        todayDateLabel.setText("Today - " + LocalDate.now(ZoneId.systemDefault()).format(TODAY_DATE_FORMATTER));

        requestTabButton.setStyle("-fx-background-color: transparent; -fx-border-color: black; -fx-border-width: 0 0 2 0;");
        historyTabButton.setStyle("-fx-background-color: transparent; -fx-border-color: transparent; -fx-border-width: 0 0 0 0;");
        requestContentDisplay.setVisible(true);
        historyContent.setVisible(false);
    }

    private void updateUserInfo() {
        if (currentUser != null) {
            userNameLabel.setText(currentUser.getRole() + "!");
            userRoleLabel.setText("(" + currentUser.getName() + ")");

            boolean isStaff = "Staff".equalsIgnoreCase(currentUser.getRole());

            routineScheduleSection.setVisible(isStaff);
            routineScheduleSection.setManaged(isStaff);

            // Hapus atau komentari baris ini karena rightSpacer tidak ada lagi di FXML
            // if (rightSpacer != null) { // if block tidak perlu jika @FXML dideklarasikan
            //     rightSpacer.setVisible(!isStaff);
            //     rightSpacer.setManaged(!isStaff);
            // }

            // Add Request button hanya untuk Non-Staff (Public/Curator)
            addRequestButton.setVisible(!isStaff);
            addRequestButton.setManaged(!isStaff); // Tambahkan ini agar tombol tidak memakan ruang
            addRequestButton.setText("Add Request"); // Mengatur teks tombol untuk pop-up

            if (isStaff && currentUser instanceof Staff) {
                Staff staff = (Staff) currentUser;
                if (jadwalPembersihanLabel != null) {
                    jadwalPembersihanLabel.setText(staff.getJadwalPemeliharaan());
                }
            }
        } else {
            // Ini penting: Jika currentUser null, ini bisa menjadi penyebab (null) di UI Anda
            userNameLabel.setText("Guest");
            userRoleLabel.setText("(Undefined Role)");
            routineScheduleSection.setVisible(false);
            routineScheduleSection.setManaged(false);
            addRequestButton.setVisible(true); // Default to visible for guest if no user
            addRequestButton.setManaged(true);
        }
    }

    // ... (Sisa metode onAddRequestButtonClick, loadRequests, loadHistory, navigasi tidak berubah) ...

    @FXML
    public void onAddRequestButtonClick(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/org/example/exhibitly/add_maintenance_request_popup.fxml"));
            Parent root = loader.load();

            AddMaintenanceRequestPopupController popupController = loader.getController();

            Stage popupStage = new Stage();
            popupStage.initModality(Modality.APPLICATION_MODAL);
            popupStage.initOwner(((Node) event.getSource()).getScene().getWindow());
            popupStage.setTitle("Add New Maintenance Request");

            Scene popupScene = new Scene(root);
            // Hanya tambahkan stylesheet jika pathnya benar dan file ada
            // if (getClass().getResource("/css/style.css") != null) {
            //     popupScene.getStylesheets().add(getClass().getResource("/css/style.css").toExternalForm());
            // }

            popupStage.setScene(popupScene);
            popupController.setDialogStage(popupStage);
            popupStage.showAndWait();

            Maintenance newRequest = popupController.getNewMaintenanceRequest();
            if (newRequest != null) {
                allMaintenanceRecords.add(newRequest);
                System.out.println("New maintenance request submitted: " + newRequest);
                loadRequests();
                onRequestTabClick(null);
            }
        } catch (IOException e) {
            e.printStackTrace();
            System.err.println("Gagal memuat pop-up Add Maintenance Request: " + e.getMessage());
        }
    }

    private void loadRequests() {
        todayRequestsContainer.getChildren().clear();
        pastRequestsContainer.getChildren().clear();
        LocalDate todayLocalDate = LocalDate.now(ZoneId.systemDefault());

        List<Maintenance> openRequests = allMaintenanceRecords.stream()
                .filter(req -> !"Done".equalsIgnoreCase(req.getStatus()))
                .sorted(Comparator
                        .comparing((Maintenance req) -> req.getRequestDate().toInstant().atZone(ZoneId.systemDefault()).toLocalDate(), Comparator.reverseOrder())
                        .thenComparing((Maintenance req) -> req.getRequestDate(), Comparator.reverseOrder()))
                .collect(Collectors.toList());

        LocalDate lastRequestDate = null;

        for (Maintenance request : openRequests) {
            LocalDate currentRequestDate = request.getRequestDate().toInstant().atZone(ZoneId.systemDefault()).toLocalDate();

            if (currentRequestDate.isEqual(todayLocalDate)) {
                try {
                    FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/org/example/exhibitly/maintenance_request_item.fxml"));
                    GridPane requestItem = fxmlLoader.load();
                    ((Label) requestItem.lookup("#timeLabel")).setText(TIME_FORMAT.format(request.getRequestDate()));
                    ((Label) requestItem.lookup("#artefactNameLabel")).setText("Artefak ID: " + request.getArtefactID());

                    String[] parts = request.getDescription().split("\n", 3);
                    String requesterName = "N/A";
                    String descriptionContent = request.getDescription();
                    if (parts.length > 0 && parts[0].startsWith("Requester: ")) {
                        requesterName = parts[0].replace("Requester: ", "").trim();
                    }
                    if (parts.length > 2) {
                        descriptionContent = parts[2];
                    } else if (parts.length == 2 && !parts[1].startsWith("Targeted Staff:")) {
                        descriptionContent = parts[1];
                    } else if (parts.length == 2 && parts[1].startsWith("Targeted Staff:")) {
                        descriptionContent = "No description provided.";
                    }

                    ((Label) requestItem.lookup("#requesterNameLabel")).setText(requesterName);
                    ((Label) requestItem.lookup("#descriptionLabel")).setText(descriptionContent);
                    ((Label) requestItem.lookup("#statusLabel")).setText(request.getStatus());
                    todayRequestsContainer.getChildren().add(requestItem);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            } else {
                if (lastRequestDate == null || !currentRequestDate.isEqual(lastRequestDate)) {
                    Label dateHeader = new Label(currentRequestDate.format(TODAY_DATE_FORMATTER));
                    dateHeader.setStyle("-fx-font-size: 16px; -fx-font-weight: bold; -fx-padding: 10 0 5 0;");
                    pastRequestsContainer.getChildren().add(dateHeader);
                    lastRequestDate = currentRequestDate;
                }
                try {
                    FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/org/example/exhibitly/maintenance_request_item.fxml"));
                    GridPane requestItem = fxmlLoader.load();
                    ((Label) requestItem.lookup("#timeLabel")).setText(TIME_FORMAT.format(request.getRequestDate()));
                    ((Label) requestItem.lookup("#artefactNameLabel")).setText("Artefak ID: " + request.getArtefactID());
                    String[] parts = request.getDescription().split("\n", 3);
                    String requesterName = "N/A";
                    String descriptionContent = request.getDescription();
                    if (parts.length > 0 && parts[0].startsWith("Requester: ")) {
                        requesterName = parts[0].replace("Requester: ", "").trim();
                    }
                    if (parts.length > 2) {
                        descriptionContent = parts[2];
                    } else if (parts.length == 2 && !parts[1].startsWith("Targeted Staff:")) {
                        descriptionContent = parts[1];
                    } else if (parts.length == 2 && parts[1].startsWith("Targeted Staff:")) {
                        descriptionContent = "No description provided.";
                    }

                    ((Label) requestItem.lookup("#requesterNameLabel")).setText(requesterName);
                    ((Label) requestItem.lookup("#descriptionLabel")).setText(descriptionContent);
                    ((Label) requestItem.lookup("#statusLabel")).setText(request.getStatus());
                    pastRequestsContainer.getChildren().add(requestItem);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        if (todayRequestsContainer.getChildren().isEmpty()) {
            todayRequestsContainer.getChildren().add(new Label("Tidak ada permintaan maintenance untuk hari ini."));
        }
        if (pastRequestsContainer.getChildren().isEmpty() && openRequests.stream().noneMatch(req -> !req.getRequestDate().toInstant().atZone(ZoneId.systemDefault()).toLocalDate().isEqual(todayLocalDate))) {
            pastRequestsContainer.getChildren().add(new Label("Tidak ada permintaan maintenance sebelumnya."));
        }
    }

    private void loadHistory() {
        historyDataContainer.getChildren().clear();

        List<Maintenance> historyRecords = allMaintenanceRecords.stream()
                .filter(req -> "Done".equalsIgnoreCase(req.getStatus()))
                .sorted(Comparator
                        .comparing((Maintenance req) -> req.getPerformedDate().toInstant().atZone(ZoneId.systemDefault()).toLocalDate(), Comparator.reverseOrder())
                        .thenComparing((Maintenance req) -> req.getPerformedDate(), Comparator.reverseOrder()))
                .collect(Collectors.toList());

        LocalDate lastPerformedDate = null;

        for (Maintenance record : historyRecords) {
            LocalDate currentPerformedDate = record.getPerformedDate().toInstant().atZone(ZoneId.systemDefault()).toLocalDate();

            if (lastPerformedDate == null || !currentPerformedDate.isEqual(lastPerformedDate)) {
                Label dateHeader = new Label(currentPerformedDate.format(TODAY_DATE_FORMATTER));
                dateHeader.setStyle("-fx-font-size: 16px; -fx-font-weight: bold; -fx-padding: 10 0 5 0;");
                historyDataContainer.getChildren().add(dateHeader);
                lastPerformedDate = currentPerformedDate;
            }
            try {
                FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/org/example/exhibitly/maintenance_request_item.fxml"));
                GridPane historyItem = fxmlLoader.load();

                ((Label) historyItem.lookup("#timeLabel")).setText(TIME_FORMAT.format(record.getPerformedDate()));
                ((Label) historyItem.lookup("#artefactNameLabel")).setText("Artefak ID: " + record.getArtefactID());
                String[] parts = record.getDescription().split("\n", 3);
                String requesterName = "N/A";
                String descriptionContent = record.getDescription();
                if (parts.length > 0 && parts[0].startsWith("Requester: ")) {
                    requesterName = parts[0].replace("Requester: ", "").trim();
                }
                if (parts.length > 2) {
                    descriptionContent = parts[2];
                } else if (parts.length == 2 && !parts[1].startsWith("Targeted Staff:")) {
                    descriptionContent = parts[1];
                }

                ((Label) historyItem.lookup("#requesterNameLabel")).setText(requesterName);
                ((Label) historyItem.lookup("#descriptionLabel")).setText(descriptionContent);
                ((Label) historyItem.lookup("#statusLabel")).setText(record.getStatus());

                historyDataContainer.getChildren().add(historyItem);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        if (historyDataContainer.getChildren().isEmpty()) {
            historyDataContainer.getChildren().add(new Label("Tidak ada riwayat maintenance."));
        }
    }
    @FXML
    public void onRequestTabClick(ActionEvent event) {
        requestTabButton.setStyle("-fx-background-color: transparent; -fx-border-color: black; -fx-border-width: 0 0 2 0;");
        historyTabButton.setStyle("-fx-background-color: transparent; -fx-border-color: transparent; -fx-border-width: 0 0 0 0;");
        requestContentDisplay.setVisible(true);
        historyContent.setVisible(false);
    }

    @FXML
    public void onHistoryTabClick(ActionEvent event) {
        historyTabButton.setStyle("-fx-background-color: transparent; -fx-border-color: black; -fx-border-width: 0 0 2 0;");
        requestTabButton.setStyle("-fx-background-color: transparent; -fx-border-color: transparent; -fx-border-width: 0 0 0 0;");
        requestContentDisplay.setVisible(false);
        historyContent.setVisible(true);
    }

    // Metode navigasi lainnya (jika ada dan tidak berubah)
    @FXML
    private void onLogoutButtonClick(ActionEvent event) {
        try {
            Parent loginParent = FXMLLoader.load(getClass().getResource("/org/example/exhibitly/login_page.fxml")); // Sesuaikan path FXML Anda
            Scene loginScene = new Scene(loginParent);
            Stage window = (Stage)((Node)event.getSource()).getScene().getWindow();
            window.setScene(loginScene);
            window.show();
        } catch (IOException e) {
            e.printStackTrace();
            System.err.println("Gagal memuat halaman login: " + e.getMessage());
        }
    }

    @FXML
    private void onMaintenanceButtonClick(ActionEvent event) {
        // Sudah di halaman maintenance, tidak perlu melakukan apa-apa
        // Atau bisa refresh halaman jika diinginkan
    }

    @FXML
    private void onLogoButtonClick(ActionEvent event) {
        try {
            Parent homeParent = FXMLLoader.load(getClass().getResource("/org/example/exhibitly/home_page.fxml")); // Sesuaikan path FXML Anda
            Scene homeScene = new Scene(homeParent);
            Stage window = (Stage)((Node)event.getSource()).getScene().getWindow();
            window.setScene(homeScene);
            window.show();
        } catch (IOException e) {
            e.printStackTrace();
            System.err.println("Gagal memuat halaman home: " + e.getMessage());
        }
    }

    // placeholder methods for FXML onAction that might be present
    @FXML
    public void onExhibitButtonClick(ActionEvent actionEvent) {
        System.out.println("Exhibit button clicked!");
        // Tambahkan navigasi ke halaman Exhibit di sini
    }

    @FXML
    public void onArtefactButtonClick(ActionEvent actionEvent) {
        System.out.println("Artefact button clicked!");
        // Tambahkan navigasi ke halaman Artefact di sini
    }

    @FXML
    public void onTicketsButtonClick(ActionEvent actionEvent) {
        System.out.println("Tickets button clicked!");
        // Tambahkan navigasi ke halaman Tickets di sini
    }
}