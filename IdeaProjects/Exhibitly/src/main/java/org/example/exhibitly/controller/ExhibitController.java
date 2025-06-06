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
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import org.example.exhibitly.models.Artefact;
import org.example.exhibitly.models.Exhibit;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class ExhibitController implements Initializable {

    @FXML private Label exhibitTitleLabel;
    @FXML private Label exhibitDescriptionLabel;
    @FXML private Label curatorNameLabel;
    @FXML private ImageView exhibitImageView;
    @FXML private HBox artefactsContainer;
    @FXML private Button logoButton;
    @FXML private Button loginButton;
    @FXML private ImageView logoHeaderImageView;
    @FXML private ImageView logoFooter;

    private Exhibit exhibitData;
    private List<Artefact> artefactList;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        try {
            logoHeaderImageView.setImage(new Image(getClass().getResourceAsStream("/images/logo.png")));
        } catch (Exception e) {}
        try {
            logoFooter.setImage(new Image(getClass().getResourceAsStream("/images/logo2.png")));
        } catch (Exception e) {}

        artefactList = new ArrayList<>();
        artefactList.add(new Artefact(1, "ARCA GANESHA", "Jawa Timur", 800, "Arca Ganesha dari Jawa Timur.", "/images/arca1.png"));
        artefactList.add(new Artefact(2, "ARCA CIREBON", "Jawa Barat", 1400, "Arca Cirebon dari Jawa Barat.", "/images/arca2.png"));
        artefactList.add(new Artefact(3, "ARCA JATINANGOR", "Jawa Barat", 1200, "Arca Jatinangor dari Jawa Barat.", "/images/arca3.png"));
        artefactList.add(new Artefact(4, "ARCA DAGU", "DKI Jakarta", 1000, "Arca Dagu dari DKI Jakarta.", "/images/arca4.png"));

        exhibitData = new Exhibit(
                1,
                101,
                "ART HISTORY EXHIBITION",
                "Pameran ini menampilkan perjalanan sejarah seni rupa Indonesia dari masa klasik hingga modern. Pengunjung dapat menikmati koleksi artefak langka, lukisan, dan patung yang merepresentasikan kekayaan budaya Nusantara.",
                "/images/exhibit_main.png"
        );

        populateExhibitDetails();
    }

    private void populateExhibitDetails() {
        if (exhibitData == null) return;
        exhibitTitleLabel.setText(exhibitData.getTitle());
        curatorNameLabel.setText("");
        exhibitDescriptionLabel.setText(exhibitData.getDescription());
        try {
            exhibitImageView.setImage(new Image(getClass().getResourceAsStream(exhibitData.getMediaURL())));
        } catch (Exception e) {
            exhibitImageView.setImage(null);
        }

        artefactsContainer.getChildren().clear();
        for (Artefact artefact : artefactList) {
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
        try {
            imageView.setImage(new Image(getClass().getResourceAsStream(artefact.getMediaURL())));
        } catch (Exception e) {
            imageView.setImage(null); 
        }
        imageView.setFitWidth(120);
        imageView.setFitHeight(120);
        imageView.setPreserveRatio(true);

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
        try {
            detailImageView.setImage(new Image(getClass().getResourceAsStream(artefact.getMediaURL())));
        } catch (Exception e) {
            detailImageView.setImage(null);
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

    // --- Navigation Header ---
    @FXML
    private void onLogoButtonClick(ActionEvent actionEvent) {
        navigateToPage(actionEvent, "/org/example/exhibitly/LandingPage.fxml");
    }

    @FXML
    private void onLoginButtonClick(ActionEvent actionEvent) {
        navigateToPage(actionEvent, "/org/example/exhibitly/login.fxml");
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

    private void navigateToPage(ActionEvent event, String path) {
        String pageName = path.substring(path.lastIndexOf('/') + 1).replace(".fxml", "");
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(path));
            Parent root = loader.load();

            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow(); // Casting ke Node diperlukan

            // Buat Scene dengan ukuran tetap 1366x768
            Scene scene = new Scene(root, 1366, 768);
            stage.setScene(scene);

            // Set judul Stage secara konsisten
            stage.setTitle("Museum Nusantara - " + pageName);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            System.err.println("Gagal memuat halaman " + pageName + ": " + e.getMessage());
        }
    }
}
