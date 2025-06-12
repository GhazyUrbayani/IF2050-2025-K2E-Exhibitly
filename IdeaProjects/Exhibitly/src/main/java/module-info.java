module org.example.exhibitly {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.graphics;
    requires javafx.swing;

    requires org.controlsfx.controls;
    requires net.synedra.validatorfx;
    requires java.sql;
    requires mysql.connector.j;
    requires java.desktop;

    // --- Tambahan untuk ZXing ---
    requires com.google.zxing; 
    requires com.google.zxing.javase;

    // opens org.example.exhibitly to javafx.fxml;
    opens org.example.exhibitly.controller to javafx.fxml;
    opens org.example.exhibitly.models to javafx.fxml;

    // exports org.example.exhibitly;
    exports org.example.exhibitly.controller;
    exports org.example.exhibitly.models;
}