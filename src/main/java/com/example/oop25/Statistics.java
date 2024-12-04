// giao dien thong ke

package com.example.oop25;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

import java.io.IOException;

public class Statistics {

    @FXML
    void btnBorrowedBooksStats(MouseEvent event) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("thongkesachmuon.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        Stage stage = new Stage();
        stage.setScene(scene);
        stage.show();
        // xóa khung
        ((Node)(event.getSource())).getScene().getWindow().hide();
    }

    @FXML
    void btnBorrowerStats(MouseEvent event) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("ThongKeNguoiMuon.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        Stage stage = new Stage();
        stage.setScene(scene);
        stage.show();
        // xóa khung
        ((Node)(event.getSource())).getScene().getWindow().hide();
    }

    @FXML
    void btnRemainingBooksStats(MouseEvent event) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("thongkesachconlai.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        Stage stage = new Stage();
        stage.setScene(scene);
        stage.show();
        // xóa khung
        ((Node)(event.getSource())).getScene().getWindow().hide();
    }

    @FXML
    void goBack(MouseEvent event) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("Trangchu.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        Stage stage = new Stage();
        stage.setScene(scene);
        stage.show();
        // xóa khung
        ((Node)(event.getSource())).getScene().getWindow().hide();
    }

}
