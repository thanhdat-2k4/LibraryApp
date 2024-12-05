package com.example.oop25;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

public class Guide {
    @FXML
    void goBackPersonal(MouseEvent event) {
        try {
            // Quay lại màn hình chính (Home)
            FXMLLoader loader = new FXMLLoader(getClass().getResource("Personal.fxml"));
            Scene scene = new Scene(loader.load());
            Stage stage = (Stage) ((javafx.scene.Node) event.getSource()).getScene().getWindow();
            stage.setScene(scene);
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Không thể chuyển về màn hình Cá nhân .");
        }
    }
}
