//// them doc gia

package com.example.oop25;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.*;
import java.time.LocalDate;

public class AddReader {

    @FXML
    private TextField idCard; // CCCD (Căn cước công dân)

    @FXML
    private TextField phoneNumber; // Số điện thoại

    @FXML
    private TextField address; // Địa chỉ

    @FXML
    private TextField readerId; // Mã độc giả

    @FXML
    private DatePicker expirationDate; // Ngày hết hạn

    @FXML
    private DatePicker birthDate; // Ngày sinh

    @FXML
    private TextField readerName; // Tên độc giả

    @FXML
    void cancelAction(MouseEvent event) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("trangchuquanlidocgia.fxml"));
        Scene scene = new Scene(fxmlLoader.load());

        Stage stage = new Stage();
        stage.setTitle("Quản lý độc giả");
        stage.setScene(scene);
        stage.show();

        ((Node) (event.getSource())).getScene().getWindow().hide();
    }

    @FXML
    void addReaderAction(MouseEvent event) throws IOException {
        String readerIdText = readerId.getText().trim();
        String readerNameText = readerName.getText().trim();
        String addressText = address.getText().trim();
        String phoneNumberText = phoneNumber.getText().trim();
        String idCardText = idCard.getText().trim();
        String renewalDate = LocalDate.now().toString();
        String expirationDateText = expirationDate.getValue() != null ? expirationDate.getValue().toString() : null;

        if (!phoneNumberText.matches("\\d{10}")) {
            showAlert("Lỗi", "Số điện thoại không hợp lệ", "Số điện thoại phải có đúng 10 chữ số.");
            return;
        }

        if (!idCardText.matches("\\d{12}")) {
            showAlert("Lỗi", "CCCD không hợp lệ", "CCCD phải có đúng 12 chữ số.");
            return;
        }

        if (expirationDateText == null) {
            showAlert("Lỗi", "Ngày không hợp lệ", "Vui lòng chọn ngày hết hạn.");
            return;
        }

        if (renewalDate.compareTo(expirationDateText) >= 0) {
            showAlert("Lỗi", "Ngày không hợp lệ", "Ngày hết hạn phải sau ngày gia hạn.");
            return;
        }

        try (Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/library", "root", "1234")) {
            System.out.println("Kết nối thành công tới cơ sở dữ liệu!");

            String checkSQL = "SELECT COUNT(*) FROM `danh sách độc giả` WHERE madocgia = ?";
            try (PreparedStatement checkStmt = connection.prepareStatement(checkSQL)) {
                checkStmt.setString(1, readerIdText);
                ResultSet resultSet = checkStmt.executeQuery();
                if (resultSet.next() && resultSet.getInt(1) > 0) {
                    showAlert("Lỗi", "Mã độc giả bị trùng", "Mã độc giả này đã tồn tại trong hệ thống.");
                    return;
                }
            }

            String insertSQL = "INSERT INTO `danh sách độc giả` (madocgia, ten_docgia, thong_tin, ngay_giahan, ngay_hethan, ghi_chu) VALUES (?, ?, ?, ?, ?, ?)";
            try (PreparedStatement insertStmt = connection.prepareStatement(insertSQL)) {
                insertStmt.setString(1, readerIdText);
                insertStmt.setString(2, readerNameText);
                insertStmt.setString(3, phoneNumberText);
                insertStmt.setString(4, renewalDate);
                insertStmt.setString(5, expirationDateText);
                insertStmt.setString(6, "");

                int rowsInserted = insertStmt.executeUpdate();
                if (rowsInserted > 0) {
                    showAlert("Thông báo", "Thành công", "Đã thêm độc giả mới thành công.");
                } else {
                    showAlert("Lỗi", "Không thể thêm", "Có lỗi xảy ra khi thêm độc giả.");
                }
                System.out.println("SQL thực thi: " + insertSQL);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            showAlert("Lỗi", "Lỗi cơ sở dữ liệu", "Không thể kết nối hoặc thao tác với cơ sở dữ liệu.");
            return;
        }

        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("danhsachkhithemdocgia.fxml"));
        Scene scene = new Scene(fxmlLoader.load());

        Stage stage = new Stage();
        stage.setTitle("Danh sách độc giả");
        stage.setScene(scene);
        stage.show();

        ((Node) (event.getSource())).getScene().getWindow().hide();
    }

    public static void showAlert(String title, String header, String content) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(header);
        alert.setContentText(content);
        alert.showAndWait();
    }
}
