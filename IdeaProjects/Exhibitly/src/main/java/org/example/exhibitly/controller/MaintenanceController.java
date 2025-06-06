package org.example.exhibitly.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.TextArea;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import org.example.exhibitly.models.Actor;
import org.example.exhibitly.models.Maintenance;
import org.example.exhibitly.models.Staff;

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

public class MaintenanceController extends BaseController implements Initializable {

    // ... (Elemen FXML yang sama seperti sebelumnya) ...
    @FXML private Label userNameLabel;
    @FXML private Label userRoleLabel;
    @FXML private VBox routineScheduleSection;
    @FXML private Label jadwalPembersihanLabel;

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

    // ... (Elemen FXML header/footer lainnya) ...

    private List<Maintenance> allMaintenanceRecords;
    private Actor currentUser;

    private static final SimpleDateFormat TIME_FORMAT = new SimpleDateFormat("HH:mm");
    private static final SimpleDateFormat DATE_FORMAT_DISPLAY_FULL = new SimpleDateFormat("dd MMMM yyyy"); // Untuk header tanggal
    private static final DateTimeFormatter TODAY_DATE_FORMATTER = DateTimeFormatter.ofPattern("dd MMMM yyyy"); // Mengubah ini agar konsisten

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        if (!session.isLoggedIn() || !session.isStaff()) {
            return;
        }

        allMaintenanceRecords = new ArrayList<>();
        Date todayDateUtil = new Date(); // java.util.Date untuk dummy data
        Date yesterdayDateUtil = Date.from(LocalDate.now(ZoneId.systemDefault()).minusDays(1).atStartOfDay(ZoneId.systemDefault()).toInstant());
        Date twoDaysAgoDateUtil = Date.from(LocalDate.now(ZoneId.systemDefault()).minusDays(2).atStartOfDay(ZoneId.systemDefault()).toInstant());


        // Initial dummy data
        // Untuk contoh ini, saya tambahkan beberapa tanggal berbeda di 'Past Requests'
        allMaintenanceRecords.add(new Maintenance(1, null, null, UUID.randomUUID().toString().substring(0,8), 101, todayDateUtil, null, "Pembersihan", "Not Done", "Tolong dibersihin ya"));
        allMaintenanceRecords.add(new Maintenance(2, null, null, UUID.randomUUID().toString().substring(0,8), 102, todayDateUtil, todayDateUtil, "Perbaikan", "Done", "Perlu perbaikan kecil"));
        allMaintenanceRecords.add(new Maintenance(3, null, null, UUID.randomUUID().toString().substring(0,8), 103, todayDateUtil, null, "Pembersihan", "Not Done", "Debu terlalu tebal"));

        allMaintenanceRecords.add(new Maintenance(4, null, null, UUID.randomUUID().toString().substring(0,8), 104, yesterdayDateUtil, yesterdayDateUtil, "Rutin", "Done", "Pembersihan rutin kemarin"));
        allMaintenanceRecords.add(new Maintenance(5, null, null, UUID.randomUUID().toString().substring(0,8), 105, yesterdayDateUtil, yesterdayDateUtil, "Inspeksi", "Done", "Cek stabilitas kemarin"));
        allMaintenanceRecords.add(new Maintenance(6, null, null, UUID.randomUUID().toString().substring(0,8), 106, yesterdayDateUtil, null, "Pembersihan", "Not Done", "Perlu penanganan khusus kemarin"));

        Date threeDaysAgoDateUtil = Date.from(LocalDate.now(ZoneId.systemDefault()).minusDays(3).atStartOfDay(ZoneId.systemDefault()).toInstant());
        allMaintenanceRecords.add(new Maintenance(8, null, null, UUID.randomUUID().toString().substring(0,8), 108, threeDaysAgoDateUtil, null, "Perbaikan", "Not Done", "Sensor tidak berfungsi"));
        allMaintenanceRecords.add(new Maintenance(9, null, null, UUID.randomUUID().toString().substring(0,8), 109, threeDaysAgoDateUtil, null, "Cek", "Not Done", "Lampu display mati"));


        allMaintenanceRecords.add(new Maintenance(7, null, null, UUID.randomUUID().toString().substring(0,8), 107, twoDaysAgoDateUtil, twoDaysAgoDateUtil, "Perbaikan", "Done", "Display kusam (Done)"));


