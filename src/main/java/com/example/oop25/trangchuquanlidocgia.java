package com.example.oop25;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

import java.io.IOException;

public class trangchuquanlidocgia {

    @FXML
    private Button an_thong_tin;

    @FXML
    private Button seatch_docgia;

    @FXML
    private Button suathongtindocgia;

    @FXML
    private Button themdocgia;



    @FXML
    void click_dong(MouseEvent event) throws IOException {
        //đi đến trang chủ
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("Trangchu.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 600, 580);
        Stage stage = new Stage();
        stage.setScene(scene);
        stage.setTitle("Trang chủ  !");
        stage.show();
        // xóa khung
        ((Node)(event.getSource())).getScene().getWindow().hide();
    }

    @FXML
    void click_search(MouseEvent event) throws IOException {
        //đi đến tìm kiếm  độc giả
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("giaodientimkiemdocgia.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 600, 580);
        Stage stage = new Stage();
        stage.setScene(scene);
        stage.setTitle("tìm kiếm  độc giả  !");
        stage.show();
        // xóa khung
        ((Node)(event.getSource())).getScene().getWindow().hide();
    }

    @FXML
    void click_sua(MouseEvent event) throws IOException {
       //đi đến sưar  độc giả
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("giaodientimkiemdocgia.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 600, 580);
        Stage stage = new Stage();
        stage.setScene(scene);
        stage.setTitle("Trang sửa thông tin !");
        stage.show();
        // xóa khung
        ((Node)(event.getSource())).getScene().getWindow().hide();
    }

    @FXML
    void click_themdocgia(MouseEvent event) throws IOException {
        //đi đến thêm  độc giả
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("themdocgia.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 600, 580);
        Stage stage = new Stage();
        stage.setScene(scene);
        stage.setTitle("phiếu thêm độc giả  !");
        stage.show();
        // xóa khung
        ((Node)(event.getSource())).getScene().getWindow().hide();
    }

}
