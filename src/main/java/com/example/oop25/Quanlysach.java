package com.example.oop25;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

import java.io.IOException;

public class Quanlysach {

    @FXML
    void click_Suasach(MouseEvent event) {
        changeScene(event, "Suasach.fxml");
    }

    @FXML
    void click_Danhsach(MouseEvent event) {
        changeScene(event, "Danhsach.fxml");
    }

    @FXML
    void click_Themsach(MouseEvent event) {
        changeScene(event, "Themsach.fxml");
    }

    @FXML
    void click_Timkiemsach(MouseEvent event) {
        changeScene(event, "Timkiemsach.fxml");
    }

    @FXML
    void click_Xoasach(MouseEvent event) {
        changeScene(event, "Xoasach.fxml");
    }

    @FXML
    void click_returnTrangchu(MouseEvent event) {
        changeScene(event, "Trangchu.fxml");
    }

    // Hàm tiện ích để chuyển đổi giao diện
    private void changeScene(MouseEvent event, String fxmlFile) {
        try {
            // Tải giao diện từ file FXML
            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlFile));
            Parent root = loader.load();

            // Lấy stage hiện tại
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            Scene scene = new Scene(root);

            // Đặt giao diện mới
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}