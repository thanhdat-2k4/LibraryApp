// phieu sua thong tin doc gia
package com.example.oop25;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

import java.io.IOException;

public class ReaderEditForm {

    @FXML
    private TextField SDT;

    @FXML
    private TextField diachi;

    @FXML
    private Button huy;

    @FXML
    private Button luu;

    @FXML
    private TextField mathe;

    @FXML
    private TextField tendocgia;

    @FXML
    void click_huy(MouseEvent event) throws IOException {
        // Nếu tất cả hợp lệ, mở trang kế tiếp
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("trangchuquanlidocgia.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        Stage stage = new Stage();
        stage.setTitle("trang tìm kiếm !");
        stage.setScene(scene);
        stage.show();

        // Thoát trang hiện tại
        ((Node) (event.getSource())).getScene().getWindow().hide();
    }

    @FXML
    void click_luu(MouseEvent event) throws IOException {
        String maThe = mathe.getText().trim();
        String sdt = SDT.getText().trim();
        String tenDocGia = tendocgia.getText().trim();
        String diaChi = diachi.getText().trim();

        // Kiểm tra các trường hợp lỗi

        // 1. Kiểm tra mã độc giả (phải là chuỗi không rỗng và không chứa ký tự đặc biệt)
        if (maThe.isEmpty() || !maThe.matches("\\w+")) {
            showError("Mã độc giả không hợp lệ! Vui lòng kiểm tra lại.");
            return;
        }

        // 2. Kiểm tra SDT phải là số và có đúng 10 ký tự
        if (!sdt.matches("\\d{10}")) {
            showError("Số điện thoại phải gồm đúng 10 chữ số!");
            return;
        }

        // 3. Kiểm tra tên độc giả hợp lệ (không chứa số hoặc ký tự đặc biệt)
        if (!tenDocGia.matches("[\\p{L} ]+")) { // Cho phép chữ cái và khoảng trắng
            showError("Tên độc giả không hợp lệ! Tên không được chứa số hoặc ký tự đặc biệt.");
            return;
        }

        // 4. Kiểm tra địa chỉ (không để trống)
        if (diaChi.isEmpty()) {
            showError("Địa chỉ không được để trống!");
            return;
        }

        showSuccess("Lưu thông tin thành công!");



        // Nếu tất cả hợp lệ, mở trang kế tiếp
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("giaodientimkiemdocgia.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        Stage stage = new Stage();
        stage.setTitle("trang tìm kiếm !");
        stage.setScene(scene);
        stage.show();

        // Thoát trang hiện tại
        ((Node) (event.getSource())).getScene().getWindow().hide();


    }


    private void showError(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Lỗi");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    private void showSuccess(String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Thông báo");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}

