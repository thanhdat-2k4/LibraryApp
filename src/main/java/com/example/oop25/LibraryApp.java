package com.example.oop25;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class LibraryApp {

    @FXML
    void onThongKeClick(ActionEvent event) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("thongke.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 1000, 600);

        // Khởi tạo đối tượng Stage
        Stage stage = (Stage) ((javafx.scene.Node) event.getSource()).getScene().getWindow();

        stage.setTitle("Thống Kê");
        stage.setScene(scene);
        stage.show();
    }

    @FXML
    void onCaNhanClick(ActionEvent event) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("canhan.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 1000, 600);

        // Khởi tạo đối tượng Stage
        Stage stage = (Stage) ((javafx.scene.Node) event.getSource()).getScene().getWindow();

        stage.setTitle("Cá Nhân");
        stage.setScene(scene);
        stage.show();
    }
}