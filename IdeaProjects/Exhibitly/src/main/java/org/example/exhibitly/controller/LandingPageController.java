package org.example.exhibitly.controller;

import javafx.animation.FadeTransition;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.IOException;
import java.net.URL;
import java.util.*;

public class LandingPageController implements Initializable {

    // === FXML Components ===
    @FXML private Button LoginButton;
    @FXML private Button onExhibitButtonClick;
    @FXML private Button onArtefactButtonClick;
    @FXML private Button onTicketButtonClick;
    @FXML private Button onLogoButtonClick;
    @FXML private Button logoButton;

    @FXML
    private ImageView LandingImageView;
    @FXML
    private ImageView Landing2ImageView;
    @FXML
    private ImageView myImageView;
    @FXML
    private ImageView logoFooter;

    @FXML
    private Label welcomeTextLabel;

    private List<String> welcomeMessages;
    private int currentMessageIndex = 0;
    private Timeline timeline;

    // === Initialization ===
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        loadImages();
        setupWelcomeMessages();
        startWelcomeTextAnimation();
    }

    private void loadImages() {
        Map<ImageView, String> imageMappings = new HashMap<>();
        imageMappings.put(LandingImageView, "/images/Landing.png");
        imageMappings.put(Landing2ImageView, "/images/Landing2.png");
        imageMappings.put(logoFooter, "/images/logo2.png");

        for (Map.Entry<ImageView, String> entry : imageMappings.entrySet()) {
            try {
                Image img = new Image(getClass().getResource(entry.getValue()).toExternalForm());
                entry.getKey().setImage(img);
            } catch (NullPointerException e) {
                System.err.println("Gagal memuat gambar: " + entry.getValue());
                e.printStackTrace();
            }
        }

        // Set logo di dalam tombol
        try {
            ImageView logoImageView = (ImageView) logoButton.getGraphic();
            logoImageView.setImage(new Image(getClass().getResourceAsStream("/images/logo.png")));
        } catch (Exception e) {
            System.err.println("Error loading logo for button: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void setupWelcomeMessages() {
        welcomeMessages = new ArrayList<>(Arrays.asList(
                "SELAMAT DATANG",       // Bahasa Indonesia
                "RAHAJENG RAWUH",       // Bahasa Jawa
                "HORAS",                // Bahasa Batak
                "WILUJENG SUMPING",     // Bahasa Sunda
                "ASSALAMUALAIKUM"       // Umum
        ));
    }

    private void startWelcomeTextAnimation() {
        welcomeTextLabel.setText(welcomeMessages.get(currentMessageIndex));

        timeline = new Timeline(
                new KeyFrame(Duration.seconds(0.0), event -> {
                    FadeTransition fadeOut = new FadeTransition(Duration.seconds(0.5), welcomeTextLabel);
                    fadeOut.setFromValue(1.0);
                    fadeOut.setToValue(0.0);
                    fadeOut.setOnFinished(e -> {
                        currentMessageIndex = (currentMessageIndex + 1) % welcomeMessages.size();
                        welcomeTextLabel.setText(welcomeMessages.get(currentMessageIndex));

                        FadeTransition fadeIn = new FadeTransition(Duration.seconds(0.5), welcomeTextLabel);
                        fadeIn.setFromValue(0.0);
                        fadeIn.setToValue(1.0);
                        fadeIn.play();
                    });
                    fadeOut.play();
                }),
                new KeyFrame(Duration.seconds(4.0)) // Siklus tiap 4 detik
        );

        timeline.setCycleCount(Timeline.INDEFINITE);
        timeline.play();
    }

    // === Navigation Buttons ===
    @FXML
    private void onLoginButtonClick(ActionEvent actionEvent) {
        navigateToPage(actionEvent, "/org/example/exhibitly/login.fxml");
    }

    @FXML
    private void onLogoButtonClick(ActionEvent actionEvent) {
        navigateToPage(actionEvent, "/org/example/exhibitly/LandingPage.fxml");
    }

    @FXML
    private void onExhibitButtonClick(ActionEvent actionEvent) {
        navigateToPage(actionEvent, "/org/example/exhibitly/Exhibit.fxml");
    }

    @FXML
    private void onArtefactButtonClick(ActionEvent actionEvent) { // <-- Metode ini harus menerima ActionEvent
        navigateToPage(actionEvent, "/org/example/exhibitly/Artefact.fxml");

    }

    @FXML
    private void onTicketButtonClick(ActionEvent actionEvent) {
        navigateToPage(actionEvent, "/org/example/exhibitly/Ticket.fxml");
    }

    @FXML
    private void onLogoutButtonClick(ActionEvent event) {
        navigateToPage(event, "/org/example/exhibitly/login.fxml");
    }

    @FXML
    private void onMaintenanceButtonClick(ActionEvent event) {
        System.out.println("KONTOL!");
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
