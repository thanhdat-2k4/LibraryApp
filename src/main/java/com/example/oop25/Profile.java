// cá nhan
package com.example.oop25;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.event.ActionEvent;
import java.io.IOException;
public class Profile {

    private void changeScene(ActionEvent event, String fxmlFile, String title, double width, double height) throws IOException {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource(fxmlFile));
            Scene scene = new Scene(fxmlLoader.load(), width, height);
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setTitle(title);
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            System.err.println("Không thể tải file FXML: " + fxmlFile);
            e.printStackTrace();
            throw e;
        }
    }

    @FXML
    void click_danhsachnhanvien(ActionEvent event) throws IOException {
        changeScene(event, "danhsachnhanvien.fxml", "Danh Sách Nhân Viên", 1000, 600);
    }

    @FXML
    void click_doimatkhau(ActionEvent event) throws IOException {
        changeScene(event, "doimatkhau.fxml", "Đổi Mật Khẩu", 1000, 600);
    }

    @FXML
    void click_quaylai(ActionEvent event) throws IOException {
        changeScene(event, "Trangchu.fxml", "Trang Chủ", 1000, 600);
    }
}