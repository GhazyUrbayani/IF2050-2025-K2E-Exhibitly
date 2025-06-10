package org.example.exhibitly.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.Cursor;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

import org.example.exhibitly.models.Actor;
import org.example.exhibitly.models.Artefact;
import org.example.exhibitly.models.Exhibit;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class ExhibitController extends BaseController implements Initializable {

    @FXML private Label exhibitTitleLabel;
    @FXML private Label exhibitDescriptionLabel;
    @FXML private Label curatorNameLabel;
    @FXML private ImageView exhibitImageView;
    @FXML private HBox artefactsContainer;
    @FXML private Button logoButton;
    @FXML private Button loginLogoutButton;
    @FXML private ImageView logoHeaderImageView;
    @FXML private ImageView logoFooter;
    @FXML private Button editExhibitButton;
    @FXML private Button addArtefactToExhibitButton;

    private Exhibit exhibitData;
    private Connection connection;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        this.connection = new DatabaseConnection().getConnection();

        try {
            logoHeaderImageView.setImage(new Image(getClass().getResourceAsStream("/images/logo.png")));
        } catch (Exception e) {}
        try {
            logoFooter.setImage(new Image(getClass().getResourceAsStream("/images/logo2.png")));
        } catch (Exception e) {}
        
        updateLoginLogoutButton();

        loadExhibitDataFromDB(1);
          
        populateExhibitDetails();

        setupRoleBasedAccess();
    }

    private void loadExhibitDataFromDB(int exhibitId) {
        String exhibitSql = "SELECT e.*, a.name AS curatorName FROM Exhibit e JOIN Actor a ON e.kuratorID = a.actorID WHERE e.exhibitID = ?";
        
        try (PreparedStatement exhibitStmt = connection.prepareStatement(exhibitSql)) {
            exhibitStmt.setInt(1, exhibitId);
            ResultSet rs = exhibitStmt.executeQuery();

            if (rs.next()) {
                this.exhibitData = new Exhibit(
                    rs.getInt("exhibitID"),
                    rs.getInt("kuratorID"),
                    rs.getString("title"),
                    rs.getString("description"),
                    rs.getString("mediaURL")
                );
                this.exhibitData.setCuratorName(rs.getString("curatorName"));

                loadArtefactsForExhibit(exhibitId);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void loadArtefactsForExhibit(int exhibitId) {
        String artefactSql = "SELECT a.* FROM Artefact a JOIN Artefact_Exhibit ae ON a.artefactID = ae.artefactID WHERE ae.exhibitID = ?";
        List<Artefact> artefacts = new ArrayList<>();
        
        try (PreparedStatement artefactStmt = connection.prepareStatement(artefactSql)) {
            artefactStmt.setInt(1, exhibitId);
            ResultSet rs = artefactStmt.executeQuery();
            
            while (rs.next()) {
                artefacts.add(new Artefact(
                    rs.getInt("artefactID"),
                    rs.getString("title"),
                    rs.getString("region"),
                    rs.getInt("period"),
                    rs.getString("description"),
                    rs.getString("mediaURL")
                ));
            }
            this.exhibitData.setArtefacts(artefacts);
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

    private void populateExhibitDetails() {
        if (exhibitData == null) return;
        exhibitTitleLabel.setText(exhibitData.getTitle());
        curatorNameLabel.setText("KURATOR: " + exhibitData.getCuratorName().toUpperCase());
        exhibitDescriptionLabel.setText(exhibitData.getDescription());
        try {
            exhibitImageView.setImage(new Image(getClass().getResourceAsStream(exhibitData.getMediaURL())));
        } catch (Exception e) {
            exhibitImageView.setImage(null);
        }

        artefactsContainer.getChildren().clear();
        for (Artefact artefact : exhibitData.getArtefacts()) {
            VBox artefactPreview = createArtefactPreview(artefact);
            artefactsContainer.getChildren().add(artefactPreview);
        }
    }

    private VBox createArtefactPreview(Artefact artefact) {
        VBox previewContainer = new VBox(10);
        previewContainer.setAlignment(Pos.CENTER);
        previewContainer.setPrefWidth(180);
        previewContainer.setStyle("-fx-background-color: #fff; -fx-border-color: #ddd; -fx-border-radius: 8; -fx-padding: 10; -fx-background-radius: 8;");

        ImageView imageView = new ImageView();
        imageView.setFitWidth(120);
        imageView.setFitHeight(120);
        imageView.setPreserveRatio(true);

        Image img = null;
        String url = artefact.getMediaURL();
        if (url != null && !url.isEmpty()) {
            try {
                java.io.InputStream is = getClass().getResourceAsStream(url);
                if (is != null) {
                    img = new Image(is);
                }
            } catch (Exception ignored) {}
        }
        if (img == null || img.isError()) {
            java.io.InputStream placeholderStream = getClass().getResourceAsStream("/org/example/exhibitly/images/placeholder.png");
            if (placeholderStream != null) {
                img = new Image(placeholderStream);
            }
        }
        imageView.setImage(img);

        Label nameLabel = new Label(artefact.getTitle());
        nameLabel.setStyle("-fx-font-family: 'Plus Jakarta Sans Bold'; -fx-font-size: 14px; -fx-text-fill: #222;");

        Button detailButton = new Button("Lihat Selengkapnya");
        detailButton.setStyle("-fx-background-color: #333; -fx-text-fill: white; -fx-font-family: 'Plus Jakarta Sans Regular';");
        detailButton.setCursor(Cursor.HAND);

        detailButton.setOnAction(event -> {
            handleArtefactDetailClick(artefact); 
        });

        previewContainer.getChildren().addAll(imageView, nameLabel, detailButton);

        return previewContainer;
    }

    private void handleArtefactDetailClick(Artefact artefact) {
        System.out.println("Menampilkan pop-up detail untuk Artefak ID: " + artefact.getArtefactID());

        Stage detailStage = new Stage();

        VBox detailLayout = new VBox(15);
        detailLayout.setPadding(new javafx.geometry.Insets(25));
        detailLayout.setAlignment(Pos.CENTER);
        detailLayout.setStyle("-fx-background-color: #FAFAFA;");

        ImageView detailImageView = new ImageView();
        detailImageView.setFitWidth(300);
        detailImageView.setFitHeight(300);
        detailImageView.setPreserveRatio(true);
        String imageUrl = artefact.getMediaURL();
        try {
            Image image;
            if (imageUrl != null && imageUrl.startsWith("http")) {
                image = new Image(imageUrl, true); 
            } else {
                image = new Image(getClass().getResourceAsStream(imageUrl));
            }
            detailImageView.setImage(image);
        } catch (Exception e) {
            System.err.println("Gagal memuat gambar detail: " + imageUrl);
        }

        Label name = new Label(artefact.getTitle());
        name.setStyle("-fx-font-family: 'Playfair Display SC Bold'; -fx-font-size: 24px;");

        Label region = new Label("Region: " + artefact.getRegion());
        region.setStyle("-fx-font-family: 'Plus Jakarta Sans Regular'; -fx-font-size: 16px;");

        Label period = new Label("Period: " + artefact.getPeriod());
        period.setStyle("-fx-font-family: 'Plus Jakarta Sans Regular'; -fx-font-size: 16px;");

        Label description = new Label(artefact.getDescription());
        description.setWrapText(true);
        description.setMaxWidth(400);
        description.setStyle("-fx-font-family: 'Plus Jakarta Sans Regular'; -fx-font-size: 14px; -fx-text-fill: #444;");

        Button closeButton = new Button("Close");
        closeButton.setOnAction(e -> detailStage.close());
        closeButton.setCursor(Cursor.HAND);
        closeButton.setStyle("-fx-background-color: #555; -fx-text-fill: white; -fx-font-size: 14px; -fx-padding: 8px 20px;");

        detailLayout.getChildren().addAll(detailImageView, name, region, period, description, closeButton);

        detailStage.setScene(new Scene(detailLayout));
        detailStage.setTitle("Detail: " + artefact.getTitle());
        detailStage.show();
    }

    private void setupRoleBasedAccess() {
        boolean isKurator = session.isKurator();
        if (editExhibitButton != null) {
            editExhibitButton.setVisible(isKurator);
            editExhibitButton.setManaged(isKurator);
        }
        if (addArtefactToExhibitButton != null) {
            addArtefactToExhibitButton.setVisible(isKurator);
            addArtefactToExhibitButton.setManaged(isKurator);
        }
    }

    // --- Navigation Header ---
    @FXML
    private void onLogoButtonClick(ActionEvent actionEvent) {
        navigateToPage(actionEvent, "/org/example/exhibitly/LandingPage.fxml");
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
    private void onEditExhibitButtonClick(ActionEvent event) {
        Stage dialogStage = new Stage();
        dialogStage.setTitle("Edit Exhibit");
        dialogStage.initModality(Modality.WINDOW_MODAL);
        dialogStage.initOwner(exhibitTitleLabel.getScene().getWindow());

        VBox layout = new VBox(15);
        layout.setPadding(new javafx.geometry.Insets(20));
        layout.setStyle("-fx-background-color: #FAFAFA;");

        TextField titleField = new TextField(exhibitData.getTitle());
        titleField.setPromptText("Judul Pameran");

        TextArea descriptionArea = new TextArea(exhibitData.getDescription());
        descriptionArea.setPromptText("Deskripsi Pameran");
        descriptionArea.setPrefRowCount(4);

        TextField mediaURLField = new TextField(exhibitData.getMediaURL());
        mediaURLField.setPromptText("Media URL");

        HBox buttonBox = new HBox(10);
        Button saveButton = new Button("Simpan");
        Button cancelButton = new Button("Batal");
        buttonBox.getChildren().addAll(saveButton, cancelButton);

        layout.getChildren().addAll(
            new Label("Edit Judul:"), titleField,
            new Label("Edit Deskripsi:"), descriptionArea,
            new Label("Edit Media URL:"), mediaURLField,
            buttonBox
        );

        saveButton.setOnAction(e -> {
            try {
                int loggedInKuratorId = session.getActor().getActorID();

                String sql = "UPDATE Exhibit SET title = ?, description = ?, mediaURL = ?, kuratorID = ? WHERE exhibitID = ?";
                try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
                    pstmt.setString(1, titleField.getText());
                    pstmt.setString(2, descriptionArea.getText());
                    pstmt.setString(3, mediaURLField.getText());
                    pstmt.setInt(4, loggedInKuratorId);
                    pstmt.setInt(5, exhibitData.getExhibitID());
                    pstmt.executeUpdate();
                }
                loadExhibitDataFromDB(exhibitData.getExhibitID());
                populateExhibitDetails();
                dialogStage.close();
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        });

        cancelButton.setOnAction(e -> dialogStage.close());

        dialogStage.setScene(new Scene(layout));
        dialogStage.showAndWait();
    }

    @FXML
    private void onAddArtefactToExhibitClick(ActionEvent event) {
        try {
            Stage dialogStage = new Stage();
            dialogStage.setTitle("Tambah Artefak ke Exhibit");
            dialogStage.initModality(Modality.WINDOW_MODAL);
            dialogStage.initOwner(exhibitTitleLabel.getScene().getWindow());

            VBox layout = new VBox(10);
            layout.setPadding(new javafx.geometry.Insets(20));
            layout.setAlignment(Pos.CENTER_LEFT);

            List<Artefact> artefactsNotInExhibit = getArtefactsNotInExhibit();
            List<CheckBox> checkBoxes = new ArrayList<>();
            for (Artefact artefact : artefactsNotInExhibit) {
                CheckBox cb = new CheckBox(artefact.getTitle());
                cb.setUserData(artefact);
                checkBoxes.add(cb);
            }
            layout.getChildren().addAll(checkBoxes);

            Button addButton = new Button("Tambah");
            addButton.setOnAction(e -> {
                for (CheckBox cb : checkBoxes) {
                    if (cb.isSelected()) {
                        Artefact artefact = (Artefact) cb.getUserData();
                        addArtefactToExhibit(artefact.getArtefactID(), exhibitData.getExhibitID());
                    }
                }
                loadExhibitDataFromDB(exhibitData.getExhibitID());
                populateExhibitDetails();
                dialogStage.close();
            });
            layout.getChildren().add(addButton);

            dialogStage.setScene(new Scene(layout));
            dialogStage.showAndWait();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private List<Artefact> getArtefactsNotInExhibit() {
        List<Artefact> artefacts = new ArrayList<>();
        String sql = "SELECT * FROM Artefact WHERE artefactID NOT IN (SELECT artefactID FROM Artefact_Exhibit WHERE exhibitID = ?)";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, exhibitData.getExhibitID());
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                artefacts.add(new Artefact(
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
        return artefacts;
    }

    private void addArtefactToExhibit(int artefactID, int exhibitID) {
        String sql = "INSERT INTO Artefact_Exhibit (artefactID, exhibitID) VALUES (?, ?)";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, artefactID);
            stmt.setInt(2, exhibitID);
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void onExhibitButtonClick(ActionEvent actionEvent) {
        System.out.println("Sudah di halaman Exhibit!");
    }

    @FXML
    private void onArtefactButtonClick(ActionEvent actionEvent) {
        navigateToPage(actionEvent, "/org/example/exhibitly/Artefact.fxml");
    }

    @FXML
    private void onTicketsButtonClick(ActionEvent actionEvent) {
        navigateToPage(actionEvent, "/org/example/exhibitly/Ticket.fxml");
    }
}
