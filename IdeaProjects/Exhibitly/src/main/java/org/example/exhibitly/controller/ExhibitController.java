package org.example.exhibitly.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;
import javafx.fxml.Initializable;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class ExhibitController {
    
    @FXML
    private ImageView exhibitImageView;

    public void initialize(URL url, ResourceBundle resourceBundle) {
        try {
            Image image = new Image(getClass().getResource("/images/logo.png").toExternalForm());
            exhibitImageView.setImage(image);
        } catch (Exception e) {
            System.out.println("[Erorr] Couldn't load logo");
        }
    }

    @FXML
    public void handleLogin(ActionEvent actionEvent) {
        try {
            navigateToPage(actionEvent, "/org/example/exhibitly/Login.fxml");
        } catch (IOException e) {
            System.err.println("[Error] " + e.getMessage());
        }
    }

    @FXML 
    public void handleExhibit(ActionEvent actionEvent) {
        System.out.println("Already on Exhibit Page!");
    }

    @FXML
    public void handleArtefact(ActionEvent actionEvent) {
        try {
            navigateToPage(actionEvent, "/org/example/exhibitly/Artefact.fxml");
        } catch (IOException e) {
            System.err.println("[Error] " + e.getMessage());
        }
    }

    @FXML
    public void handleTickets(ActionEvent actionEvent) {
        System.out.println("Tickets button clicked!");
    }

    @FXML
    public void handleExhibit1(ActionEvent event) {
        System.out.println("Ancient Civilizations exhibit clicked!");
        // TODO: Navigate to detailed exhibit page
    }

    @FXML
    public void handleExhibit2(ActionEvent event) {
        System.out.println("Maritime Heritage exhibit clicked!");
        // TODO: Navigate to detailed exhibit page
    }

    private void navigateToPage(ActionEvent actionEvent, String path) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource(path));
        Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
        Scene scene = new Scene(root, 1366, 768);

        try {
            scene.getStylesheets().add(getClass().getResource("/css/style.css").toExternalForm());
        } catch (Exception e) {
            System.out.println("[Error] Couldn't load stylesheet");
        }

        stage.setScene(scene);
        stage.show();
    }
}
