package com.example.oop25;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Date;

public class EditPersonalInfo {

    @FXML
    private TextField nameField;

    @FXML
    private DatePicker birthdateField;

    @FXML
    private TextField phoneField;

    @FXML
    void updatePersonalInfo(MouseEvent event) {
        // Lấy email từ lớp Login
        String email = Login.loggedInUserEmail;

        if (email == null || email.isEmpty()) {
            showError("Không tìm thấy email người dùng. Vui lòng đăng nhập lại.");
            return;
        }

        // Lấy dữ liệu từ các trường nhập liệu
        String name = nameField.getText();
        String phone = phoneField.getText();

        // Lấy ngày sinh từ DatePicker
        Date birthdate = null;
        if (birthdateField.getValue() != null) {
            birthdate = Date.valueOf(birthdateField.getValue());
        }

        // Kiểm tra nếu dữ liệu còn thiếu
        if (name.isEmpty() || phone.isEmpty() || birthdate == null) {
            showError("Vui lòng điền đầy đủ thông tin.");
            return;
        }

        // Kết nối đến cơ sở dữ liệu và thực hiện cập nhật thông tin
        try (Connection connection = DriverManager.getConnection(
                "jdbc:mysql://localhost:3306/library", "root", "1234")) {

            String sql = "UPDATE `nhân viên` SET `họ và tên` = ?, `ngày sinh` = ?, `số điện thoại` = ? WHERE email = ?";
            try (PreparedStatement statement = connection.prepareStatement(sql)) {
                statement.setString(1, name);
                statement.setDate(2, birthdate); // Chuyển đổi ngày từ LocalDate sang java.sql.Date
                statement.setString(3, phone);
                statement.setString(4, email);

                int rowsUpdated = statement.executeUpdate();
                if (rowsUpdated > 0) {
                    showInfo("Cập nhật thông tin thành công!");
                } else {
                    showError("Không tìm thấy người dùng với email này.");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            showError("Đã xảy ra lỗi khi cập nhật thông tin.");
        }
    }

    @FXML
    void goBackToPersonal(MouseEvent event) {
        try {
            // Tải Personal.fxml
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("Personal.fxml"));
            Parent root = fxmlLoader.load();

            // Lấy Stage hiện tại
            Stage stage = (Stage) ((javafx.scene.Node) event.getSource()).getScene().getWindow();

            // Đặt Scene mới từ Personal.fxml
            stage.setScene(new Scene(root));
            stage.setTitle("Thông tin cá nhân");
        } catch (Exception e) {
            e.printStackTrace();
            showError("Không thể chuyển về màn hình Personal.fxml.");
        }
    }

    @FXML
    void exitEditInfo(MouseEvent event) {
        // Đóng cửa sổ hiện tại
        Stage stage = (Stage) ((javafx.scene.Node) event.getSource()).getScene().getWindow();
        stage.close();
    }

    // Hàm hiển thị thông báo lỗi
    private void showError(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR, message, ButtonType.OK);
        alert.showAndWait();
    }

    // Hàm hiển thị thông báo thành công
    private void showInfo(String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION, message, ButtonType.OK);
        alert.showAndWait();
    }
}
