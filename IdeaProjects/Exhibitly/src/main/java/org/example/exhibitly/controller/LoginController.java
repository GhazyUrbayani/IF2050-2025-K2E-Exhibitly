package org.example.exhibitly.controller;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.event.ActionEvent; // Penting untuk onAction
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.Initializable;



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

        System.out.println("Email: " + email);
        System.out.println("Password: " + password);

        // TODO: Implementasi logika login (misalnya, validasi, otentikasi ke database)
        // Contoh sederhana:
        if (email.equals("user@example.com") && password.equals("password123")) {
            System.out.println("Login Berhasil!");
            // Tambahkan navigasi ke halaman lain
        } else {
            System.out.println("Login Gagal: Email atau Password salah.");
            // Tampilkan pesan error di UI
        }
    }

    // Metode untuk tombol "Login" di header (jika berbeda fungsinya dengan di form)
    @FXML
    protected void onLoginButtonClick(ActionEvent event) {
        System.out.println("Tombol 'Login' di header diklik!");
        // Anda bisa tambahkan logika di sini, misalnya jika tombol ini membuka popup login
    }

    // Contoh metode dari template sebelumnya, jika diperlukan
    @FXML
    protected void onHelloButtonClick() {
        // Ini berasal dari template awal Anda, mungkin tidak relevan lagi untuk halaman login
        if (welcomeText != null) {
            welcomeText.setText("Welcome to JavaFX Application!");
        }
    }
    @FXML
    private ImageView myImageView;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        // Cara load gambar dari folder resources/images/logo.png
        Image image = new Image(getClass().getResource("/images/logo.png").toExternalForm());
        myImageView.setImage(image);
    }

    public void handleExhibit(ActionEvent actionEvent) {
    }

    public void handleArtefact(ActionEvent actionEvent) {
    }

    public void handleTickets(ActionEvent actionEvent) {
    }
}