package org.example.exhibitly.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;
import org.example.exhibitly.models.Artefact;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class ArtefactController implements Initializable {

    @FXML
    private ImageView logoHeaderImageView; // fx:id untuk logo di header
    @FXML
    private ImageView logoFooter; // fx:id untuk logo di footer

    @FXML
    private Button onLoginButtonClick; // Untuk mengambil Stage dari tombol ini

    @FXML
    private TextField searchTextField;
    @FXML
    private Label resultsCountLabel;
    @FXML
    private GridPane artefactGrid; // GridPane tempat artefak akan ditambahkan

    @FXML
    private TextField periodFromField;
    @FXML
    private TextField periodToField;
    // Tambahkan @FXML untuk CheckBox region jika ingin mengontrolnya di Java
    // @FXML private CheckBox dkiJakartaCheckbox;
    // ...

    // Data artefak (simulasi dari database)
    private List<Artefact> allArtefacts;
    @FXML
    private Button logoButton;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        // Inisialisasi gambar header dan footer
        try {
            logoFooter.setImage(new Image(getClass().getResourceAsStream("/images/logo2.png")));
        } catch (Exception e) {
            System.err.println("Error loading logos: " + e.getMessage());
            e.printStackTrace();
        }
        try {
            ImageView logoImageView = (ImageView) logoButton.getGraphic();
            logoImageView.setImage(new Image(getClass().getResourceAsStream("/images/logo.png")));
        } catch (Exception e) {
            System.err.println("Error loading logo for button: " + e.getMessage());
            e.printStackTrace();
        }

        // Contoh data artefak
        allArtefacts = new ArrayList<>();
        allArtefacts.add(new Artefact("A001", "ARCA GANESHA", "Jawa Timur", 800, 900, "/images/arca1.png"));
        allArtefacts.add(new Artefact("A002", "ARCA CIREBON", "Jawa Barat", 1400, 1500, "/images/arca2.png"));
        allArtefacts.add(new Artefact("A003", "ARCA JATINANGOR", "Jawa Barat", 1200, 1300, "/images/arca3.png"));
        allArtefacts.add(new Artefact("A004", "ARCA DAGU", "DKI Jakarta", 1000, 1100, "/images/arca4.png"));
        allArtefacts.add(new Artefact("A005", "ARCA TUBIS", "DI Yogyakarta", 700, 800, "/images/arca5.png"));
        allArtefacts.add(new Artefact("A006", "ARCA CISITU", "Jawa Barat", 900, 1000, "/images/arca6.png"));
        allArtefacts.add(new Artefact("A007", "ARCA GANESHA", "Jawa Timur", 800, 900, "/images/arca1.png"));
        allArtefacts.add(new Artefact("A001", "ARCA GANESHA", "Jawa Timur", 800, 900, "/images/arca1.png"));
        allArtefacts.add(new Artefact("A001", "ARCA GANESHA", "Jawa Timur", 800, 900, "/images/arca1.png"));
        allArtefacts.add(new Artefact("A001", "ARCA GANESHA", "Jawa Timur", 800, 900, "/images/arca1.png"));
        allArtefacts.add(new Artefact("A001", "ARCA GANESHA", "Jawa Timur", 800, 900, "/images/arca1.png"));
        allArtefacts.add(new Artefact("A001", "ARCA GANESHA", "Jawa Timur", 800, 900, "/images/arca1.png"));
        allArtefacts.add(new Artefact("A001", "ARCA GANESHA", "Jawa Timur", 800, 900, "/images/arca1.png"));
        // Tambahkan lebih banyak data sesuai kebutuhan Anda
        // allArtefacts.add(new Artefact("A007", "NAMA ARTEFAK BARU", "REGION BARU", 1600, 1700, "/path/to/new_artefact.png"));


        // Tampilkan semua artefak saat pertama kali halaman dimuat
        displayArtefacts(allArtefacts);
    }

    private void displayArtefacts(List<Artefact> artefactsToDisplay) {
        artefactGrid.getChildren().clear(); // Bersihkan grid sebelum menambahkan yang baru

        int column = 0;
        int row = 0;
        int maxColumns = 3; // Sesuai dengan ColumnConstraints di FXML

        for (Artefact artefact : artefactsToDisplay) {
            // Membuat VBox sebagai template untuk setiap artefak
            VBox artefactCard = new VBox(10); // Spacing 10px
            artefactCard.setAlignment(Pos.CENTER);
            artefactCard.setStyle("-fx-border-color: #DDDDDD; -fx-border-width: 1px; -fx-padding: 10px; -fx-background-color: white;");

            ImageView imageView = new ImageView();
            imageView.setFitWidth(200); // Ukuran gambar di card
            imageView.setFitHeight(200);
            imageView.setPreserveRatio(true);

            // Muat gambar artefak
            try {
                imageView.setImage(new Image(getClass().getResourceAsStream(artefact.getImageUrl())));
            } catch (Exception e) {
                System.err.println("Failed to load image for: " + artefact.getName() + " from " + artefact.getImageUrl());
                imageView.setImage(new Image(getClass().getResourceAsStream("/org/example/exhibitly/images/placeholder.png"))); // Gambar placeholder
            }

            Label nameLabel = new Label(artefact.getName());
            nameLabel.setFont(Font.font("Plus Jakarta Sans Bold", FontWeight.BOLD, 14));
            nameLabel.setWrapText(true); // Teks nama artefak bisa wrap
            nameLabel.setAlignment(Pos.CENTER);
            nameLabel.setMaxWidth(200); // Sesuaikan dengan lebar gambar

            Label regionLabel = new Label(artefact.getRegion());
            regionLabel.setFont(Font.font("Plus Jakarta Sans Regular", 12));
            regionLabel.setTextFill(javafx.scene.paint.Color.GRAY);

            // Tombol "Lihat Selengkapnya"
            Button detailButton = new Button("Lihat Selengkapnya");
            detailButton.setStyle("-fx-background-color: #555555; -fx-text-fill: white;");
            detailButton.setOnAction(e -> showArtefactDetail(artefact)); // Menambahkan aksi klik

            artefactCard.getChildren().addAll(imageView, nameLabel, regionLabel, detailButton);

            // Tambahkan artefak ke GridPane
            artefactGrid.add(artefactCard, column, row);

            column++;
            if (column == maxColumns) {
                column = 0;
                row++;
            }
        }
        resultsCountLabel.setText(artefactsToDisplay.size() + " RESULTS");
    }

    private void showArtefactDetail(Artefact artefact) {
        // Implementasi untuk menampilkan detail artefak (bisa berupa pop-up atau halaman baru)
        System.out.println("Detail for: " + artefact.getName() + " (" + artefact.getId() + ")");
        // Contoh: Membuat pop-up sederhana
        Stage detailStage = new Stage();
        VBox detailLayout = new VBox(10);
        detailLayout.setPadding(new javafx.geometry.Insets(20));
        detailLayout.setAlignment(Pos.CENTER);
        detailLayout.setStyle("-fx-background-color: white;");

        ImageView detailImageView = new ImageView();
        detailImageView.setFitWidth(300);
        detailImageView.setFitHeight(300);
        detailImageView.setPreserveRatio(true);
        try {
            detailImageView.setImage(new Image(getClass().getResourceAsStream(artefact.getImageUrl())));
        } catch (Exception e) {
            detailImageView.setImage(new Image(getClass().getResourceAsStream("/org/example/exhibitly/images/placeholder.png")));
        }

        Label name = new Label(artefact.getName());
        name.setFont(Font.font("Plus Jakarta Sans Bold", FontWeight.BOLD, 18));

        Label region = new Label("Region: " + artefact.getRegion());
        Label period = new Label("Period: " + artefact.getPeriodStart() + " - " + artefact.getPeriodEnd());
        Label description = new Label("ID: " + artefact.getId() + "\n" + // Contoh detail lebih lengkap
                "This is a detailed description for " + artefact.getName() + ". " +
                "It represents a significant cultural artifact from the " + artefact.getRegion() + " region.");
        description.setWrapText(true);
        description.setMaxWidth(400);

        Button closeButton = new Button("Close");
        closeButton.setOnAction(e -> detailStage.close());
        closeButton.setStyle("-fx-background-color: #AAAAAA; -fx-text-fill: white;");


        detailLayout.getChildren().addAll(detailImageView, name, region, period, description, closeButton);
        detailStage.setScene(new Scene(detailLayout));
        detailStage.setTitle(artefact.getName() + " Details");
        detailStage.show();
    }


    // --- Event Handlers (dari header dan filter) ---

    @FXML
    private void onSearch() {

    }

    @FXML
    private void onValidatePeriodFilter() {

    }

    @FXML
    private void onValidateRegionFilter() {

    }


    // Event handler dari Header (pastikan sudah disesuaikan dengan ActionEvent/MouseEvent)
    @FXML
    private void onLoginButtonClick(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/org/example/exhibitly/login.fxml"));
            Parent loginPage = loader.load();
            Stage stage = (Stage) ((javafx.scene.Node) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(loginPage));
            stage.setTitle("Museum Nusantara - Login");
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            System.err.println("Gagal memuat halaman login: " + e.getMessage());
        }
    }

    @FXML
    public void onLogoButtonClick(ActionEvent event) { // Jika logo adalah Button
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/org/example/exhibitly/LandingPage.fxml"));
            Parent landingPage = loader.load();
            Stage stage = (Stage) ((javafx.scene.Node) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(landingPage));
            stage.setTitle("Museum Nusantara - Landing Page");
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            System.err.println("Gagal memuat halaman landing page: " + e.getMessage());
        }
    }
    // Jika logo adalah ImageView yang di-klik, gunakan MouseEvent:
    /*
    @FXML
    private ImageView logoHeaderImageView; // Asumsi fx:id untuk ImageView logo di header
    // ... di initialize()
    // logoHeaderImageView.setOnMouseClicked(this::onLogoImageClick);
    // ...
    private void onLogoImageClick(MouseEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/org/example/exhibitly/LandingPage.fxml"));
            Parent landingPage = loader.load();
            Stage stage = (Stage) ((javafx.scene.Node) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(landingPage));
            stage.setTitle("Museum Nusantara - Landing Page");
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            System.err.println("Gagal memuat halaman landing page: " + e.getMessage());
        }
    }
    */


    @FXML
    private void onExhibitButtonClick(ActionEvent event) {
        try {
            // Ganti ini dengan path FXML untuk halaman Exhibit
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/org/example/exhibitly/ExhibitPage.fxml"));
            Parent exhibitPage = loader.load();
            Stage stage = (Stage) ((javafx.scene.Node) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(exhibitPage));
            stage.setTitle("Museum Nusantara - Exhibit");
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            System.err.println("Gagal memuat halaman Exhibit: " + e.getMessage());
        }
    }

    @FXML
    private void onArtefactButtonClick(ActionEvent event) {
        displayArtefacts(allArtefacts);
        // Karena ini sudah halaman Artefact, mungkin tidak perlu ganti halaman
        // Tapi jika Anda ingin me-refresh halaman atau filter default, bisa panggil displayArtefacts(allArtefacts);
        System.out.println("Already on Artefact Page.");
    }

    @FXML
    private void onTicketsButtonClick(ActionEvent event) {
        try {
            // Ganti ini dengan path FXML untuk halaman Tickets
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/org/example/exhibitly/TicketsPage.fxml"));
            Parent ticketsPage = loader.load();
            Stage stage = (Stage) ((javafx.scene.Node) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(ticketsPage));
            stage.setTitle("Museum Nusantara - Tickets");
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            System.err.println("Gagal memuat halaman Tickets: " + e.getMessage());
        }
    }
}
