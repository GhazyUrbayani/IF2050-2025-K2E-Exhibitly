package org.example.exhibitly.models;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent; // Penting: Import Parent
import javafx.scene.Scene;
import javafx.scene.text.Font; // Penting: Import Font
import javafx.stage.Stage;

import java.io.IOException;

public class HelloApplication extends Application {

    @Override
    public void start(Stage stage) throws IOException {
        // 1. Load Fonts terlebih dahulu (ini sudah benar)
        // Pastikan jalur ke file font sudah benar, dimulai dari root classpath (dengan '/')
        // Misalnya, jika font ada di src/main/resources/fonts/
        Font.loadFont(getClass().getResourceAsStream("/fonts/PlusJakartaSans-Bold.ttf"), 14);
        Font.loadFont(getClass().getResourceAsStream("/fonts/PlusJakartaSans-Regular.ttf"), 14);
        Font.loadFont(getClass().getResourceAsStream("/fonts/PlayfairDisplaySC-Bold.ttf"), 14);
        Font.loadFont(getClass().getResourceAsStream("/fonts/PlayfairDisplaySC-Regular.ttf"), 14);

        // 2. Load FXML *sebelum* membuat Scene
        // Anda mendeklarasikan 'root' setelah menggunakannya, ini akan menyebabkan error kompilasi
        Parent root = FXMLLoader.load(getClass().getResource("/org/example/exhibitly/LandingPage.fxml"));

        // 3. Buat Scene dengan 'root' yang sudah di-load
        Scene scene = new Scene(root, 1366, 768);

        // 4. Tambahkan Stylesheet ke Scene
        // Pastikan jalur ke file CSS sudah benar. Jika 'css/styles.css' ada di package yang sama dengan HelloApplication.java
        scene.getStylesheets().add(getClass().getResource("/css/style.css").toExternalForm());
        // Jika css ada di root resources folder, atau di package lain, gunakan path absolut:
        // scene.getStylesheets().add(getClass().getResource("/path/to/your/css/styles.css").toExternalForm());


        // 5. Set Scene ke Stage
        stage.setScene(scene);

        // 6. Set Judul Stage
        stage.setTitle("Museum Nusantara - LandingPage");

        // 7. Tampilkan Stage
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}