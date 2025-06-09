module org.example.exhibitly {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.graphics;
    requires javafx.swing;// Biasanya dibutuhkan untuk Image, ImageView

    requires org.controlsfx.controls;
    requires net.synedra.validatorfx;
    requires java.sql;
    requires mysql.connector.j;
    requires java.desktop; // Dibutuhkan untuk BufferedImage dan SwingFXUtils

    // --- Tambahan untuk ZXing ---
    requires com.google.zxing; // Modul untuk zxing-core
    requires com.google.zxing.javase; // Modul untuk zxing-javase (penting untuk konversi gambar)
    // --- Akhir Tambahan untuk ZXing ---

    // opens org.example.exhibitly to javafx.fxml; // Jika Anda tidak punya main class di root package
    opens org.example.exhibitly.controller to javafx.fxml;
    opens org.example.exhibitly.models to javafx.fxml; // Ganti 'models' menjadi 'model' sesuai package Anda

    // exports org.example.exhibitly; // Jika Anda punya main class di root package
    exports org.example.exhibitly.controller;
    exports org.example.exhibitly.models; // Ganti 'models' menjadi 'model' sesuai package Anda
}