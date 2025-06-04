module org.example.exhibitly {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.graphics;

    requires org.controlsfx.controls;
    requires net.synedra.validatorfx;

    opens org.example.exhibitly to javafx.fxml;
    exports org.example.exhibitly;
}