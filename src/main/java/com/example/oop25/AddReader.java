//// them doc gia
//package com.example.oop25;
//
//import javafx.fxml.FXML;
//import javafx.fxml.FXMLLoader;
//import javafx.scene.Node;
//import javafx.scene.Scene;
//import javafx.scene.control.Alert;
//import javafx.scene.control.DatePicker;
//import javafx.scene.control.TextField;
//import javafx.scene.input.MouseEvent;
//import javafx.stage.Stage;
//
//import java.io.IOException;
//import java.sql.*;
//import java.time.LocalDate;
//
//public class AddReader {
//
//    @FXML
//    private TextField CCCD;
//
//    @FXML
//    private TextField SDT;
//
//    @FXML
//    private TextField dia_chi;
//
//    @FXML
//    private TextField matdocgia;
//
//    @FXML
//    private DatePicker ngay_het_han;
//
//    @FXML
//    private DatePicker ngay_sinh;
//
//    @FXML
//    private TextField ten_docgia;
//
//    @FXML
//    void click_huy(MouseEvent event) throws IOException {
//        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("trangchuquanlidocgia.fxml"));
//        Scene scene = new Scene(fxmlLoader.load());
//
//        // Khởi tạo Stage mới
//        Stage stage = new Stage();
//        stage.setTitle("Quản lý độc giả");
//        stage.setScene(scene);
//        stage.show();
//
//        // Đóng cửa sổ hiện tại
//        ((Node) (event.getSource())).getScene().getWindow().hide();
//    }
//
//    @FXML
//    void click_them(MouseEvent event) throws IOException {
//        // Lấy thông tin từ các trường nhập liệu
//        String madocgia = matdocgia.getText().trim();
//        String tenDocGia = ten_docgia.getText().trim();
//        String diaChi = dia_chi.getText().trim();
//        String sdt = SDT.getText().trim();
//        String cccd = CCCD.getText().trim();
//        String ngayGiaHan = LocalDate.now().toString(); // Ngày gia hạn là ngày hôm nay
//        String ngayHetHan = ngay_het_han.getValue() != null ? ngay_het_han.getValue().toString() : null;
//
//        // Kiểm tra dữ liệu hợp lệ
//        if (!sdt.matches("\\d{10}")) {
//            showAlert("Lỗi", "Số điện thoại không hợp lệ", "Số điện thoại phải có đúng 10 chữ số.");
//            return;
//        }
//
//        if (!cccd.matches("\\d{12}")) {
//            showAlert("Lỗi", "CCCD không hợp lệ", "CCCD phải có đúng 12 chữ số.");
//            return;
//        }
//
//        if (ngayHetHan == null) {
//            showAlert("Lỗi", "Ngày không hợp lệ", "Vui lòng chọn ngày hết hạn.");
//            return;
//        }
//
//        if (ngayGiaHan.compareTo(ngayHetHan) >= 0) {
//            showAlert("Lỗi", "Ngày không hợp lệ", "Ngày hết hạn phải sau ngày gia hạn.");
//            return;
//        }
//
//        // Kiểm tra mã độc giả không trùng trong cơ sở dữ liệu và thêm mới
//        try (Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/library", "root", "1234")) {
//            System.out.println("Kết nối thành công tới cơ sở dữ liệu!");
//
//            // Kiểm tra mã độc giả
//            String checkSQL = "SELECT COUNT(*) FROM `danh sách độc giả` WHERE madocgia = ?";
//            try (PreparedStatement checkStmt = connection.prepareStatement(checkSQL)) {
//                checkStmt.setString(1, madocgia);
//                ResultSet resultSet = checkStmt.executeQuery();
//                if (resultSet.next() && resultSet.getInt(1) > 0) {
//                    showAlert("Lỗi", "Mã độc giả bị trùng", "Mã độc giả này đã tồn tại trong hệ thống.");
//                    return;
//                }
//            }
//
//            // Thêm độc giả mới
//            String insertSQL = "INSERT INTO `danh sách độc giả` (madocgia, ten_docgia, thong_tin, dia_chi, ngay_giahan, ngay_hethan) VALUES (?, ?, ?, ?, ?, ?)";
//            try (PreparedStatement insertStmt = connection.prepareStatement(insertSQL)) {
//                insertStmt.setString(1, madocgia);
//                insertStmt.setString(2, tenDocGia);
//                insertStmt.setString(3, sdt); // "thong_tin" là số điện thoại
//                insertStmt.setString(4, diaChi);
//                insertStmt.setString(5, ngayGiaHan); // Ngày gia hạn là hôm nay
//                insertStmt.setString(6, ngayHetHan);
//
//                int rowsInserted = insertStmt.executeUpdate();
//                if (rowsInserted > 0) {
//                    showAlert("Thông báo", "Thành công", "Đã thêm độc giả mới thành công.");
//                } else {
//                    showAlert("Lỗi", "Không thể thêm", "Có lỗi xảy ra khi thêm độc giả.");
//                }
//                System.out.println("SQL thực thi: " + insertSQL);
//            }
//        } catch (SQLException e) {
//            e.printStackTrace();
//            showAlert("Lỗi", "Lỗi cơ sở dữ liệu", "Không thể kết nối hoặc thao tác với cơ sở dữ liệu.");
//            return;
//        }
//
//        // Chuyển về giao diện danh sách độc giả
//        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("danhsachkhithemdocgia.fxml"));
//        Scene scene = new Scene(fxmlLoader.load());
//
//        Stage stage = new Stage();
//        stage.setTitle("Danh sách độc giả");
//        stage.setScene(scene);
//        stage.show();
//
//        ((Node) (event.getSource())).getScene().getWindow().hide();
//    }
//
//    public static void showAlert(String title, String header, String content) {
//        Alert alert = new Alert(Alert.AlertType.INFORMATION);
//        alert.setTitle(title);
//        alert.setHeaderText(header);
//        alert.setContentText(content);
//        alert.showAndWait();
//    }
//}
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
