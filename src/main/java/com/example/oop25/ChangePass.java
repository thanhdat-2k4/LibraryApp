package com.example.oop25;

import javafx.fxml.FXML;
import javafx.scene.control.PasswordField;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;

public class ChangePass {

    @FXML
    private PasswordField newPasswordField;

    @FXML
    private PasswordField confirmPasswordField;

    @FXML
    void changePassword(MouseEvent event) {
        // Lấy email từ lớp Login
        String email = Login.loggedInUserEmail;

        if (email == null || email.isEmpty()) {
            showError("Không tìm thấy email người dùng. Vui lòng đăng nhập lại.");
            return;
        }

        // Lấy mật khẩu mới và xác nhận mật khẩu từ các trường nhập liệu
        String newPassword = newPasswordField.getText();
        String confirmPassword = confirmPasswordField.getText();

        if (newPassword.isEmpty() || confirmPassword.isEmpty()) {
            showError("Vui lòng điền đầy đủ thông tin.");
            return;
        }

        if (!newPassword.equals(confirmPassword)) {
            showError("Mật khẩu mới và mật khẩu xác nhận không khớp.");
            return;
        }

        try {
            // Kết nối đến cơ sở dữ liệu
            Connection connection = DriverManager.getConnection(
                    "jdbc:mysql://localhost:3306/library", "root", "1234");

            // Câu lệnh SQL để cập nhật mật khẩu
            String sql = "UPDATE `nhân viên` SET `password` = ? WHERE email = ?";
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setString(1, newPassword);
            statement.setString(2, email);

            int rowsUpdated = statement.executeUpdate();
            if (rowsUpdated > 0) {
                showInfo("Cập nhật mật khẩu thành công!");
            } else {
                showError("Không tìm thấy người dùng với email này.");
            }

            // Đóng kết nối
            statement.close();
            connection.close();
        } catch (Exception e) {
            e.printStackTrace();
            showError("Đã xảy ra lỗi khi cập nhật mật khẩu.");
        }
    }

    @FXML
    void exitChangePassword(MouseEvent event) {
        try {
            // Quay lại giao diện Personal.fxml
            Stage stage = (Stage) ((javafx.scene.Node) event.getSource()).getScene().getWindow();
            javafx.fxml.FXMLLoader loader = new javafx.fxml.FXMLLoader(getClass().getResource("Personal.fxml"));
            stage.getScene().setRoot(loader.load());
        } catch (Exception e) {
            e.printStackTrace();
            showError("Không thể quay lại giao diện Personal.");
        }
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
