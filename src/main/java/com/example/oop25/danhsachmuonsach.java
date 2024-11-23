package com.example.oop25;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.sql.*;
import java.util.ResourceBundle;

public class danhsachmuonsach implements Initializable {

    @FXML
    private ComboBox<String> loai_search;

    @FXML
    private TextField search;

    @FXML
    void click_in(MouseEvent event) {

    }

    @FXML
    void click_muon(MouseEvent event) {

    }

    @FXML
    void click_thoat(MouseEvent event) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("trangchuquanlidocgia.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 600, 580);
        Stage stage = null;
        stage.setTitle("Hello!");
        stage.setScene(scene);
        stage.show();
    }

    @FXML
    void click_tra(MouseEvent event) {

    }

    @FXML
    void nhap_search(KeyEvent event) throws SQLException {
        if (event.getCode() == KeyCode.ENTER) {
            int i = loai_search.getSelectionModel().getSelectedIndex();
            if (i == 0) {
                Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/library", "root", "1234");

                //    tìm kiểm theo mã thẻ
                String codeSQL = "select * from `danh sách độc giả` where mathe = ?;";
                Boolean isValid = false;
                try (PreparedStatement searcher = connection.prepareStatement(codeSQL)) {
                    searcher.setString(1, search.getText());
                    ResultSet set = searcher.executeQuery();
                    while (set.next()) {
                        System.out.println(set.getString("mathe") + " " +
                                set.getString("ten_docgia") + " " +
                                set.getString("thong_tin") + " " +
                                set.getString("ngay_giahan") + " " +
                                set.getString("ngay_hethan"));
                        isValid = true;
                    }
                }
                // Hiển thị thông báo cho người dùng
                if (isValid) {
                    showAlert("Thông báo", "Mã độc giả hợp lệ!", "Mã bạn nhập đã được chấp nhận.");
                } else {
                    showAlert("Thông báo", "Mã độc giả không hợp lệ!", "Vui lòng kiểm tra và thử lại.");
                }

            } else if (i == 1) {
                Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/library", "root", "1234");
                //    tìm kiểm theo tên độc giả
                String codeSQL = "select * from `danh sách độc giả` where ten_docgia = ?;";
                Boolean isValid = false;
                try (PreparedStatement searcher = connection.prepareStatement(codeSQL)) {
                    searcher.setString(1, search.getText());
                    ResultSet set = searcher.executeQuery();
                    while (set.next()) {
                        System.out.println(set.getString("mathe") + " " +
                                set.getString("ten_docgia") + " " +
                                set.getString("thong_tin") + " " +
                                set.getString("ngay_giahan") + " " +
                                set.getString("ngay_hethan"));
                        isValid = true;
                    }
                }
                // Hiển thị thông báo cho người dùng
                if (isValid) {
                    showAlert("Thông báo", "Mã độc giả hợp lệ!", "Mã bạn nhập đã được chấp nhận.");
                } else {
                    showAlert("Thông báo", "Mã độc giả không hợp lệ!", "Vui lòng kiểm tra và thử lại.");
                }

            } else if (i == 2) {

                Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/library", "root", "1234");

                //    tìm kiểm theo ngày gia hạn
                String codeSQL = "select * from `danh sách độc giả` where ngay_giahan = ?;";
                Boolean isValid = false;
                try (PreparedStatement searcher = connection.prepareStatement(codeSQL)) {
                    searcher.setString(1, search.getText());
                    ResultSet set = searcher.executeQuery();
                    while (set.next()) {
                        System.out.println(set.getString("mathe") + " " +
                                set.getString("ten_docgia") + " " +
                                set.getString("thong_tin") + " " +
                                set.getString("ngay_giahan") + " " +
                                set.getString("ngay_hethan"));
                        isValid = true;
                    }
                }
                // Hiển thị thông báo cho người dùng
                if (isValid) {
                    showAlert("Thông báo", "Mã độc giả hợp lệ!", "Mã bạn nhập đã được chấp nhận.");
                } else {
                    showAlert("Thông báo", "Mã độc giả không hợp lệ!", "Vui lòng kiểm tra và thử lại.");
                }


            } else if (i == 3) {

                Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/library", "root", "1234");

                //    tìm kiểm theo ngày hết hạn
                String codeSQL = "select * from `danh sách độc giả` where ngay_hethan = ?;";
                Boolean isValid = false;
                try (PreparedStatement searcher = connection.prepareStatement(codeSQL)) {
                    searcher.setString(1, search.getText());
                    ResultSet set = searcher.executeQuery();
                    while (set.next()) {
                        System.out.println(set.getString("mathe") + " " +
                                set.getString("ten_docgia") + " " +
                                set.getString("thong_tin") + " " +
                                set.getString("ngay_giahan") + " " +
                                set.getString("ngay_hethan"));
                        isValid = true;
                    }
                }
                // Hiển thị thông báo cho người dùng
                if (isValid) {
                    showAlert("Thông báo", "Mã độc giả hợp lệ!", "Mã bạn nhập đã được chấp nhận.");
                } else {
                    showAlert("Thông báo", "Mã độc giả không hợp lệ!", "Vui lòng kiểm tra và thử lại.");
                }


            } else {
                showAlert("Chưa chọn loại tìm kiếm", "Lỗi", "Lối");
            }
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        ObservableList<String> phuongphap = FXCollections.observableArrayList(
                "Tìm kiếm theo mã thẻ",
                "Tìm kiếm theo tên",
                "Tìm kiếm theo ngày gia hạn",
                "Tìm kiếm theo ngày hết hạn"
        );
        loai_search.setItems(phuongphap);
    }

    public static void showAlert(String title, String header, String content) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(header);
        alert.setContentText(content);
        alert.showAndWait();
    }
}
