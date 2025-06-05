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
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
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
    @FXML
    private Label welcomeTextLabel;
    private List<String> welcomeMessages;
    private int currentMessageIndex = 0;
    private Timeline timeline;

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
        // Inisialisasi Pesan Selamat Datang
        welcomeMessages = new ArrayList<>(Arrays.asList(
                "SELAMAT DATANG",          // Bahasa Indonesia
                "RAHAJENG RAWUH",          // Bahasa Jawa (halus/krama)
                "HORAS",                   // Bahasa Batak
                "WILUJENG SUMPING",        // Bahasa Sunda
                "ASSALAMUALAIKUM"          // Bahasa Arab (umum diucapkan)
                // Tambahkan lebih banyak bahasa daerah jika diinginkan!
        ));

        // Panggil metode untuk memulai animasi teks
        startWelcomeTextAnimation();

    }
    private void startWelcomeTextAnimation() {
        // Set teks awal
        welcomeTextLabel.setText(welcomeMessages.get(currentMessageIndex));

        timeline = new Timeline(
                new KeyFrame(Duration.seconds(0.0), event -> {
                    // Fade out (memudar keluar) teks saat ini
                    FadeTransition fadeOut = new FadeTransition(Duration.seconds(0.5), welcomeTextLabel);
                    fadeOut.setFromValue(1.0);
                    fadeOut.setToValue(0.0);
                    fadeOut.setOnFinished(e -> {
                        // Setelah fade out selesai, ganti teks
                        currentMessageIndex = (currentMessageIndex + 1) % welcomeMessages.size();
                        welcomeTextLabel.setText(welcomeMessages.get(currentMessageIndex));

                        // Fade in (memudar masuk) teks baru
                        FadeTransition fadeIn = new FadeTransition(Duration.seconds(0.5), welcomeTextLabel);
                        fadeIn.setFromValue(0.0);
                        fadeIn.setToValue(1.0);
                        fadeIn.play();
                    });
                    fadeOut.play();
                })
        );
        // Durasi total untuk satu siklus (fade out + ganti teks + fade in + jeda sebelum siklus berikutnya)
        // Di sini: 0.5s fade out + 0.5s jeda (saat teks diganti) + 0.5s fade in + 2.5s jeda visual = 4.0 detik per pesan
        timeline.getKeyFrames().add(new KeyFrame(Duration.seconds(4.0))); // Ubah 4.0 sesuai kecepatan yang diinginkan

        timeline.setCycleCount(Timeline.INDEFINITE); // Ulangi terus-menerus
        timeline.play();
    }



}