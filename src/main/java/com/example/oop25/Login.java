package com.example.oop25;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class Login {

    private final String DB_URL = "jdbc:mysql://localhost:3306/library";
    private final String DB_USER = "root";
    private final String DB_PASSWORD = "1234";

    @FXML
    private TextField email;

    @FXML
    private PasswordField password;

    public static String loggedInUserEmail;


    @FXML
    void click_login(MouseEvent event) {
        String emailInput = email.getText();
        String passwordInput = password.getText();
        loggedInUserEmail =  email.getText();

        if (validateLogin(emailInput, passwordInput)) {
            // Chuyển sang Quanlysach.fxml
            try {
                javafx.fxml.FXMLLoader loader = new javafx.fxml.FXMLLoader(getClass().getResource("Trangchu.fxml"));
                javafx.scene.Scene scene = new javafx.scene.Scene(loader.load());
                Stage stage = (Stage) email.getScene().getWindow();
                stage.setScene(scene);
            } catch (Exception e) {
                showAlert("Lỗi", "Không thể mở giao diện quản lý sách.");
            }
        } else {
            // Hiển thị thông báo sai thông tin đăng nhập
            showAlert("Đăng nhập thất bại", "Email hoặc mật khẩu không đúng.");
        }
    }

    @FXML
    void click_forgotpass(MouseEvent event) {
        // Chuyển sang Forgotpass.fxml
        try {
            javafx.fxml.FXMLLoader loader = new javafx.fxml.FXMLLoader(getClass().getResource("ForgotPass.fxml"));
            javafx.scene.Scene scene = new javafx.scene.Scene(loader.load());
            Stage stage = (Stage) email.getScene().getWindow();
            stage.setScene(scene);
        } catch (Exception e) {
            showAlert("Lỗi", "Không thể mở giao diện quên mật khẩu.");
        }
    }

    private boolean validateLogin(String email, String password) {
        String query = "SELECT * FROM `nhân viên` WHERE email = ? AND password = ?";
        try (Connection connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {

            preparedStatement.setString(1, email);
            preparedStatement.setString(2, password);

            ResultSet resultSet = preparedStatement.executeQuery();
            return resultSet.next(); // Nếu có kết quả trả về, đăng nhập thành công
        } catch (Exception e) {
            e.printStackTrace();
            showAlert("Lỗi kết nối", "Không thể kết nối tới cơ sở dữ liệu.");
        }
        return false;
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
