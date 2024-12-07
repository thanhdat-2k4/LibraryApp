// quan ly sach
package com.example.oop25;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

import java.io.IOException;

public class BookManager  {

    @FXML
    void editBook(MouseEvent event) {
        changeScene(event, "Suasach.fxml");
    }

    @FXML
    void viewBookList(MouseEvent event) {
        changeScene(event, "Danhsach.fxml");
    }

    @FXML
    void addBook(MouseEvent event) {
        changeScene(event, "Themsach.fxml");
    }

    @FXML
    void searchBook(MouseEvent event) {
        changeScene(event, "Timkiemsach.fxml");
    }

    @FXML
    void deleteBook(MouseEvent event) {
        changeScene(event, "Xoasach.fxml");
    }

    @FXML
    void returnToHomePage(MouseEvent event) {
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