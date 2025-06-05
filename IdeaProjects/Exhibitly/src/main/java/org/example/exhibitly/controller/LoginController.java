package org.example.exhibitly.controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.event.ActionEvent;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.Initializable;
import javafx.stage.Stage;


public class LoginController implements Initializable{

    @FXML
    private TextField emailField;

    @FXML
    private PasswordField passwordField;

    @FXML
    private Label welcomeText; // Jika Anda masih ingin menggunakan label ini di FXML lain

    // Metode untuk tombol "Masuk" pada form login
    @FXML
    protected void handleLogin(ActionEvent event) {
        String email = emailField.getText();
        String password = passwordField.getText();

    }
    @FXML
    private Button onLoginButtonClick;

    // Metode untuk tombol "Login" di header (jika berbeda fungsinya dengan di form)
    @FXML
    protected void onLoginButtonClick(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/org/example/exhibitly/login.fxml"));
            Parent loginPage = loader.load();

            Stage stage = (Stage) onLoginButtonClick.getScene().getWindow();

            stage.setScene(new Scene(loginPage));
            stage.setTitle("Museum Nusantara - Login");
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            // Handle error, misalnya tampilkan dialog error
            System.err.println("Gagal memuat halaman login: " + e.getMessage());
        }

    }

    @FXML
    private Button logoButton;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        // Cara load gambar dari folder resources/images/logo.png
        try {
            ImageView logoImageView = (ImageView) logoButton.getGraphic();
            logoImageView.setImage(new Image(getClass().getResourceAsStream("/images/logo.png")));
        } catch (Exception e) {
            System.err.println("Error loading logo for button: " + e.getMessage());
            e.printStackTrace();
        }

    }

    public void onExhibitButtonClick(ActionEvent actionEvent) {
    }

    @FXML
    private void onArtefactButtonClick(ActionEvent event) { // <-- Metode ini harus menerima ActionEvent
        try {
            // Path ke file FXML halaman Artefak
            // Pastikan path ini benar dan diawali dengan '/'
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/org/example/exhibitly/Artefact.fxml"));
            Parent artefactPage = loader.load();

            // Mendapatkan Stage dari tombol yang diklik
            Stage stage = (Stage) ((javafx.scene.Node) event.getSource()).getScene().getWindow();

            // Mengatur Scene baru ke halaman Artefak
            stage.setScene(new Scene(artefactPage));
            stage.setTitle("Museum Nusantara - Artefacts"); // Ubah judul jendela
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            System.err.println("Gagal memuat halaman Artefak: " + e.getMessage());
            // Tambahkan Alert atau notifikasi ke pengguna jika terjadi error loading
        }
    }

    public void onTicketsButtonClick(ActionEvent actionEvent) {
    }
    @FXML
    private Button onLogoButtonClick;


    @FXML
    public void onLogoButtonClick(ActionEvent event) { // <--- Tambahkan ActionEvent event
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/org/example/exhibitly/LandingPage.fxml"));
            Parent landingPage = loader.load();

            // Mendapatkan Stage dari tombol yang diklik
            Stage stage = (Stage) ((javafx.scene.Node) event.getSource()).getScene().getWindow(); // <--- Ini yang benar

            stage.setScene(new Scene(landingPage));
            stage.setTitle("Museum Nusantara - Landing Page");
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            System.err.println("Gagal memuat halaman landing page: " + e.getMessage());
            // Tambahkan dialog peringatan kepada pengguna jika perlu
        }



    }
}