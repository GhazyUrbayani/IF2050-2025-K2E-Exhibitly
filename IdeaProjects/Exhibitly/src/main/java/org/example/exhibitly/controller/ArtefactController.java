package org.example.exhibitly.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.Cursor;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
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
    protected TextField periodFromField;
    @FXML
    protected TextField periodToField;

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

    private List<Artefact> allArtefacts;
    private Connection connection;
    private Set<Integer> selectedArtefactIds = new HashSet<>();

    @FXML
    private Button logoButton;
    @FXML 
    private Button addArtefactButton;
    @FXML
    private Button deleteSelectedButton;
    @FXML
    private Button deleteAllButton;

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

        /* Real Time Update for each Filter */
        searchTextField.textProperty().addListener((observable, oldValue, newValue) -> {
            applyAllFilter();
        });

        // periodFromField.textProperty().addListener((observable, oldValue, newValue) -> {
        //     applyAllFilter();
        // });

        // periodToField.textProperty().addListener((observable, oldValue, newValue) -> {
        //     applyAllFilter();
        // });
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
            "-fx-border-color: #333;" +
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

        CheckBox selectCheckBox = new CheckBox();
        selectCheckBox.setOnAction(e -> {
            if (selectCheckBox.isSelected()) {
                selectedArtefactIds.add(artefact.getArtefactID());
            } else {
                selectedArtefactIds.remove(artefact.getArtefactID());
            }
        });
        
        Button detailButton = new Button("Detail");
        detailButton.setStyle("-fx-background-color: #333; -fx-text-fill: white; -fx-font-family: 'Plus Jakarta Sans Regular';");
        detailButton.setCursor(Cursor.HAND);
        detailButton.setOnAction(e -> showArtefactDetail(artefact));
        buttonContainer.getChildren().add(detailButton);
    
        if (session.isKurator()) {
            Button editButton = new Button("Edit");
            editButton.setStyle("-fx-background-color: #8B0000; -fx-text-fill: white; -fx-font-family: 'Plus Jakarta Sans Regular';");
            editButton.setCursor(Cursor.HAND);
            editButton.setOnAction(e -> handleEditArtefact(artefact));
            buttonContainer.getChildren().add(editButton);

            Button deleteButton = new Button("Delete");
            deleteButton.setStyle("-fx-background-color: #C62828; -fx-text-fill: white; -fx-font-family: 'Plus Jakarta Sans Regular';");
            deleteButton.setCursor(Cursor.HAND);
            deleteButton.setOnAction(e -> {handleDeleteArtefact(artefact); });
            buttonContainer.getChildren().add(deleteButton);
        }
    
        card.getChildren().addAll(imageView, nameLabel, regionLabel, buttonContainer);
    
        return card;
    }

    private void setupRoleBasedAccess() {
        if (addArtefactButton != null) {
            addArtefactButton.setVisible(session.isKurator());
            addArtefactButton.setManaged(session.isKurator());
        }
        if (deleteSelectedButton != null) {
            deleteSelectedButton.setVisible(session.isKurator());
            deleteSelectedButton.setManaged(session.isKurator());
        }
        if (deleteAllButton != null) {
            deleteAllButton.setVisible(session.isKurator());
            deleteAllButton.setManaged(session.isKurator());
        }
    }

    private void handleDeleteArtefact(Artefact artefact) {
        Alert confirmAlert = new Alert(Alert.AlertType.CONFIRMATION);
        confirmAlert.setTitle("Konfirmasi Hapus Artefak");
        confirmAlert.setHeaderText("Yakin ingin menghapus artefak \"" + artefact.getTitle() + "\"?");
        confirmAlert.setContentText("Artefak akan dihapus dari database. Tindakan ini tidak dapat dibatalkan.");
    
        confirmAlert.showAndWait().ifPresent(response -> {
            if (response == ButtonType.OK) {
                try (Connection conn = new DatabaseConnection().getConnection()) {
    
                    String deleteRelasiKurator = "DELETE FROM Kurator_Artefact WHERE artefactID = ?";
                    try (PreparedStatement pstmtKurator = conn.prepareStatement(deleteRelasiKurator)) {
                        pstmtKurator.setInt(1, artefact.getArtefactID());
                        pstmtKurator.executeUpdate();
                    }
    
                    String sql = "DELETE FROM Artefact WHERE artefactID = ?";
                    try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
                        pstmt.setInt(1, artefact.getArtefactID());
                        int affectedRows = pstmt.executeUpdate();
    
                        if (affectedRows > 0) {
                            loadAllArtefactsFromDB();
                            displayArtefacts(allArtefacts);
                        }
                    }
                } catch (SQLException e) {
                    Alert errorAlert = new Alert(Alert.AlertType.ERROR);
                    errorAlert.setTitle("Gagal Menghapus Artefak");
                    errorAlert.setHeaderText("Artefak gagal dihapus.");
                    errorAlert.setContentText("Pastikan artefak sedang tidak digunakan di Exhibit");
                    errorAlert.showAndWait();
                }
            }
        });
    }

    // --- Handler untuk Delete Selected ---
    private void handleDeleteSelectedArtefacts() {
        Stage dialogStage = new Stage();
        dialogStage.setTitle("Pilih Artefak yang Akan Dihapus");
        dialogStage.initModality(Modality.WINDOW_MODAL);
        dialogStage.initOwner(artefactGrid.getScene().getWindow());
    
        VBox layout = new VBox(10);
        layout.setPadding(new javafx.geometry.Insets(20));
        layout.setAlignment(Pos.CENTER_LEFT);
    
        List<CheckBox> checkBoxes = new ArrayList<>();
        for (Artefact artefact : allArtefacts) {
            CheckBox cb = new CheckBox(artefact.getTitle());
            cb.setUserData(artefact);
            checkBoxes.add(cb);
        }
        layout.getChildren().addAll(checkBoxes);
    
        Button deleteButton = new Button("Hapus");
        deleteButton.setStyle("-fx-background-color: #C62828; -fx-text-fill: white;");
        deleteButton.setOnAction(e -> {
            List<Artefact> artefakTerpilih = new ArrayList<>();
            for (CheckBox cb : checkBoxes) {
                if (cb.isSelected()) {
                    artefakTerpilih.add((Artefact) cb.getUserData());
                }
            }
            if (artefakTerpilih.isEmpty()) {
                Alert alert = new Alert(Alert.AlertType.WARNING, "Pilih minimal satu artefak!", ButtonType.OK);
                alert.showAndWait();
                return;
            }
        
            StringBuilder sb = new StringBuilder("Yakin ingin menghapus artefak berikut?\n");
            for (Artefact a : artefakTerpilih) {
                sb.append("- ").append(a.getTitle()).append("\n");
            }
            Alert confirmAlert = new Alert(Alert.AlertType.CONFIRMATION, sb.toString(), ButtonType.OK, ButtonType.CANCEL);
            confirmAlert.setTitle("Konfirmasi Hapus Artefak");
            confirmAlert.setHeaderText("Konfirmasi Penghapusan");
        
            confirmAlert.showAndWait().ifPresent(response -> {
                if (response == ButtonType.OK) {
                    try (Connection conn = new DatabaseConnection().getConnection()) {
                        for (Artefact artefact : artefakTerpilih) {

                            String deleteRelasiKurator = "DELETE FROM Kurator_Artefact WHERE artefactID = ?";
                            try (PreparedStatement pstmtKurator = conn.prepareStatement(deleteRelasiKurator)) {
                                pstmtKurator.setInt(1, artefact.getArtefactID());
                                pstmtKurator.executeUpdate();
                            }
                            String sql = "DELETE FROM Artefact WHERE artefactID = ?";
                            try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
                                pstmt.setInt(1, artefact.getArtefactID());
                                pstmt.executeUpdate();
                            }
                        }
                    } catch (SQLException ex) {
                        Alert errorAlert = new Alert(Alert.AlertType.ERROR, "Gagal menghapus artefak. Pastikan tidak sedang digunakan di Exhibit!", ButtonType.OK);
                        errorAlert.showAndWait();
                    }
                    loadAllArtefactsFromDB();
                    displayArtefacts(allArtefacts);
                    dialogStage.close();
                }
            });
        });
        layout.getChildren().add(deleteButton);
    
        dialogStage.setScene(new Scene(layout));
        dialogStage.showAndWait();
    }
    
    // --- Handler untuk Delete All ---
    private void handleDeleteAllArtefacts() {
        Alert confirm = new Alert(Alert.AlertType.CONFIRMATION, "Yakin ingin menghapus SEMUA artefak?", ButtonType.YES, ButtonType.NO);
        confirm.showAndWait().ifPresent(response -> {
            if (response == ButtonType.YES) {
                try (Statement stmt = connection.createStatement()) {
                    stmt.executeUpdate("DELETE FROM Artefact");
                    allArtefacts.clear();
                    displayArtefacts(allArtefacts);
                } catch (SQLException e) {
                    Alert alert = new Alert(Alert.AlertType.ERROR, "Gagal menghapus semua artefak. Pastikan sedang tidak digunakan di Exhibit!", ButtonType.OK);
                    alert.showAndWait();
                }
            }
        });
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


    protected List<Artefact> filterByPeriod(List<Artefact> artefactList) {
        String fromPeriod = periodFromField != null ? periodFromField.getText().trim() : "";
        String toPeriod = periodToField != null ? periodToField.getText().trim() : "";
        if (fromPeriod.isEmpty() && toPeriod.isEmpty()) {
            return artefactList;
        }
        List<Artefact> filterArtefact = new java.util.ArrayList<>();
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

    @FXML
    private void onDeleteSelectedArtClick(ActionEvent event) {
        handleDeleteSelectedArtefacts();
    }

    @FXML
    private void onDeleteAllArtClick(ActionEvent event) {
        handleDeleteAllArtefacts();
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

            Label errorLabel = new Label();
            errorLabel.setStyle("-fx-text-fill: red;");

            Parent parent = page.getParent();
            if (parent instanceof VBox) {
                VBox dialogVBox = (VBox) parent;
                if (!dialogVBox.getChildren().contains(errorLabel)) {
                    dialogVBox.getChildren().add(errorLabel);
                }
            } else if (page instanceof GridPane) {
                ((GridPane) page).add(errorLabel, 0, 6, 2, 1); // kolom 0, row 6, span 2 kolom
            }

            saveButton.setOnAction(e -> {

                titleField.setStyle("");
                periodField.setStyle("");
                regionComboBox.setStyle("");
                errorLabel.setText("");
            
                String title = titleField.getText().trim();
                String region = regionComboBox.getValue();
                String periodText = periodField.getText().trim();
            
                if (title.isEmpty()) {
                    titleField.setStyle("-fx-border-color: red;");
                    errorLabel.setText("Judul artefak wajib diisi.");
                    return;
                }
                try (PreparedStatement checkStmt = connection.prepareStatement(
                        "SELECT artefactID FROM Artefact WHERE LOWER(title) = ?")) {
                    checkStmt.setString(1, title.toLowerCase());
                    ResultSet rs = checkStmt.executeQuery();
                    if (rs.next()) {
                        int foundId = rs.getInt("artefactID");
                        if (artefactToEdit == null || artefactToEdit.getArtefactID() != foundId) {
                            titleField.setStyle("-fx-border-color: red;");
                            errorLabel.setText("Judul artefak sudah ada di database.");
                            return;
                        }
                    }
                } catch (SQLException ex) {
                    errorLabel.setText("Gagal cek duplikasi judul.");
                    return;
                }
            
                if (region == null || region.isEmpty()) {
                    regionComboBox.setStyle("-fx-border-color: red;");
                    errorLabel.setText("Region wajib dipilih.");
                    return;
                }
            
                int period;
                try {
                    period = Integer.parseInt(periodText);
                    if (periodText.length() > 4 || period < 0) {
                        periodField.setStyle("-fx-border-color: red;");
                        errorLabel.setText("Periode harus berupa angka maksimal 4 digit.");
                        return;
                    }
                } catch (NumberFormatException ex) {
                    periodField.setStyle("-fx-border-color: red;");
                    errorLabel.setText("Periode harus berupa angka.");
                    return;
                }
            
                try {
                    if (artefactToEdit == null) {
                        int nextId = 1;
                        String maxIdSql = "SELECT MAX(artefactID) FROM Artefact";
                        try (Statement stmt = connection.createStatement();
                             ResultSet rs = stmt.executeQuery(maxIdSql)) {
                            if (rs.next()) {
                                nextId = rs.getInt(1) + 1;
                            }
                        }
                        String sql = "INSERT INTO Artefact (artefactID, title, region, period, description, mediaURL) VALUES (?, ?, ?, ?, ?, ?)";
                        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
                            pstmt.setInt(1, nextId);
                            pstmt.setString(2, title);
                            pstmt.setString(3, region);
                            pstmt.setInt(4, period);
                            pstmt.setString(5, descriptionArea.getText());
                            pstmt.setString(6, mediaURLField.getText());
                            pstmt.executeUpdate();
                        }
                    } else {
                        String sql = "UPDATE Artefact SET title = ?, region = ?, period = ?, description = ?, mediaURL = ? WHERE artefactID = ?";
                        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
                            pstmt.setString(1, title);
                            pstmt.setString(2, region);
                            pstmt.setInt(3, period);
                            pstmt.setString(4, descriptionArea.getText());
                            pstmt.setString(5, mediaURLField.getText());
                            pstmt.setInt(6, artefactToEdit.getArtefactID());
                            pstmt.executeUpdate();
                        }
                    }
                    loadAllArtefactsFromDB();
                    displayArtefacts(allArtefacts);
                    dialogStage.close();
                } catch (SQLException ex) {
                    errorLabel.setText("Gagal menyimpan artefak.");
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
