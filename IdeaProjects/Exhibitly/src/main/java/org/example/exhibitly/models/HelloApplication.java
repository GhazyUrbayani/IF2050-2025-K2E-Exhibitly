package org.example.exhibitly.models;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.text.Font;
import javafx.stage.Stage;

import java.io.IOException;

public class HelloApplication extends Application {

    @Override
    public void start(Stage stage) throws IOException {
        Font.loadFont(getClass().getResourceAsStream("/fonts/PlusJakartaSans-Bold.ttf"), 14);
        Font.loadFont(getClass().getResourceAsStream("/fonts/PlusJakartaSans-Regular.ttf"), 14);
        Font.loadFont(getClass().getResourceAsStream("/fonts/PlayfairDisplaySC-Bold.ttf"), 14);
        Font.loadFont(getClass().getResourceAsStream("/fonts/PlayfairDisplaySC-Regular.ttf"), 14);

        Parent root = FXMLLoader.load(getClass().getResource("/org/example/exhibitly/LandingPage.fxml"));

        Scene scene = new Scene(root, 1366, 768);

        scene.getStylesheets().add(getClass().getResource("/css/style.css").toExternalForm());
   
        stage.setScene(scene);

        stage.setTitle("Museum Nusantara - LandingPage");

        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}