package org.example.exhibitly.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
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
import java.util.HashSet;
import java.util.List;
import java.util.ResourceBundle;
import java.util.Set;

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

    /* CheckBox Attributes */
    @FXML
    private CheckBox  DKIJakartaCheckBox;
    @FXML
    private CheckBox  JawaBaratCheckBox;
    @FXML
    private CheckBox  JawaTengahCheckBox;
    @FXML
    private CheckBox  DIYogyakartaCheckBox;
    @FXML
    private CheckBox  JawaTimurCheckBox;
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
        allArtefacts = new ArrayList<>();
        allArtefacts.add(new Artefact("A001", "ARCA GANESHA", "Jawa Timur", 800, "/images/arca1.png"));
        allArtefacts.add(new Artefact("A002", "ARCA CIREBON", "Jawa Barat", 1400, "/images/arca2.png"));
        allArtefacts.add(new Artefact("A003", "ARCA JATINANGOR", "Jawa Barat", 1200,  "/images/arca3.png"));
        allArtefacts.add(new Artefact("A004", "ARCA DAGU", "DKI Jakarta", 1000,  "/images/arca4.png"));
        allArtefacts.add(new Artefact("A005", "ARCA TUBIS", "DI Yogyakarta", 700,  "/images/arca5.png"));
        allArtefacts.add(new Artefact("A006", "ARCA CISITU", "Jawa Barat", 900,  "/images/arca6.png"));

        /* Real Time Update for each Filter */
        // searchTextField.textProperty().addListener((observable, oldValue, newValue) -> {
        //     applyAllFilter();
        // });

        // periodFromField.textProperty().addListener((observable, oldValue, newValue) -> {
        //     applyAllFilter();
        // });

        // periodToField.textProperty().addListener((observable, oldValue, newValue) -> {
        //     applyAllFilter();
        // });

        // setupRegionFilterListeners();

        setupEnterKeyHandlers();

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
        Label period = new Label("Period: " + artefact.getPeriod());
        Label description = new Label("ID: " + artefact.getId() + "\n" + // Contoh detail lebih lengkap
                "This is a detailed description for " + artefact.getName() + ". " +
                "It represents a significant cultural artefact from the " + artefact.getRegion() + " region.");
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

    // --- Key Handlers ---

    private void setupEnterKeyHandlers() {
        searchTextField.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.ENTER) {
                onSearch();
            }
        });
    }


    // --- Event Handlers (dari header dan filter) ---

    @FXML
    private void onSearch() {
        applyAllFilter();
    }


    @FXML
    private void onValidatePeriodFilter() {
        applyAllFilter();
    }

    @FXML
    private void onValidateRegionFilter() {
        applyAllFilter();
    }


    private void applyAllFilter() {
        List<Artefact> filterArtefact = new ArrayList<>(allArtefacts);

        String userSearch = searchTextField.getText();

        if (userSearch != null && !userSearch.trim().isEmpty()) {
            filterArtefact = filterBySearchText(filterArtefact, userSearch);
        }

        Set<String> selectedRegion = getSelectedRegion();
        if (!selectedRegion.isEmpty()) {
            filterArtefact = filterByRegion(filterArtefact, selectedRegion);
        }

        filterArtefact = filterByPeriod(filterArtefact);

        displayArtefacts(filterArtefact);
    }

    private Set<String> getSelectedRegion() {
        Set<String> region = new HashSet<>();

        if (DKIJakartaCheckBox != null && DKIJakartaCheckBox.isSelected()) {
            region.add("DKI Jakarta");
        }
        if (JawaBaratCheckBox != null && JawaBaratCheckBox.isSelected()) {
            region.add("Jawa Barat");
        }
        if (JawaTengahCheckBox != null && JawaTengahCheckBox.isSelected()) {
            region.add("Jawa Tengah");
        }
        if (DIYogyakartaCheckBox != null && DIYogyakartaCheckBox.isSelected()) {
            region.add("DI Yogyakarta");
        }
        if (JawaTimurCheckBox != null && JawaTimurCheckBox.isSelected()) {
            region.add("Jawa Timur");
        }
        return region;
    }

    private void setupRegionFilterListeners() {
        if (DKIJakartaCheckBox != null) {
            DKIJakartaCheckBox.selectedProperty().addListener((observable, oldValue, newValue) -> {
                System.out.println("DKI Jakarta checkbox changed: " + newValue);
                applyAllFilter();
            });
        }

        if (JawaBaratCheckBox != null) {
            JawaBaratCheckBox.selectedProperty().addListener((observable, oldValue, newValue) -> {
                System.out.println("Jawa Barat checkbox changed: " + newValue);
                applyAllFilter();
            });
        }

        if (JawaTengahCheckBox != null) {
            JawaTengahCheckBox.selectedProperty().addListener((observable, oldValue, newValue) -> {
                System.out.println("Jawa Tengah checkbox changed: " + newValue);
                applyAllFilter();
            });
        }

        if (DIYogyakartaCheckBox != null) {
            DIYogyakartaCheckBox.selectedProperty().addListener((observable, oldValue, newValue) -> {
                System.out.println("DI Yogyakarta checkbox changed: " + newValue);
                applyAllFilter();
            });
        }

        if (JawaTimurCheckBox != null) {
            JawaTimurCheckBox.selectedProperty().addListener((observable, oldValue, newValue) -> {
                System.out.println("Jawa Timur checkbox changed: " + newValue);
                applyAllFilter();
            });
        }
    }

    private List<Artefact> filterBySearchText(List<Artefact> artefactList, String userSearch) {
        if (userSearch == null || userSearch.trim().isEmpty()) {
            return artefactList;
        }

        userSearch = userSearch.trim().toLowerCase();

        List<Artefact> filterArtefact = new ArrayList<>();

        for (Artefact entry : artefactList) {
            boolean matchesName = entry.getName().toLowerCase().contains(userSearch);
            boolean matchesId = entry.getId().toLowerCase().contains(userSearch);
            boolean matchesRegion = entry.getRegion().toLowerCase().contains(userSearch);

            if (matchesName || matchesId || matchesRegion) {
                filterArtefact.add(entry);
            }
        }
        return filterArtefact;
    }

    private List<Artefact> filterByRegion(List<Artefact> artefactList, Set<String> regionSet) {
        List<Artefact> filterArtefacts = new ArrayList<>();

        for (Artefact entry : artefactList) {
            boolean matchesRegion = regionSet.contains(entry.getRegion());

            if (matchesRegion) {
                filterArtefacts.add(entry);
            }
        }
        return filterArtefacts;
    }

    private List<Artefact> filterByPeriod(List<Artefact> artefactList) {
        String fromPeriod;
        String toPeriod;

        if (periodFromField != null) {
            fromPeriod = periodFromField.getText().trim();
        } else {
            fromPeriod = "";
        }

        if (periodToField != null) {
            toPeriod = periodToField.getText().trim();
        } else {
            toPeriod = "";
        }

        if (fromPeriod.isEmpty() && toPeriod.isEmpty()) {
            return artefactList;
        }        

        List<Artefact> filterArtefact = new ArrayList<>();

        try {
            int fromYear;
            int toYear;

            if (fromPeriod.isEmpty()) {
                fromYear = 0;
            } else {
                fromYear = Integer.parseInt(fromPeriod);
            }

            if (toPeriod.isEmpty()) {
                toYear = 0;
            } else {
                toYear = Integer.parseInt(toPeriod);
            }

            for (Artefact entry : artefactList) {
                boolean periodChecker;

                if (fromPeriod.isEmpty()) {
                    periodChecker = entry.getPeriod() <= toYear;
                } else if (toPeriod.isEmpty()) {
                    periodChecker = entry.getPeriod() >= fromYear;
                } else {
                    periodChecker = entry.getPeriod() >= fromYear && entry.getPeriod() <= toYear;
                }

                if (periodChecker) {
                    filterArtefact.add(entry);
                }
            }
        } catch (NumberFormatException e) {
            System.err.println("Invalid period format!");
            return artefactList;
        }
        
        return filterArtefact;
    }

    @FXML
    private void onClearAllFilters() {
        if (searchTextField != null) {
            searchTextField.clear();
        }

        if (periodFromField != null) {
            periodFromField.clear();
        }
        if (periodToField != null) {
            periodToField.clear();
        }

        if (DKIJakartaCheckBox != null) {
            DKIJakartaCheckBox.setSelected(false);
        }
        if (JawaBaratCheckBox != null) {
            JawaBaratCheckBox.setSelected(false);
        }
        if (JawaTengahCheckBox != null) {
            JawaTengahCheckBox.setSelected(false);
        }
        if (DIYogyakartaCheckBox != null) {
            DIYogyakartaCheckBox.setSelected(false);
        }
        if (JawaTimurCheckBox != null) {
            JawaTimurCheckBox.setSelected(false);
        }

        applyAllFilter();
    }

    // Event handler dari Header (pastikan sudah disesuaikan dengan ActionEvent/MouseEvent)
    
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
    private void onLoginButtonClick(ActionEvent actionEvent) {
        navigateToPage(actionEvent, "/org/example/exhibitly/login.fxml");
    }

    @FXML
    private void onExhibitButtonClick(ActionEvent actionEvent) {
        navigateToPage(actionEvent, "/org/example/exhibitly/Exhibit.fxml");
    }

    @FXML
    private void onArtefactButtonClick(ActionEvent actionEvent) { // <-- Metode ini harus menerima ActionEvent
        System.out.println("Sudah ada di Artefact Page");
    }

    @FXML
    private void onTicketButtonClick(ActionEvent actionEvent) {
        navigateToPage(actionEvent, "/org/example/exhibitly/Ticket.fxml");
    }

    @FXML
    private void onLogoButtonClick(ActionEvent actionEvent) { // <--- Tambahkan ActionEvent event
        navigateToPage(actionEvent, "/org/example/exhibitly/LandingPage.fxml");
    }

    private void navigateToPage(ActionEvent actionEvent, String path) {
        String pageName = path.substring(path.lastIndexOf('/') + 1).replace(".fxml", "");
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(path));
            Parent root = loader.load();

            Stage stage = (Stage) ((javafx.scene.Node) actionEvent.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root));
            
            stage.setTitle("Museum Nusantar - " + pageName);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            System.err.println("Gagal memuat halaman " + pageName + ": " + e.getMessage());
        }
    }
}
