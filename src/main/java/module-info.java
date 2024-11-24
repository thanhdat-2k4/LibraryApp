module com.example.oop25 {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;
    requires com.dlsc.formsfx;
    requires org.kordamp.bootstrapfx.core;
    requires java.desktop;
    requires java.sql;
    requires mysql.connector.java;
    requires jdk.javadoc;

    opens com.example.oop25 to javafx.fxml;
    exports com.example.oop25;
}