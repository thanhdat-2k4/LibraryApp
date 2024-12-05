package com.example.oop25;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.PasswordField;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;

public class ResetPass {

    // Kết nối cơ sở dữ liệu
    private final String DB_URL = "jdbc:mysql://localhost:3306/library";
    private final String DB_USER = "root";
    private final String DB_PASSWORD = "1234";

    @FXML
    private PasswordField newPasswordField;

    @FXML
    private PasswordField confirmPasswordField;

    @FXML
    void click_resetPass(MouseEvent event) {
        // Lấy mật khẩu từ giao diện
        String newPassword = newPasswordField.getText();
        String confirmPassword = confirmPasswordField.getText();

        // Kiểm tra xem các trường đã được điền chưa
        if (newPassword.isEmpty() || confirmPassword.isEmpty()) {
            showAlert("Lỗi", "Vui lòng nhập đầy đủ thông tin.");
            return;
        }

        // Kiểm tra xem mật khẩu nhập lại có khớp không
        if (!newPassword.equals(confirmPassword)) {
            showAlert("Lỗi", "Mật khẩu không khớp. Vui lòng thử lại.");
            return;
        }

        // Cập nhật mật khẩu trong cơ sở dữ liệu
        try (Connection connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD)) {
            String query = "UPDATE `nhân viên` SET password = ? WHERE email = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(query);

            // Lấy email đã xác minh từ lớp ForgotPass (cần truyền thông tin email)
            String verifiedEmail = ForgotPass.userEmail; // Giả sử userEmail là biến tĩnh trong ForgotPass

            preparedStatement.setString(1, newPassword); // Mật khẩu mới
            preparedStatement.setString(2, verifiedEmail); // Email của tài khoản

            int rowsAffected = preparedStatement.executeUpdate();
            if (rowsAffected > 0) {
                showAlert("Thành công", "Mật khẩu đã được cập nhật.");
                click_returnLogin(event); // Quay lại màn hình đăng nhập
            } else {
                showAlert("Lỗi", "Không thể cập nhật mật khẩu. Vui lòng thử lại.");
            }
        } catch (Exception e) {
            e.printStackTrace();
            showAlert("Lỗi", "Có lỗi xảy ra khi cập nhật mật khẩu.");
        }
    }

    @FXML
    void click_returnLogin(MouseEvent event) {
        try {
            javafx.fxml.FXMLLoader loader = new javafx.fxml.FXMLLoader(getClass().getResource("Login.fxml"));
            javafx.scene.Scene scene = new javafx.scene.Scene(loader.load());
            Stage stage = (Stage) newPasswordField.getScene().getWindow();
            stage.setScene(scene);
        } catch (Exception e) {
            showAlert("Lỗi", "Không thể quay lại màn hình đăng nhập.");
        }
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