        // Simulate user login
        currentUser = new Staff(1, "ardystaff", "password123", "Stanislaus Ardy Bramantyo", "Setiap Hari, 09.00 - 15.00");

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
                        .comparing((Maintenance req) -> req.getRequestDate().toInstant().atZone(ZoneId.systemDefault()).toLocalDate(), Comparator.reverseOrder())
                        .thenComparing((Maintenance req) -> req.getRequestDate(), Comparator.reverseOrder())) // Urutkan juga berdasarkan waktu request
                .collect(Collectors.toList());

        LocalDate lastRequestDate = null; // Untuk melacak tanggal terakhir yang diproses

        for (Maintenance request : openRequests) {
            LocalDate currentRequestDate = request.getRequestDate().toInstant().atZone(ZoneId.systemDefault()).toLocalDate();

            // Jika tanggalnya hari ini, masukkan ke todayRequestsContainer
            if (currentRequestDate.isEqual(todayLocalDate)) {
                try {
                    FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/org/example/exhibitly/maintenance_request_item.fxml"));
                    GridPane requestItem = fxmlLoader.load();
                    ((Label) requestItem.lookup("#timeLabel")).setText(TIME_FORMAT.format(request.getRequestDate()));
                    ((Label) requestItem.lookup("#artefactNameLabel")).setText("Artefak " + request.getArtefactID());
                    // Asumsi requesterName ada di deskripsi atau field lain
                    ((Label) requestItem.lookup("#requesterNameLabel")).setText(request.getDescription().split("\n")[0].replace("Requester: ", "").trim()); // Ambil nama requester dari deskripsi
                    ((Label) requestItem.lookup("#descriptionLabel")).setText(request.getDescription().split("\n").length > 1 ? request.getDescription().split("\n")[1] : request.getDescription()); // Sisanya deskripsi
                    ((Label) requestItem.lookup("#statusLabel")).setText(request.getStatus());
                    todayRequestsContainer.getChildren().add(requestItem);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            } else {
                // Untuk permintaan yang bukan hari ini, tambahkan header tanggal jika tanggalnya berbeda dari yang sebelumnya
                if (lastRequestDate == null || !currentRequestDate.isEqual(lastRequestDate)) {
                    Label dateHeader = new Label(currentRequestDate.format(TODAY_DATE_FORMATTER)); // Gunakan format tanggal penuh
                    dateHeader.setStyle("-fx-font-size: 16px; -fx-font-weight: bold; -fx-padding: 10 0 5 0;");
                    pastRequestsContainer.getChildren().add(dateHeader);
                    lastRequestDate = currentRequestDate;
                }
                try {
                    FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/org/example/exhibitly/maintenance_request_item.fxml"));
                    GridPane requestItem = fxmlLoader.load();
                    ((Label) requestItem.lookup("#timeLabel")).setText(TIME_FORMAT.format(request.getRequestDate()));
                    ((Label) requestItem.lookup("#artefactNameLabel")).setText("Artefak " + request.getArtefactID());
                    ((Label) requestItem.lookup("#requesterNameLabel")).setText(request.getDescription().split("\n")[0].replace("Requester: ", "").trim());
                    ((Label) requestItem.lookup("#descriptionLabel")).setText(request.getDescription().split("\n").length > 1 ? request.getDescription().split("\n")[1] : request.getDescription());
                    ((Label) requestItem.lookup("#statusLabel")).setText(request.getStatus());
                    pastRequestsContainer.getChildren().add(requestItem);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        // Jika tidak ada request, tampilkan pesan
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
                        .thenComparing((Maintenance req) -> req.getPerformedDate(), Comparator.reverseOrder())) // Urutkan juga berdasarkan waktu performed
                .collect(Collectors.toList());

        LocalDate lastPerformedDate = null; // Untuk melacak tanggal terakhir yang diproses di history

        for (Maintenance record : historyRecords) {
            LocalDate currentPerformedDate = record.getPerformedDate().toInstant().atZone(ZoneId.systemDefault()).toLocalDate();

            // Tambahkan header tanggal jika tanggalnya berbeda dari yang sebelumnya
            if (lastPerformedDate == null || !currentPerformedDate.isEqual(lastPerformedDate)) {
                Label dateHeader = new Label(currentPerformedDate.format(TODAY_DATE_FORMATTER)); // Gunakan format tanggal penuh
                dateHeader.setStyle("-fx-font-size: 16px; -fx-font-weight: bold; -fx-padding: 10 0 5 0;");
                historyDataContainer.getChildren().add(dateHeader);
                lastPerformedDate = currentPerformedDate;
            }
            try {
                FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/org/example/exhibitly/maintenance_request_item.fxml"));
                GridPane historyItem = fxmlLoader.load();

                // Fill data into the loaded item
                ((Label) historyItem.lookup("#timeLabel")).setText(TIME_FORMAT.format(record.getPerformedDate()));
                ((Label) historyItem.lookup("#artefactNameLabel")).setText("Artefak " + record.getArtefactID());
                // Asumsi requesterName ada di deskripsi atau field lain
                ((Label) historyItem.lookup("#requesterNameLabel")).setText(record.getDescription().split("\n")[0].replace("Requester: ", "").trim());
                ((Label) historyItem.lookup("#descriptionLabel")).setText(record.getDescription().split("\n").length > 1 ? record.getDescription().split("\n")[1] : record.getDescription());
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
    private void onSubmitNewRequest(ActionEvent event) {
        String artefactNameInput = artefactNameField.getText().trim();
        String requesterNameInput = requesterNameField.getText().trim();
        String description = descriptionArea.getText().trim();

        if (artefactNameInput.isEmpty() || requesterNameInput.isEmpty() || description.isEmpty()) {
            System.err.println("Error: Semua field harus diisi!");
            return;
        }

        int dummyArtefactID = 999;
        String newRequestID = UUID.randomUUID().toString();
        Date currentDateTime = new Date();

        // Tambahkan nama requester ke deskripsi agar bisa diambil saat menampilkan
        String fullDescription = "Requester: " + requesterNameInput + "\n" + description;

        Maintenance newMaintenanceRequest = new Maintenance(
                newRequestID,
                dummyArtefactID,
                currentDateTime,
                "Permintaan User",
                "Not Done",
                fullDescription // Simpan deskripsi lengkap dengan nama requester
        );

        allMaintenanceRecords.add(newMaintenanceRequest);
        System.out.println("New maintenance request added: " + newMaintenanceRequest);

        loadRequests(); // Refresh tampilan
        addRequestForm.setVisible(false);
        requestContentDisplay.setVisible(true);
        artefactNameField.clear();
        requesterNameField.clear();
        descriptionArea.clear();
    }

    @FXML
    private void onRequestTabClick(ActionEvent event) {
        requestTabButton.setStyle("-fx-background-color: transparent; -fx-border-color: black; -fx-border-width: 0 0 2 0;");
        historyTabButton.setStyle("-fx-background-color: transparent; -fx-border-color: transparent; -fx-border-width: 0 0 0 0;");
        requestContentDisplay.setVisible(true);
        historyContent.setVisible(false);
        addRequestForm.setVisible(false); // Sembunyikan form saat pindah tab
    }

    @FXML
    private void onHistoryTabClick(ActionEvent event) {
        historyTabButton.setStyle("-fx-background-color: transparent; -fx-border-color: black; -fx-border-width: 0 0 2 0;");
        requestTabButton.setStyle("-fx-background-color: transparent; -fx-border-color: transparent; -fx-border-width: 0 0 0 0;");
        requestContentDisplay.setVisible(false);
        historyContent.setVisible(true);
        addRequestForm.setVisible(false); // Sembunyikan form saat pindah tab
    }

    @FXML
    private void onAddRequestButtonClick(ActionEvent event) {
        // Tampilkan form dan sembunyikan daftar request/history
        addRequestForm.setVisible(true);
        requestContentDisplay.setVisible(false);
        historyContent.setVisible(false);
        // Reset form fields
        artefactNameField.clear();
        requesterNameField.clear();
        descriptionArea.clear();
    }

    @FXML
    private void onCancelAddRequest(ActionEvent event) {
        // Sembunyikan form dan kembali ke tampilan request
        addRequestForm.setVisible(false);
        requestContentDisplay.setVisible(true);
        // Bersihkan form
        artefactNameField.clear();
        requesterNameField.clear();
        descriptionArea.clear();
    }



    // ===============================================
    // Metode Navigasi Umum (dari header)
    // ===============================================
    @FXML
    private void onLogoButtonClick(ActionEvent event) throws IOException {
        if (session.isLoggedIn()) {
            navigateToPage(event, "/org/example/exhibitly/LandingDoneLoginPage.fxml");
        } else {
            navigateToPage(event, "/org/example/exhibitly/LandingPage.fxml");
        }
    }

    @FXML
    private void onExhibitButtonClick(ActionEvent event) throws IOException {
        System.out.println("Go to Exhibit Page");
        // loadScene(event, "/org/example/exhibitly/exhibit_page.fxml");
    }

    @FXML
    private void onArtefactButtonClick(ActionEvent event) throws IOException {
        loadScene(event, "/org/example/exhibitly/artefact_page.fxml");
    }

    @FXML
    private void onTicketsButtonClick(ActionEvent event) throws IOException {
        System.out.println("Go to Tickets Page");
        // loadScene(event, "/org/example/exhibitly/tickets_page.fxml");
    }

    @FXML
    private void onLogoutButtonClick(ActionEvent event) throws IOException {
        System.out.println("User logged out.");
        loadScene(event, "/org/example/exhibitly/login_page.fxml");
    }

    @FXML
    private void onMaintenanceButtonClick(ActionEvent event) throws IOException {
        // Sudah di halaman maintenance, tidak perlu ganti scene
        // System.out.println("Already on Maintenance Page");
    }

    // Helper method untuk memuat scene baru
    private void loadScene(ActionEvent event, String fxmlPath) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource(fxmlPath));
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }
}