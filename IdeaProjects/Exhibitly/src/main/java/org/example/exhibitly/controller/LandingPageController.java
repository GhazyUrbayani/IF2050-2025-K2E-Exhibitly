package org.example.exhibitly.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class LandingPageController implements Initializable {

    @FXML
    private Button onLoginButtonClick;
    @FXML
    private Button onExhibitButtonClick;
    @FXML
    private Button onArtefactButtonClick;
    @FXML
    private Button onTicketsButtonClick;
    @FXML
    private Button onLogoButtonClick;
    @FXML
    private Button logoButton;

    @FXML
    private void onLoginButtonClick() {
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

    // Metode untuk tombol navigasi lainnya (EXHIBIT, ARTEFACT, TICKETS)
    // Anda bisa menambahkan logika navigasi ke halaman yang sesuai di sini
    @FXML
    private void onExhibitButtonClick() {
        System.out.println("Tombol EXHIBIT diklik!");
    }

    @FXML
    private void onArtefactButtonClick() {
        System.out.println("Tombol ARTEFACT diklik!");
    }

    @FXML
    private void onTicketsButtonClick() {
        System.out.println("Tombol TICKETS diklik!");
        // Logika untuk menampilkan halaman tickets
    }

    @FXML
    public void onLogoButtonClick() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/org/example/exhibitly/LandingPage.fxml"));
            Parent landingPage = loader.load();

            Stage stage = (Stage) onLogoButtonClick.getScene().getWindow();

            stage.setScene(new Scene(landingPage));
            stage.setTitle("Museum Nusantara - Landing Page");
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            // Handle error, misalnya tampilkan dialog error
            System.err.println("Gagal memuat halaman login: " + e.getMessage());
        }
    }

    @FXML
    private ImageView LandingImageView;
    @FXML
    private ImageView Landing2ImageView;
    @FXML
    private ImageView myImageView;
    @FXML
    private ImageView logoFooter;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        // Pastikan path gambar sudah benar.
        // Jika gambar ada di src/main/resources/org/example/exhibitly/images/
        // maka path-nya adalah:

        try {
            Image image1 = new Image(getClass().getResource("/images/Landing.png").toExternalForm());
            LandingImageView.setImage(image1);
        } catch (NullPointerException e) {
            System.err.println("Gagal memuat Landing.png. Pastikan file ada di /org/example/exhibitly/images/Landing.png");
            e.printStackTrace();
        }
        try {
            Image image2 = new Image(getClass().getResource("/images/Landing2.png").toExternalForm());
            Landing2ImageView.setImage(image2);
        } catch (NullPointerException e) {
            System.err.println("Gagal memuat Landing2.png. Pastikan file ada di /org/example/exhibitly/images/Landing2.png");
            e.printStackTrace();
        }
        try {
            Image image4 = new Image(getClass().getResource("/images/logo2.png").toExternalForm());
            logoFooter.setImage(image4);
        } catch (NullPointerException e) {
            System.err.println("Gagal memuat logo2.png. Pastikan file ada di /org/example/exhibitly/images/Landing2.png");
            e.printStackTrace();
        }
        try {
            ImageView logoImageView = (ImageView) logoButton.getGraphic();
            logoImageView.setImage(new Image(getClass().getResourceAsStream("/images/logo.png")));
        } catch (Exception e) {
            System.err.println("Error loading logo for button: " + e.getMessage());
            e.printStackTrace();
        }

    }


}