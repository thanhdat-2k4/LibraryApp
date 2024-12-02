package com.example.oop25;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.IOException;

public class DoiMatKhau {

    @FXML
    private PasswordField confirmpass;

    @FXML
    private PasswordField currentpass;

    @FXML
    private PasswordField newpass;

    @FXML
    private Label messageLabel; // Thông báo trạng thái

    @FXML
    void click_hientai(ActionEvent event) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("doimnatkhau.fxml"));
            Scene scene = new Scene(fxmlLoader.load());
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setTitle("Đổi mật khẩu");
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            messageLabel.setText("Lỗi: Không thể tải giao diện đổi mật khẩu.");
        }
    }

    @FXML
    public void click_quaylai(ActionEvent actionEvent) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("canhan.fxml"));
            Scene scene = new Scene(fxmlLoader.load(), 1000, 600);
            Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
            stage.setTitle("Cá Nhân");
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            messageLabel.setText("Lỗi: Không thể tải giao diện cá nhân.");
        }
    }

    @FXML
    public void click_xacnhan(ActionEvent actionEvent) {
        String currentPassInput = currentpass.getText().trim();
        String newPassInput = newpass.getText().trim();
        String confirmPassInput = confirmpass.getText().trim();

        if (currentPassInput.isEmpty() || newPassInput.isEmpty() || confirmPassInput.isEmpty()) {
            messageLabel.setText("Vui lòng nhập đầy đủ vào các ô.");
            hideMessageAfterDelay();
            return;
        }

        if (currentPassInput.length() != 8 || newPassInput.length() != 8 || confirmPassInput.length() != 8) {
            messageLabel.setText("Mật khẩu phải chứa đúng 8 ký tự.");
            hideMessageAfterDelay();
            return;
        }

        if (!currentPassInput.equals(PasswordStorage.getCurrentPassword())) {
            messageLabel.setText("Mật khẩu hiện tại không đúng.");
            hideMessageAfterDelay();
            return;
        }

        if (!newPassInput.equals(confirmPassInput)) {
            messageLabel.setText("Mật khẩu xác nhận không khớp.");
            hideMessageAfterDelay();
            return;
        }

        PasswordStorage.setCurrentPassword(newPassInput);
        messageLabel.setText("Đổi mật khẩu thành công! Mật khẩu mới đã được cập nhật.");
        hideMessageAfterDelay();
        currentpass.clear();
        newpass.clear();
        confirmpass.clear();
    }

    // Hàm ẩn messageLabel sau 10 giây
    private void hideMessageAfterDelay() {
        // Sử dụng Timeline để ẩn messageLabel sau 10 giây
        Timeline timeline = new Timeline(
                new KeyFrame(Duration.seconds(3), e -> messageLabel.setText(""))
        );
        timeline.setCycleCount(1); // Chạy một lần
        timeline.play(); // Chạy timeline
    }
}