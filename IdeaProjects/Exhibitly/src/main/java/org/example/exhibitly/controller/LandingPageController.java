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

public class LandingPageController extends BaseController implements Initializable {

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

    @FXML
    private Label userInfoLabel;

    private List<String> welcomeMessages;
    private int currentMessageIndex = 0;
    private Timeline timeline;

    // === Initialization ===
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        loadImages();
        setupWelcomeMessages();
        startWelcomeTextAnimation();
        updateUserInterface();
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

    private void updateUserInterface() {
        if (userInfoLabel != null) {
            if (session.isLoggedIn()) {
                userInfoLabel.setText("Welcome, " + session.getCurrentActor().getName() + "!");
                userInfoLabel.setVisible(true);
            } else {
                userInfoLabel.setVisible(false);
            }
        }
    }
    // === Navigation Buttons ===
    @FXML
    private void onLoginButtonClick(ActionEvent event) {
        if (session.isLoggedIn()) {
            System.out.println("User already logged in: " + getCurrentUserDisplay());
        } else {
            navigateToPage(event, "/org/example/exhibitly/login.fxml", false);
        }    
    }

    @FXML
    private void onLogoButtonClick(ActionEvent event) {
        if (session.isLoggedIn()) {
            navigateToPage(event, "/org/example/exhibitly/LandingDoneLoginPage.fxml");
        } else {
            navigateToPage(event, "/org/example/exhibitly/LandingPage.fxml");
        }
    }

    @FXML
    private void onExhibitButtonClick(ActionEvent event) {
        navigateToPage(event, "/org/example/exhibitly/Exhibit.fxml");
    }

    @FXML
    private void onArtefactButtonClick(ActionEvent event) { // <-- Metode ini harus menerima ActionEvent
        navigateToPage(event, "/org/example/exhibitly/Artefact.fxml");

    }

    @FXML
    private void onTicketButtonClick(ActionEvent event) {
        navigateToPage(event, "/org/example/exhibitly/Ticket.fxml");
    }

    @FXML
    private void onLogoutButtonClick(ActionEvent event) {
        handleLogout(event);
    }

    @FXML
    private void onMaintenanceButtonClick(ActionEvent event) {
        navigateToPage(event, "/org/example/exhibitly/MaintenancePage.fxml");
    }

}
