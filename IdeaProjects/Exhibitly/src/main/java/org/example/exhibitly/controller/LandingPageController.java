package org.example.exhibitly.controller;

import javafx.animation.*;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.IOException;
import java.net.URL;
import java.util.*;

public class LandingPageController extends BaseController implements Initializable {

    @FXML
    private Button loginLogoutButton;
    @FXML
    private Button maintenanceButton;
    @FXML
    private Button onExhibitButtonClick;
    @FXML
    private Button onArtefactButtonClick;
    @FXML
    private Button onTicketButtonClick;
    @FXML
    private Button onLogoButtonClick;
    @FXML
    private Button logoButton;

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

    @FXML
    private ImageView KaryaKoleksiImageView;
    @FXML
    private StackPane karyaKoleksiPane;

    @FXML
    private VBox koleksiHoverArea;
    @FXML
    private VBox defaultContent;
    @FXML
    private VBox hoverContent;
    @FXML
    private Label titleLabel;
    @FXML
    private Label subtitleLabel;
    @FXML
    private Label paragraphLabel;

    private List<String> welcomeMessages;
    private int currentMessageIndex = 0;
    private Timeline timeline;


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        loadImages();
        setupWelcomeMessages();
        startWelcomeTextAnimation();
        updateUserInterface();

        koleksiHoverArea.setOnMouseEntered(e -> animateIn());
        koleksiHoverArea.setOnMouseExited(e -> animateOut());

        paragraphLabel.setOpacity(0.0);
        titleLabel.setTranslateY(0);
        subtitleLabel.setTranslateY(0);

    }

    private void animateIn() {
        // Geser ke atas
        TranslateTransition up = new TranslateTransition(Duration.millis(300), titleLabel);
        up.setToY(-40);

        // Geser ke bawah
        TranslateTransition down = new TranslateTransition(Duration.millis(300), subtitleLabel);
        down.setToY(40);

        // Fade in paragraf
        FadeTransition fadeIn = new FadeTransition(Duration.millis(300), paragraphLabel);
        fadeIn.setToValue(1.0);

        ParallelTransition pt = new ParallelTransition(up, down, fadeIn);
        pt.play();
    }

    private void animateOut() {
        // Kembalikan posisi
        TranslateTransition resetTitle = new TranslateTransition(Duration.millis(300), titleLabel);
        resetTitle.setToY(0);

        TranslateTransition resetSubtitle = new TranslateTransition(Duration.millis(300), subtitleLabel);
        resetSubtitle.setToY(0);

        // Fade out paragraf
        FadeTransition fadeOut = new FadeTransition(Duration.millis(300), paragraphLabel);
        fadeOut.setToValue(0.0);

        ParallelTransition pt = new ParallelTransition(resetTitle, resetSubtitle, fadeOut);
        pt.play();
    }

    private void loadImages() {
        Map<ImageView, String> imageMappings = new HashMap<>();
        imageMappings.put(LandingImageView, "/images/Landing.png");
        imageMappings.put(Landing2ImageView, "/images/Landing2.png");
        imageMappings.put(logoFooter, "/images/logo2.png");
        imageMappings.put(KaryaKoleksiImageView, "/images/eye.png");

        for (Map.Entry<ImageView, String> entry : imageMappings.entrySet()) {
            try {
                Image img = new Image(getClass().getResource(entry.getValue()).toExternalForm());
                entry.getKey().setImage(img);
            } catch (NullPointerException e) {
                System.err.println("Gagal memuat gambar: " + entry.getValue());
                e.printStackTrace();
            }
        }

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
                "SELAMAT DATANG",
                "RAHAJENG RAWUH",
                "HORAS",
                "WILUJENG SUMPING",
                "ASSALAMUALAIKUM"));
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
                new KeyFrame(Duration.seconds(4.0)));

        timeline.setCycleCount(Timeline.INDEFINITE);
        timeline.play();
    }

    private void updateUserInterface() {
        if (session != null && session.isLoggedIn()) {
            loginLogoutButton.setText("Logout");
            maintenanceButton.setVisible(true);

            if (userInfoLabel != null) {
                if (session.isLoggedIn()) {
                    userInfoLabel.setText("Welcome, " + session.getCurrentActor().getName() + "!");
                    userInfoLabel.setVisible(true);
                }
            }
        } else {
            loginLogoutButton.setText("Login");
            maintenanceButton.setVisible(false);

            if (userInfoLabel != null) {
                userInfoLabel.setVisible(false);
            }
        }
    }

    // === Navigation Buttons ===
    @FXML
    private void onLoginLogoutButtonClick(ActionEvent event) {
        if (session != null && session.isLoggedIn()) {
            handleLogout(event);
        } else {
            navigateToPage(event, "/org/example/exhibitly/login.fxml");
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
    private void onArtefactButtonClick(ActionEvent event) {
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

    public void refreshUIState() {
        updateUserInterface();
    }
}