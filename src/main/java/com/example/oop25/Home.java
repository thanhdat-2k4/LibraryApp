package com.example.oop25;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

import java.io.IOException;

public class Home {

    @FXML
    void btnManageBooks(MouseEvent event) throws IOException {
        // Chuyển sang giao diện Quanlysach.fxml
        changeScene(event, "Quanlysach.fxml", "Quản lý sách");

    }

    @FXML
    void goBack(MouseEvent event) throws IOException {
        // Chuyển về giao diện Login.fxml
        changeScene(event, "Login.fxml", "Đăng nhập");


    }

    // Hàm tiện ích để chuyển Scene
    private void changeScene(MouseEvent event, String fxmlFile, String title) throws IOException {
        try {
            // Load file FXML
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource(fxmlFile));
            Scene scene = new Scene(fxmlLoader.load());

            // Lấy Stage hiện tại từ sự kiện
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();

            // Thiết lập Scene và tiêu đề
            stage.setScene(scene);
            stage.setTitle(title);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    //quan li doc gia
    @FXML
    void ReaderManagement(MouseEvent event) throws IOException {
        changeScene(event, "trangchuquanlidocgia.fxml", "Quản lý độc giả");
    }

    //quan li muon sach
    @FXML
    void handleBorrowBookClick(MouseEvent event) throws IOException {
        changeScene(event, "quanlimuontra.fxml", "Quản lý mượn trả sách");
    }

    @FXML
    void btnManageProfile(MouseEvent event) throws IOException {
        changeScene(event, "Personal.fxml", "Cá nhân");
    }

    @FXML
    void btnStatistics(MouseEvent event) throws IOException {
        changeScene(event, "thongke.fxml", " Thống kê");

    }

}
