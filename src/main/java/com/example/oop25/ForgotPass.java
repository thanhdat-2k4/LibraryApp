package com.example.oop25;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.util.Random;

public class ForgotPass {

    private final String DB_URL = "jdbc:mysql://localhost:3306/library";
    private final String DB_USER = "root";
    private final String DB_PASSWORD = "1234";

    private String generatedOTP; // OTP được tạo
    public static String userEmail;    // Email người dùng nhập vào

    @FXML
    private Label emailcheck;

    @FXML
    private TextField otpField;

    @FXML
    private TextField otpField1;

    @FXML
    void click_checkemail(MouseEvent event) {
        userEmail = otpField1.getText();

        if (userEmail.isEmpty()) {
            showAlert("Lỗi", "Vui lòng nhập email của bạn.");
            return;
        }

        if (checkEmailExists(userEmail)) {
            generatedOTP = generateOTP();
            sendEmail(userEmail, generatedOTP);
            showAlert("Thành công", "OTP đã được gửi tới email của bạn.");
        } else {
            showAlert("Thất bại", "Email không tồn tại trong hệ thống.");
        }
    }

    @FXML
    void click_checkOTP(MouseEvent event) {
        String enteredOTP = otpField.getText();

        if (enteredOTP.equals(generatedOTP)) {
            try {
                javafx.fxml.FXMLLoader loader = new javafx.fxml.FXMLLoader(getClass().getResource("ResetPass.fxml"));
                javafx.scene.Scene scene = new javafx.scene.Scene(loader.load());
                Stage stage = (Stage) otpField.getScene().getWindow();
                stage.setScene(scene);
            } catch (Exception e) {
                showAlert("Lỗi", "Không thể mở giao diện đặt lại mật khẩu.");
            }
        } else {
            showAlert("Sai OTP", "OTP không đúng. Vui lòng kiểm tra lại.");
        }
    }

    @FXML
    void click_returnLogin(MouseEvent event) {
        try {
            javafx.fxml.FXMLLoader loader = new javafx.fxml.FXMLLoader(getClass().getResource("Login.fxml"));
            javafx.scene.Scene scene = new javafx.scene.Scene(loader.load());
            Stage stage = (Stage) otpField.getScene().getWindow();
            stage.setScene(scene);
        } catch (Exception e) {
            showAlert("Lỗi", "Không thể quay lại màn hình đăng nhập.");
        }
    }

    private boolean checkEmailExists(String email) {
        String query = "SELECT * FROM `nhân viên` WHERE email = ?";
        try (Connection connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {

            preparedStatement.setString(1, email);
            return preparedStatement.executeQuery().next(); // Trả về true nếu email tồn tại
        } catch (Exception e) {
            e.printStackTrace();
            showAlert("Lỗi kết nối", "Không thể kết nối tới cơ sở dữ liệu.");
        }
        return false;
    }

    private String generateOTP() {
        Random random = new Random();
        int otp = 100000 + random.nextInt(900000); // Tạo OTP 6 chữ số
        return String.valueOf(otp);
    }

    private void sendEmail(String toEmail, String otp) {
        EmailSender emailSender = new EmailSender();
        String subject = "Mã OTP khôi phục mật khẩu";
        String body = "Xin chào,\n\nMã OTP của bạn là: " + otp +
                "\n\nVui lòng không chia sẻ mã này với bất kỳ ai.\n\nTrân trọng,\nQuản lý thư viện";
        emailSender.sendEmail(toEmail, subject, body);
    }


    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
