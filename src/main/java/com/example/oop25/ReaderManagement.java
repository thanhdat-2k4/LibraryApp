// trang chủ quan li doc gia
package com.example.oop25;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

import java.io.IOException;

public class ReaderManagement {

    @FXML
    void closeWindow(MouseEvent event) throws IOException {
        //đi đến trang chủ
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("Trangchu.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        Stage stage = new Stage();
        stage.setScene(scene);
        stage.setTitle("Trang chủ  !");
        stage.show();
        // xóa khung
        ((Node)(event.getSource())).getScene().getWindow().hide();
    }

    @FXML
    void searchReader(MouseEvent event) throws IOException {
        //đi đến tìm kiếm  độc giả
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("giaodientimkiemdocgia.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        Stage stage = new Stage();
        stage.setScene(scene);
        stage.setTitle("tìm kiếm  độc giả  !");
        stage.show();
        // xóa khung
        ((Node)(event.getSource())).getScene().getWindow().hide();
    }

    @FXML
    void editReader(MouseEvent event) throws IOException {
       //đi đến sưar  độc giả
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("giaodientimkiemdocgia.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        Stage stage = new Stage();
        stage.setScene(scene);
        stage.setTitle("Trang sửa thông tin !");
        stage.show();
        // xóa khung
        ((Node)(event.getSource())).getScene().getWindow().hide();
    }

    @FXML
    void addReader(MouseEvent event) throws IOException {
        //đi đến thêm  độc giả
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("themdocgia.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        Stage stage = new Stage();
        stage.setScene(scene);
        stage.setTitle("phiếu thêm độc giả  !");
        stage.show();
        // xóa khung
        ((Node)(event.getSource())).getScene().getWindow().hide();
    }

    @FXML
    void deleteReader(MouseEvent event) throws IOException {
        //đi đến thêm  độc giả
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("andocgia.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        Stage stage = new Stage();
        stage.setScene(scene);
        stage.show();
        // xóa khung
        ((Node)(event.getSource())).getScene().getWindow().hide();
    }
}
