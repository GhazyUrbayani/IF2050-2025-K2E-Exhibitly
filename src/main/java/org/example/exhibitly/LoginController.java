package org.example.exhibitly;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.event.ActionEvent;
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

    }

    // Metode untuk tombol "Login" di header (jika berbeda fungsinya dengan di form)
    @FXML
    protected void onLoginButtonClick(ActionEvent event) {

    }

    @FXML
    private ImageView myImageView;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        // Cara load gambar dari folder resources/images/logo.png
        Image image = new Image(getClass().getResource("/images/logo.png").toExternalForm());
        myImageView.setImage(image);
    }

    public void onExhibitButtonClick(ActionEvent actionEvent) {
    }

    public void onArtefactButtonClick(ActionEvent actionEvent) {
    }

    public void onTicketsButtonClick(ActionEvent actionEvent) {
    }
}