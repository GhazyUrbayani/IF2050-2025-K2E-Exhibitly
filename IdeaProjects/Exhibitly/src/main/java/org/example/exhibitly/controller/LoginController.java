package org.example.exhibitly.controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.control.ProgressIndicator;
import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.AnchorPane;

import java.io.IOException;
import java.net.URL;
import java.sql.*;
import java.util.ResourceBundle;

public class LoginController implements Initializable {

    @FXML private TextField usernameField;
    @FXML private PasswordField passwordField;
    @FXML private Label loginMessageLabel;
    @FXML private Button loginButton;
    @FXML private Button logoButton;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        try {
            ImageView logoImageView = (ImageView) logoButton.getGraphic();
            logoImageView.setImage(new Image(getClass().getResourceAsStream("/images/logo.png")));
        } catch (Exception e) {
            System.err.println("Error loading logo: " + e.getMessage());
        }
    }

    @FXML
    protected void handleLogin(ActionEvent event) {
        String username = usernameField.getText();
        String password = passwordField.getText();

        if (username.isBlank() || password.isBlank()) {
            loginMessageLabel.setText("Username dan Password tidak boleh kosong.");
            return;
        }

        if (validateLogin(username, password)) {
            goToDashboard(event);
        } else {
            loginMessageLabel.setText("Username atau Password salah.");
        }
    }

    private boolean validateLogin(String username, String password) {
        String sql = "SELECT COUNT(1) FROM actor WHERE username = ? AND password = ?";
        try {
            DatabaseConnection connectNow = new DatabaseConnection();
            Connection connectDB = connectNow.getConnection();
            PreparedStatement preparedStatement = connectDB.prepareStatement(sql);
            preparedStatement.setString(1, username);
            preparedStatement.setString(2, password);

            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                return resultSet.getInt(1) == 1;
            }
        } catch (SQLException e) {
            System.err.println("Database error: " + e.getMessage());
        }
        return false;
    }

    private void goToDashboard(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/org/example/exhibitly/LandingDoneLoginPage.fxml"));
            Parent dashboardPage = loader.load();
            Stage stage = (Stage) ((javafx.scene.Node) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(dashboardPage));
            stage.setTitle("Museum Nusantara - Dashboard");
            stage.show();
        } catch (IOException e) {
            System.err.println("Gagal memuat dashboard: " + e.getMessage());
        }
    }

    @FXML
    public void onLogoButtonClick(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/org/example/exhibitly/LandingPage.fxml"));
            Parent landingPage = loader.load();
            Stage stage = (Stage) ((javafx.scene.Node) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(landingPage));
            stage.setTitle("Museum Nusantara - Landing Page");
            stage.show();
        } catch (IOException e) {
            System.err.println("Gagal memuat landing page: " + e.getMessage());
        }
    }

    @FXML
    public void onArtefactButtonClick(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/org/example/exhibitly/Artefact.fxml"));
            Parent artefactPage = loader.load();
            Stage stage = (Stage) ((javafx.scene.Node) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(artefactPage));
            stage.setTitle("Museum Nusantara - Artefacts");
            stage.show();
        } catch (IOException e) {
            System.err.println("Gagal memuat halaman Artefak: " + e.getMessage());
        }
    }

    @FXML
    public void onTicketsButtonClick(ActionEvent event) {
        // TODO: Implementasi halaman tiket
    }

    public void onLoginButtonClick(ActionEvent actionEvent) {
    }

    public void onExhibitButtonClick(ActionEvent actionEvent) {
    }
}
