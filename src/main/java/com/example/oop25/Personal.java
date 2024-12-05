package com.example.oop25;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

public class Personal {

    @FXML
    void goBackToHome(MouseEvent event) {
        try {
            // Quay lại màn hình chính (Home)
            FXMLLoader loader = new FXMLLoader(getClass().getResource("Trangchu.fxml"));
            Scene scene = new Scene(loader.load());
            Stage stage = (Stage) ((javafx.scene.Node) event.getSource()).getScene().getWindow();
            stage.setScene(scene);
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Không thể chuyển về màn hình Home.");
        }
    }

    @FXML
    void openGuide(MouseEvent event) {
        try {
            // Chuyển đến màn hình hướng dẫn sử dụng app
            FXMLLoader loader = new FXMLLoader(getClass().getResource("Guide.fxml"));
            Scene scene = new Scene(loader.load());
            Stage stage = (Stage) ((javafx.scene.Node) event.getSource()).getScene().getWindow();
            stage.setScene(scene);
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Không thể mở màn hình Hướng dẫn.");
        }
    }

    @FXML
    void openEditPersonalInfo(MouseEvent event) {
        try {
            // Chuyển đến màn hình Đổi thông tin nhân viên
            FXMLLoader loader = new FXMLLoader(getClass().getResource("EditPersonalInfo.fxml"));
            Scene scene = new Scene(loader.load());
            Stage stage = (Stage) ((javafx.scene.Node) event.getSource()).getScene().getWindow();
            stage.setScene(scene);
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Không thể mở màn hình Đổi thông tin nhân viên.");
        }
    }

    @FXML
    void openChangePassword(MouseEvent event) {
        try {
            // Chuyển đến màn hình Đổi mật khẩu
            FXMLLoader loader = new FXMLLoader(getClass().getResource("ChangePass.fxml"));
            Scene scene = new Scene(loader.load());
            Stage stage = (Stage) ((javafx.scene.Node) event.getSource()).getScene().getWindow();
            stage.setScene(scene);
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Không thể mở màn hình Đổi mật khẩu.");
        }
    }
}
