package com.example.oop25;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.stage.Stage;
import javafx.event.ActionEvent;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class DoiMatKhau {
    @FXML
    private PasswordField currentpass;
    @FXML
    private PasswordField newpass;
    @FXML
    private PasswordField confirmpass;
    @FXML
    private Label messageLabel;

    public class DatabaseHelper {
        private static final String URL = "jdbc:mysql://localhost:3306/PasswordDB";
        private static final String USER = "root";
        private static final String PASSWORD = "123456789";

        public static Connection getConnection() throws SQLException {
            return DriverManager.getConnection(URL, USER, PASSWORD);
        }

        public static String getCurrentPassword() throws SQLException {
            String query = "SELECT new_password FROM passwords ORDER BY updated_at DESC LIMIT 1";
            try (Connection connection = getConnection();
                 PreparedStatement statement = connection.prepareStatement(query);
                 ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    return resultSet.getString("new_password");
                }
            }
            return null;
        }

        public static void updatePassword(String oldPassword, String newPassword) throws SQLException {
            String query = "INSERT INTO passwords (old_password, new_password) VALUES (?, ?)";
            try (Connection connection = getConnection();
                 PreparedStatement statement = connection.prepareStatement(query)) {
                statement.setString(1, oldPassword);
                statement.setString(2, newPassword);
                statement.executeUpdate();
            }
        }
    }

    @FXML
    private void click_xacnhan() {
        try {
            String currentPasswordInput = currentpass.getText();
            String newPasswordInput = newpass.getText();
            String confirmPasswordInput = confirmpass.getText();

            if (currentPasswordInput.isEmpty() || newPasswordInput.isEmpty() || confirmPasswordInput.isEmpty()) {
                messageLabel.setText("Vui lòng nhập đầy đủ vào các ô!");
                return;
            }

            if (currentPasswordInput.length() < 8) {
                messageLabel.setText("Mật khẩu hiện tại phải có ít nhất 8 ký tự!");
                return;
            }

            if (newPasswordInput.length() < 8) {
                messageLabel.setText("Mật khẩu mới phải có ít nhất 8 ký tự!");
                return;
            }

            if (confirmPasswordInput.length() < 8) {
                messageLabel.setText("Mật khẩu xác nhận phải có ít nhất 8 ký tự!");
                return;
            }

            // Lấy mật khẩu hiện tại từ database
            String currentPasswordInDB = DatabaseHelper.getCurrentPassword();

            // Kiểm tra mật khẩu hiện tại
            if (!currentPasswordInput.equals(currentPasswordInDB)) {
                messageLabel.setText("Mật khẩu hiện tại sai, vui lòng nhập lại!");
                return;
            }

            // Kiểm tra mật khẩu xác nhận
            if (!newPasswordInput.equals(confirmPasswordInput)) {
                messageLabel.setText("Mật khẩu xác nhận sai, vui lòng nhập lại!");
                return;
            }

            // Cập nhật mật khẩu trong database
            DatabaseHelper.updatePassword(currentPasswordInput, newPasswordInput);
            messageLabel.setStyle("-fx-text-fill: green;");
            messageLabel.setText("Đổi mật khẩu thành công!");

        } catch (Exception e) {
            e.printStackTrace();
            messageLabel.setText("Đã xảy ra lỗi, vui lòng thử lại!");
        }
    }

    @FXML
    private void click_quaylai(ActionEvent actionEvent) { // Nhận tham số ActionEvent
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("canhan.fxml"));
            Scene scene = new Scene(fxmlLoader.load(), 1000, 600);
            Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
            stage.setTitle("Cá Nhân");
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}