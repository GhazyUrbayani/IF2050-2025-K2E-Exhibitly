package org.example.exhibitly.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.Cursor;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Modality;
import javafx.stage.Stage;
import org.example.exhibitly.models.Artefact;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.ResourceBundle;
import java.util.Set;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.sql.SQLException;

public class ArtefactController extends BaseController implements Initializable {

    @FXML
    private ImageView logoFooter;

    @FXML
    private Button loginLogoutButton;

    @FXML
    private TextField searchTextField;
    @FXML
    private Label resultsCountLabel;
    @FXML
    private GridPane artefactGrid;

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

    // Data artefak (simulasi dari database)
    private List<Artefact> allArtefacts;
    private Connection connection;

    @FXML
    private Button logoButton;
    @FXML 
    private Button addArtefactButton;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        this.connection = new DatabaseConnection().getConnection();
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

        updateLoginLogoutButton();
        loadAllArtefactsFromDB();
        displayArtefacts(allArtefacts);
        setupRoleBasedAccess();
    }

    private void loadAllArtefactsFromDB() {
        allArtefacts = new ArrayList<>();
        String sql = "SELECT * FROM Artefact";
        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                allArtefacts.add(new Artefact(
                    rs.getInt("artefactID"),
                    rs.getString("title"),
                    rs.getString("region"),
                    rs.getInt("period"),
                    rs.getString("description"),
                    rs.getString("mediaURL")
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void updateLoginLogoutButton() {
        if (session != null && session.isLoggedIn()) {
            loginLogoutButton.setText("Logout");
        } else {
            loginLogoutButton.setText("Login");
        }
    }

    private void displayArtefacts(List<Artefact> artefactsToDisplay) {
        artefactGrid.getChildren().clear();

        int column = 0;
        int row = 0;
        int maxColumns = 3;

        for (Artefact artefact : artefactsToDisplay) {
            VBox artefactCard = createArtefactCard(artefact);
            artefactGrid.add(artefactCard, column, row);
            
            column++;
            if (column == 3) {
                column = 0;
                row++;
            }
        }
        resultsCountLabel.setText(artefactsToDisplay.size() + " RESULTS");
    }

    private VBox createArtefactCard(Artefact artefact) {
        VBox card = new VBox(10);
        card.setAlignment(Pos.CENTER);
        card.setStyle(
            "-fx-background-color: white;" +
            "-fx-border-color: #DDDDDD;" +
            "-fx-border-width: 1px;" +
            "-fx-padding: 15px;" +
            "-fx-border-radius: 8px;" + 
            "-fx-background-radius: 8px;"
        );
    
        ImageView imageView = new ImageView();
        imageView.setFitWidth(200);
        imageView.setFitHeight(200);
        imageView.setPreserveRatio(true);

        String url = artefact.getMediaURL();
        try {
            Image image;
            if (url != null && url.startsWith("http")) {
                image = new Image(url, true);
            } else {
                image = new Image(getClass().getResourceAsStream(url));
            }
            imageView.setImage(image);
        } catch (Exception e) {
            System.err.println("Gagal memuat gambar: " + url);
            // imageView.setImage(new Image(getClass().getResourceAsStream("/images/placeholder.png")));
        }
    
        Label nameLabel = new Label(artefact.getTitle());
        nameLabel.setFont(Font.font("Plus Jakarta Sans", FontWeight.BOLD, 16));
    
        Label regionLabel = new Label(artefact.getRegion());
        regionLabel.setFont(Font.font("Plus Jakarta Sans", FontWeight.NORMAL, 12));
        regionLabel.setTextFill(javafx.scene.paint.Color.GRAY);
    
        HBox buttonContainer = new HBox(10);
        buttonContainer.setAlignment(Pos.CENTER);
    
        if (session.isKurator()) {
            Button editButton = new Button("Edit");
            editButton.setStyle("-fx-background-color: #333; -fx-text-fill: white; -fx-font-family: 'Plus Jakarta Sans Regular';");
            editButton.setCursor(Cursor.HAND);
            editButton.setOnAction(e -> handleEditArtefact(artefact));
            buttonContainer.getChildren().add(editButton);
        }
    
        Button detailButton = new Button("Detail");
        detailButton.setStyle("-fx-background-color: #333; -fx-text-fill: white; -fx-font-family: 'Plus Jakarta Sans Regular';");
        detailButton.setCursor(Cursor.HAND);
        detailButton.setOnAction(e -> showArtefactDetail(artefact));
        buttonContainer.getChildren().add(detailButton);
    
        card.getChildren().addAll(imageView, nameLabel, regionLabel, buttonContainer);
    
        return card;
    }

    private void setupRoleBasedAccess() {
        if (addArtefactButton != null) {
            addArtefactButton.setVisible(session.isKurator());
            addArtefactButton.setManaged(session.isKurator());
        }
    }

    private void handleDeleteArtefact(Artefact artefact) {
        System.out.println("Delete artefact: " + artefact.getTitle());
        String sql = "DELETE FROM Artefact WHERE artefactID = ?";
        try (Connection conn = new DatabaseConnection().getConnection();
            PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, artefact.getArtefactID());
            int affectedRows = pstmt.executeUpdate();

            if (affectedRows > 0) {
                System.out.println("Artefak berhasil dihapus: " + artefact.getTitle());
                loadAllArtefactsFromDB(); 
                displayArtefacts(allArtefacts);
            } else {
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void showArtefactDetail(Artefact artefact) {
        System.out.println("Detail for: " + artefact.getTitle() + " (" + artefact.getArtefactID() + ")");
        Stage detailStage = new Stage();
        VBox detailLayout = new VBox(10);
        detailLayout.setPadding(new javafx.geometry.Insets(20));
        detailLayout.setAlignment(Pos.CENTER);
        detailLayout.setStyle("-fx-background-color: white;");

        ImageView detailImageView = new ImageView();
        detailImageView.setFitWidth(300);
        detailImageView.setFitHeight(300);
        detailImageView.setPreserveRatio(true);
        Image img = null;
        if (artefact.getMediaURL() != null && artefact.getMediaURL().startsWith("http")) {
            img = new Image(artefact.getMediaURL(), true);
        } else if (artefact.getMediaURL() != null) {
            java.io.InputStream is = getClass().getResourceAsStream(artefact.getMediaURL());
            if (is != null) {
                img = new Image(is);
            }
        }
        detailImageView.setImage(img);

        Label name = new Label(artefact.getTitle());
        name.setFont(Font.font("Plus Jakarta Sans Bold", FontWeight.BOLD, 18));

        Label region = new Label("Region: " + artefact.getRegion());
        Label period = new Label("Period: " + artefact.getPeriod());
        Label description = new Label(artefact.getDescription());
        description.setWrapText(true);
        description.setMaxWidth(400);

        Button closeButton = new Button("Close");
        closeButton.setOnAction(e -> detailStage.close());
        closeButton.setStyle("-fx-background-color: #AAAAAA; -fx-text-fill: white;");


        detailLayout.getChildren().addAll(detailImageView, name, region, period, description, closeButton);
        detailStage.setScene(new Scene(detailLayout));
        detailStage.setTitle(artefact.getTitle() + " Details");
        detailStage.show();
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

    // --- CRUD Methods sesuai Entity Model ---

    @FXML
    private void handleAddArtefact() {
        showAddEditDialog(null);
    }
    private void handleEditArtefact(Artefact artefact) {
        showAddEditDialog(artefact);
    }

    @FXML
    private void onAddArtefactButtonClick(ActionEvent event) {
        showAddEditDialog(null);
    }

    private void showAddEditDialog(Artefact artefactToEdit) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/org/example/exhibitly/AddEditArtefactDialog.fxml"));
            Parent page = loader.load();

            ComboBox<String> regionComboBox = (ComboBox<String>) page.lookup("#regionComboBox");
            regionComboBox.getItems().addAll(
                "DKI Jakarta", "Jawa Barat", "Jawa Tengah", "DI Yogyakarta", "Jawa Timur"
            );

            TextField titleField = (TextField) page.lookup("#titleField");
            TextField periodField = (TextField) page.lookup("#periodField");
            TextField mediaURLField = (TextField) page.lookup("#mediaURLField");
            TextArea descriptionArea = (TextArea) page.lookup("#descriptionArea");
            Button saveButton = (Button) page.lookup("#saveButton");
            Button cancelButton = (Button) page.lookup("#cancelButton");

            Stage dialogStage = new Stage();
            dialogStage.setTitle(artefactToEdit == null ? "Tambah Artefak Baru" : "Edit Artefak");
            dialogStage.initModality(Modality.WINDOW_MODAL);
            dialogStage.initOwner(artefactGrid.getScene().getWindow());
            dialogStage.setScene(new Scene(page));

            if (artefactToEdit != null) {
                titleField.setText(artefactToEdit.getTitle());
                regionComboBox.setValue(artefactToEdit.getRegion());
                periodField.setText(String.valueOf(artefactToEdit.getPeriod()));
                mediaURLField.setText(artefactToEdit.getMediaURL());
                descriptionArea.setText(artefactToEdit.getDescription());
            }

            saveButton.setOnAction(e -> {
                try {            
                    String sql;
                    if (artefactToEdit == null) {
                        int nextId = 1;
                        String maxIdSql = "SELECT MAX(artefactID) FROM Artefact";
                        try (Statement stmt = connection.createStatement();
                             ResultSet rs = stmt.executeQuery(maxIdSql)) {
                            if (rs.next()) {
                                nextId = rs.getInt(1) + 1;
                            }
                        }
                        sql = "INSERT INTO Artefact (artefactID, title, region, period, description, mediaURL) VALUES (?, ?, ?, ?, ?, ?)";
                        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
                            pstmt.setInt(1, nextId);
                            pstmt.setString(2, titleField.getText());
                            pstmt.setString(3, regionComboBox.getValue());
                            pstmt.setInt(4, Integer.parseInt(periodField.getText()));
                            pstmt.setString(5, descriptionArea.getText());
                            pstmt.setString(6, mediaURLField.getText());
                            pstmt.executeUpdate();
                        }
                    } else {
                        sql = "UPDATE Artefact SET title = ?, region = ?, period = ?, description = ?, mediaURL = ? WHERE artefactID = ?";
                    }
                    
                    try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
                        pstmt.setString(1, titleField.getText());
                        pstmt.setString(2, regionComboBox.getValue());
                        pstmt.setInt(3, Integer.parseInt(periodField.getText()));
                        pstmt.setString(4, descriptionArea.getText());
                        pstmt.setString(5, mediaURLField.getText());
                        if (artefactToEdit != null) {
                            pstmt.setInt(6, artefactToEdit.getArtefactID());
                        }
                        pstmt.executeUpdate();
                    }
                    
                    loadAllArtefactsFromDB();
                    displayArtefacts(allArtefacts);
                    dialogStage.close();
            
                } catch (SQLException ex) {
                    ex.printStackTrace();
                } catch (NumberFormatException ex) {
                    periodField.setStyle("-fx-border-color: red;");
                }
            });

            cancelButton.setOnAction(e -> dialogStage.close());

            dialogStage.showAndWait();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void deleteArtefact(int artefactID) {
        allArtefacts.removeIf(a -> a.getArtefactID() == artefactID);
        displayArtefacts(allArtefacts);
    }

    // --- Filter & Search Methods (update agar pakai getter entity model) ---

    private List<Artefact> filterBySearchText(List<Artefact> artefactList, String userSearch) {
        if (userSearch == null || userSearch.trim().isEmpty()) {
            return artefactList;
        }

        userSearch = userSearch.trim().toLowerCase();

        List<Artefact> filterArtefact = new ArrayList<>();

        for (Artefact entry : artefactList) {
            boolean matchesTitle = entry.getTitle().toLowerCase().contains(userSearch);
            boolean matchesId = String.valueOf(entry.getArtefactID()).contains(userSearch);
            boolean matchesRegion = entry.getRegion().toLowerCase().contains(userSearch);

            if (matchesTitle || matchesId || matchesRegion) {
                filterArtefact.add(entry);
            }
        }
        return filterArtefact;
    }

    private List<Artefact> filterByRegion(List<Artefact> artefactList, Set<String> regionSet) {
        List<Artefact> filterArtefacts = new ArrayList<>();

        for (Artefact entry : artefactList) {
            if (regionSet.contains(entry.getRegion())) {
                filterArtefacts.add(entry);
            }
        }
        return filterArtefacts;
    }

    private List<Artefact> filterByPeriod(List<Artefact> artefactList) {
        String fromPeriod = periodFromField != null ? periodFromField.getText().trim() : "";
        String toPeriod = periodToField != null ? periodToField.getText().trim() : "";
        if (fromPeriod.isEmpty() && toPeriod.isEmpty()) {
            return artefactList;
        }
        List<Artefact> filterArtefact = new ArrayList<>();
        try {
            int fromYear = fromPeriod.isEmpty() ? Integer.MIN_VALUE : Integer.parseInt(fromPeriod);
            int toYear = toPeriod.isEmpty() ? Integer.MAX_VALUE : Integer.parseInt(toPeriod);
            for (Artefact entry : artefactList) {
                int period = entry.getPeriod();
                if (period >= fromYear && period <= toYear) {
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

    @FXML
    private void onLoginLogoutButtonClick(ActionEvent event) {
        if (session != null && session.isLoggedIn()) {
            handleLogout(event);
        } else {
            navigateToPage(event, "/org/example/exhibitly/login.fxml");
        }
    }

    @FXML
    private void onExhibitButtonClick(ActionEvent event) {
        navigateToPage(event, "/org/example/exhibitly/Exhibit.fxml");
    }

    @FXML
    private void onArtefactButtonClick(ActionEvent event) { // <-- Metode ini harus menerima ActionEvent
        System.out.println("Sudah ada di Artefact Page");
    }

    @FXML
    private void onTicketButtonClick(ActionEvent event) {
        navigateToPage(event, "/org/example/exhibitly/Ticket.fxml");
    }

    @FXML
    private void onLogoButtonClick(ActionEvent event) { // <--- Tambahkan ActionEvent event
        navigateToPage(event, "/org/example/exhibitly/LandingPage.fxml");
    }
}
