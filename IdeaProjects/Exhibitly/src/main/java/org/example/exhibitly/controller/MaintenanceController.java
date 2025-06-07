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
import javafx.scene.control.TextField;
import javafx.scene.control.TextArea;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.scene.layout.Region; // Import Region
import javafx.stage.Stage;

import org.example.exhibitly.models.Actor;
import org.example.exhibitly.models.Maintenance;
import org.example.exhibitly.models.Staff;
import org.example.exhibitly.models.SessionMangement;

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
    @FXML private Region rightSpacer; // Deklarasikan fx:id untuk spacer

    @FXML private Button requestTabButton;
    @FXML private Button historyTabButton;
    @FXML private VBox requestContentDisplay;
    @FXML private VBox historyContent;
    @FXML private VBox todayRequestsContainer;
    @FXML private VBox pastRequestsContainer;
    @FXML private VBox historyDataContainer;

    @FXML private Label todayDateLabel;

    @FXML private VBox addRequestForm;
    @FXML private TextField artefactNameField;
    @FXML private TextField requesterNameField;
    @FXML private TextArea descriptionArea;
    @FXML private Button addRequestButton;

    @FXML private Button LogoutButton;
    @FXML private Button MaintenanceButton;
    @FXML private Button logoButton;

    private List<Maintenance> allMaintenanceRecords;
    private Actor currentUser;

    private static final SimpleDateFormat TIME_FORMAT = new SimpleDateFormat("HH:mm");
    private static final DateTimeFormatter TODAY_DATE_FORMATTER = DateTimeFormatter.ofPattern("dd MMMMリエステル");
    private static final SimpleDateFormat DATE_FORMAT_DISPLAY = new SimpleDateFormat("dd MMM yyyy"); // Untuk tampilan di History


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        // ... (SAMA SEPERTI SEBELUMNYA: dummy data, inisialisasi SessionManager, dll.) ...

        allMaintenanceRecords = new ArrayList<>();
        Date todayDateUtil = new Date();
        Date yesterdayDateUtil = Date.from(LocalDate.now(ZoneId.systemDefault()).minusDays(1).atStartOfDay(ZoneId.systemDefault()).toInstant());
        Date twoDaysAgoDateUtil = Date.from(LocalDate.now(ZoneId.systemDefault()).minusDays(2).atStartOfDay(ZoneId.systemDefault()).toInstant());
        Date threeDaysAgoDateUtil = Date.from(LocalDate.now(ZoneId.systemDefault()).minusDays(3).atStartOfDay(ZoneId.systemDefault()).toInstant());

        allMaintenanceRecords.add(new Maintenance(1, null, null, UUID.randomUUID().toString().substring(0,8), 101, todayDateUtil, null, "Pembersihan", "Not Done", "Requester: Sendi Putra Alicia\nTolong dibersihin ya"));
        allMaintenanceRecords.add(new Maintenance(2, null, null, UUID.randomUUID().toString().substring(0,8), 102, todayDateUtil, todayDateUtil, "Perbaikan", "Done", "Requester: Joko Santoso\nPerlu perbaikan kecil"));
        allMaintenanceRecords.add(new Maintenance(3, null, null, UUID.randomUUID().toString().substring(0,8), 103, todayDateUtil, null, "Pembersihan", "Not Done", "Requester: Rina Dewi\nDebu terlalu tebal"));
        allMaintenanceRecords.add(new Maintenance(4, null, null, UUID.randomUUID().toString().substring(0,8), 104, yesterdayDateUtil, yesterdayDateUtil, "Rutin", "Done", "Requester: Bambang Wijaya\nPembersihan rutin kemarin"));
        allMaintenanceRecords.add(new Maintenance(5, null, null, UUID.randomUUID().toString().substring(0,8), 105, yesterdayDateUtil, yesterdayDateUtil, "Inspeksi", "Done", "Requester: Siska Putri\nCek stabilitas kemarin"));
        allMaintenanceRecords.add(new Maintenance(6, null, null, UUID.randomUUID().toString().substring(0,8), 106, yesterdayDateUtil, null, "Pembersihan", "Not Done", "Requester: Andi Setiawan\nPerlu penanganan khusus kemarin"));
        allMaintenanceRecords.add(new Maintenance(8, null, null, UUID.randomUUID().toString().substring(0,8), 108, threeDaysAgoDateUtil, null, "Perbaikan", "Not Done", "Requester: Citra Ayu\nSensor tidak berfungsi"));
        allMaintenanceRecords.add(new Maintenance(9, null, null, UUID.randomUUID().toString().substring(0,8), 109, threeDaysAgoDateUtil, null, "Cek", "Not Done", "Requester: Bayu Firmansyah\nLampu display mati"));
        allMaintenanceRecords.add(new Maintenance(7, null, null, UUID.randomUUID().toString().substring(0,8), 107, twoDaysAgoDateUtil, twoDaysAgoDateUtil, "Perbaikan", "Done", "Requester: Dedi Kusuma\nDisplay kusam (Done)"));

        currentUser = currentUser = new Actor(1, "ardystaff", "password123", "Stanislaus Ardy Bramantyo", "Staff"); // Contoh Staff
        updateUserInfo();
        loadRequests();
        loadHistory();

        todayDateLabel.setText("Today - " + LocalDate.now(ZoneId.systemDefault()).format(TODAY_DATE_FORMATTER));

        requestTabButton.setStyle("-fx-background-color: transparent; -fx-border-color: black; -fx-border-width: 0 0 2 0;");
        historyTabButton.setStyle("-fx-background-color: transparent; -fx-border-color: transparent; -fx-border-width: 0 0 0 0;");
        requestContentDisplay.setVisible(true);
        historyContent.setVisible(false);
        addRequestForm.setVisible(false);
    }

    private void updateUserInfo() {
        if (currentUser != null) {
            userNameLabel.setText(currentUser.getName() + "!");
            userRoleLabel.setText("(" + currentUser.getRole() + ")");

            // Tampilkan atau sembunyikan jadwal rutin dan tombol add request berdasarkan role
            if ("Staff".equalsIgnoreCase(currentUser.getRole())) {
                routineScheduleSection.setVisible(true);
                //addRequestButton.setVisible(false); // Staff tidak perlu tombol add request karena mereka menangani, bukan request
            } else { // Public/Visitor/Other roles can add request
                routineScheduleSection.setVisible(false);
                //addRequestButton.setVisible(true);
            }
        }
    }

    private void loadRequests() {
        todayRequestsContainer.getChildren().clear();
        pastRequestsContainer.getChildren().clear();
        LocalDate todayLocalDate = LocalDate.now(ZoneId.systemDefault());

        // Filter dan Urutkan permintaan yang belum selesai
        List<Maintenance> openRequests = allMaintenanceRecords.stream()
                .filter(req -> !"Done".equalsIgnoreCase(req.getStatus())) // Hanya tampilkan yang belum Done
                .sorted(Comparator
                        // Urutkan berdasarkan tanggal request (terbaru duluan)
                        .comparing((Maintenance req) -> req.getRequestDate().toInstant().atZone(ZoneId.systemDefault()).toLocalDate(), Comparator.reverseOrder())
                        // Kemudian berdasarkan waktu request (terbaru duluan)
                        .thenComparing((Maintenance req) -> TIME_FORMAT.format(req.getRequestDate()), Comparator.reverseOrder()))
                .collect(Collectors.toList());

        for (Maintenance request : openRequests) {
            try {
                FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/org/example/exhibitly/maintenance_request_item.fxml"));
                GridPane requestItem = fxmlLoader.load();

                // Fill data into the loaded item
                ((Label) requestItem.lookup("#timeLabel")).setText(TIME_FORMAT.format(request.getRequestDate())); // Gunakan waktu dari requestDate
                ((Label) requestItem.lookup("#artefactNameLabel")).setText("Artefak " + request.getArtefactID()); // Menggunakan artefactID sebagai placeholder
                // Asumsi requesterName ada di deskripsi atau field lain, atau kita bisa pakai current user name
                // Untuk demo, kita pakai nama current user jika request baru
                ((Label) requestItem.lookup("#requesterNameLabel")).setText(request.getRequestID().substring(0,8) + " (Req ID)"); // Placeholder, bisa diganti dengan nama asli requester
                ((Label) requestItem.lookup("#descriptionLabel")).setText(request.getDescription());
                ((Label) requestItem.lookup("#statusLabel")).setText(request.getStatus());

                // Group by date
                if (request.getRequestDate().toInstant().atZone(ZoneId.systemDefault()).toLocalDate().isEqual(todayLocalDate)) {
                    todayRequestsContainer.getChildren().add(requestItem);
                } else {
                    // Anda mungkin ingin menambahkan label tanggal di sini jika tanggalnya berubah
                    pastRequestsContainer.getChildren().add(requestItem);
                }

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        // If no requests, display a message
        if (todayRequestsContainer.getChildren().isEmpty()) {
            todayRequestsContainer.getChildren().add(new Label("Tidak ada permintaan maintenance untuk hari ini."));
        }
        if (pastRequestsContainer.getChildren().isEmpty()) {
            pastRequestsContainer.getChildren().add(new Label("Tidak ada permintaan maintenance sebelumnya."));
        }
    }

    private void loadHistory() {
        historyDataContainer.getChildren().clear();

        List<Maintenance> historyRecords = allMaintenanceRecords.stream()
                .filter(req -> "Done".equalsIgnoreCase(req.getStatus())) // Filter for 'Done' status
                .sorted(Comparator
                        // Urutkan berdasarkan tanggal performed (terbaru duluan)
                        .comparing((Maintenance req) -> req.getPerformedDate().toInstant().atZone(ZoneId.systemDefault()).toLocalDate(), Comparator.reverseOrder())
                        // Kemudian berdasarkan waktu performed (terbaru duluan)
                        .thenComparing((Maintenance req) -> TIME_FORMAT.format(req.getPerformedDate()), Comparator.reverseOrder()))
                .collect(Collectors.toList());

        for (Maintenance record : historyRecords) {
            try {
                FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/org/example/exhibitly/maintenance_request_item.fxml"));
                GridPane historyItem = fxmlLoader.load();

                // Fill data into the loaded item
                ((Label) historyItem.lookup("#timeLabel")).setText(DATE_FORMAT_DISPLAY.format(record.getPerformedDate()) + " " + TIME_FORMAT.format(record.getPerformedDate())); // Tampilkan tanggal & waktu
                ((Label) historyItem.lookup("#artefactNameLabel")).setText("Artefak " + record.getArtefactID()); // Menggunakan artefactID sebagai placeholder
                ((Label) historyItem.lookup("#requesterNameLabel")).setText(record.getRequestID() != null ? record.getRequestID().substring(0,8) + " (Req ID)" : "N/A"); // RequestID
                ((Label) historyItem.lookup("#descriptionLabel")).setText(record.getDescription());
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


    // ... (Metode loadRequests(), loadHistory(), onSubmitNewRequest(), onCancelAddRequest(), onAddRequestButtonClick() sama seperti sebelumnya) ...
    // Pastikan metode navigateToPage dan metode navigasi lainnya juga sudah diupdate

    private void navigateToPage(ActionEvent event, String path) {
        String pageName = path.substring(path.lastIndexOf('/') + 1).replace(".fxml", "");
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(path));
            Parent root = loader.load();

            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            Scene scene = new Scene(root, 1366, 768);
            scene.getStylesheets().add(getClass().getResource("/css/style.css").toExternalForm());

            stage.setScene(scene);
            stage.setTitle("Museum Nusantara - " + pageName);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            System.err.println("Gagal memuat halaman " + pageName + ": " + e.getMessage());
        }
    }

    // Metode untuk tombol navigasi header (jika di MaintenanceController)
    @FXML
    private void onExhibitButtonClick(ActionEvent event) {
        navigateToPage(event, "/org/example/exhibitly/exhibit_page.fxml");
    }

    @FXML
    private void onArtefactButtonClick(ActionEvent event) {
        navigateToPage(event, "/org/example/exhibitly/artefact_page.fxml");
    }

    @FXML
    private void onTicketsButtonClick(ActionEvent event) {
        navigateToPage(event, "/org/example/exhibitly/tickets_page.fxml");
    }

    @FXML
    private void onLogoButtonClick(ActionEvent event) {
        navigateToPage(event, "/org/example/exhibitly/landing_page.fxml");
    }

    @FXML
    private void onMaintenanceButtonClick(ActionEvent event) {
        // Sudah di halaman maintenance, tidak perlu navigasi
    }

    @FXML
    private void onLogoutButtonClick(ActionEvent event) throws IOException {
    }

    @FXML
    private void onRequestTabClick(ActionEvent event) {
        requestContentDisplay.setVisible(true);
        historyContent.setVisible(false);
        addRequestForm.setVisible(false); // Pastikan form tertutup saat pindah tab
        requestTabButton.setStyle("-fx-background-color: transparent; -fx-border-color: black; -fx-border-width: 0 0 2 0;");
        historyTabButton.setStyle("-fx-background-color: transparent; -fx-border-color: transparent; -fx-border-width: 0 0 0 0;");
    }

    @FXML
    private void onHistoryTabClick(ActionEvent event) {
        requestContentDisplay.setVisible(false);
        historyContent.setVisible(true);
        addRequestForm.setVisible(false); // Pastikan form tertutup saat pindah tab
        historyTabButton.setStyle("-fx-background-color: transparent; -fx-border-color: black; -fx-border-width: 0 0 2 0;");
        requestTabButton.setStyle("-fx-background-color: transparent; -fx-border-color: transparent; -fx-border-width: 0 0 0 0;");
    }

    @FXML
    private void onAddRequestButtonClick(ActionEvent event) {
        requestContentDisplay.setVisible(false);
        historyContent.setVisible(false);
        addRequestForm.setVisible(true);
    }

    @FXML
    private void onCancelAddRequest(ActionEvent event) {
        addRequestForm.setVisible(false);
        requestContentDisplay.setVisible(true);
        // Clear fields
        artefactNameField.clear();
        requesterNameField.clear();
        descriptionArea.clear();
    }
}