package com.example.oop25;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.event.ActionEvent;
import java.io.IOException;

public class ThongKe {

    // Phương thức dùng chung để chuyển scene
    private void changeScene(ActionEvent event, String fxmlFile, String title, double width, double height) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource(fxmlFile));
            Scene scene = new Scene(fxmlLoader.load(), width, height);

            // Lấy Stage hiện tại
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setTitle(title);
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            System.err.println("Không thể tải file FXML: " + fxmlFile);
            e.printStackTrace();
        }
    }

    @FXML
    void click_sachmuon(ActionEvent event) {
        changeScene(event, "thongkesachmuon.fxml", "Thống Kê Sách Mượn", 1000, 600);
    }

    @FXML
    void click_sachconlai(ActionEvent event) {
        changeScene(event, "thongkesachconlai.fxml", "Thống Kê Sách Còn Lại", 1000, 600);
    }

    @FXML
    void click_nguoimuon(ActionEvent event) {
        changeScene(event, "thongkenguoimuon.fxml", "Thống Kê Người Mượn", 1000, 600);
    }

    @FXML
    void click_quaylai(ActionEvent event) {
        changeScene(event, "Trangchu.fxml", "Trang Chủ", 1000, 600);
    }
}