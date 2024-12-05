//tra sach

//package com.example.oop25;
//
//import javafx.collections.FXCollections;
//import javafx.collections.ObservableList;
//import javafx.fxml.FXML;
//import javafx.fxml.FXMLLoader;
//import javafx.scene.Node;
//import javafx.scene.Scene;
//import javafx.scene.control.*;
//import javafx.scene.control.cell.PropertyValueFactory;
//import javafx.scene.input.MouseEvent;
//import javafx.stage.Stage;
//
//import java.io.IOException;
//import java.sql.*;
//
//public class returnBook {
//
//    @FXML
//    private TableColumn<?, ?> ISBN;
//
//    @FXML
//    private TableColumn<?, ?> NXB;
//
//    @FXML
//    private TableView<Sach> bang_tra_sach;
//
//    @FXML
//    private TextField ma_phieu;
//
//    @FXML
//    private TextField madocgia;
//
//    @FXML
//    private DatePicker ngay_muon;
//
//    @FXML
//    private DatePicker ngay_tra;
//
//    @FXML
//    private TableColumn<?, ?> so_luong_hien_con;
//
//    @FXML
//    private TableColumn<?, ?> so_luong_muon;
//
//    @FXML
//    private TextField ten_docgia;
//
//    @FXML
//    private TableColumn<?, ?> ten_sach;
//
//    @FXML
//    private TableColumn<?, ?> ten_tac_gia;
//
//    @FXML
//    void click_thoat(MouseEvent event) throws IOException {
//        FXMLLoader loader = new FXMLLoader(getClass().getResource("QuanLiMuonTra.fxml"));
//        Scene scene = new Scene(loader.load());
//        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
//        stage.setScene(scene);
//        stage.show();
//    }
//
//    @FXML
//    void click_tra(MouseEvent event) {
//        String maPhieu = ma_phieu.getText().trim();
//
//        if (maPhieu.isEmpty()) {
//            showError("Vui lòng nhập mã phiếu.");
//            return;
//        }
//
//        try (Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/library", "root", "1234")) {
//            String sqlCheckPhieu = "SELECT ISBN FROM `lượt mượn` WHERE ma_phieu = ?";
//            try (PreparedStatement stmtCheck = conn.prepareStatement(sqlCheckPhieu)) {
//                stmtCheck.setString(1, maPhieu);
//                ResultSet rs = stmtCheck.executeQuery();
//
//                if (rs.next()) {
//                    String isbn = rs.getString("ISBN");
//
//                    String sqlSach = "SELECT * FROM `thông tin sách` WHERE ISBN = ?";
//                    try (PreparedStatement stmtSach = conn.prepareStatement(sqlSach)) {
//                        stmtSach.setString(1, isbn);
//                        ResultSet rsSach = stmtSach.executeQuery();
//
//                        if (rsSach.next()) {
//                            displaySachInfo(rsSach);
//                        } else {
//                            showError("Không tìm thấy sách với mã ISBN: " + isbn);
//                        }
//                    }
//                } else {
//                    showError("Mã phiếu không hợp lệ.");
//                }
//            }
//        } catch (SQLException e) {
//            showError("Lỗi cơ sở dữ liệu: " + e.getMessage());
//        }
//    }
//
//    @FXML
//    private void displaySachInfo(ResultSet rsSach) throws SQLException {
//        Sach sach = new Sach(
//                rsSach.getString("ISBN"),
//                rsSach.getString("ten_sach"),
//                rsSach.getString("ten_tac_gia"),
//                rsSach.getString("NXB"),
//                rsSach.getInt("so_luong_hien_con"),
//                rsSach.getInt("so_luong_muon")
//        );
//
//        ObservableList<Sach> sachList = FXCollections.observableArrayList();
//        sachList.add(sach);
//
//        ten_sach.setCellValueFactory(new PropertyValueFactory<>("tenSach"));
//        ten_tac_gia.setCellValueFactory(new PropertyValueFactory<>("tenTacGia"));
//        NXB.setCellValueFactory(new PropertyValueFactory<>("nxb"));
//        ISBN.setCellValueFactory(new PropertyValueFactory<>("isbn"));
//        so_luong_hien_con.setCellValueFactory(new PropertyValueFactory<>("soLuongHienCon"));
//        so_luong_muon.setCellValueFactory(new PropertyValueFactory<>("soLuongMuon"));
//
//        bang_tra_sach.setItems(sachList);
//    }
//
//
//    @FXML
//    void click_trasach(MouseEvent event) {
//        String maPhieu = ma_phieu.getText().trim();
//
//        if (maPhieu.isEmpty()) {
//            showError("Vui lòng nhập mã phiếu.");
//            return;
//        }
//
//        try (Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/library", "root", "1234")) {
//            // Kiểm tra mã phiếu và trạng thái hiện tại
//            String sqlCheckPhieu = "SELECT ISBN FROM `lượt mượn` WHERE ma_phieu = ? AND tinh_trang = 'đang mượn'";
//            try (PreparedStatement stmtCheck = conn.prepareStatement(sqlCheckPhieu)) {
//                stmtCheck.setString(1, maPhieu);
//                ResultSet rs = stmtCheck.executeQuery();
//
//                if (rs.next()) {
//                    String isbn = rs.getString("ISBN");
//
//                    // Cập nhật trạng thái phiếu mượn
//                    String sqlUpdateTinhTrang = "UPDATE `lượt mượn` SET tinh_trang = 'đã trả' WHERE ma_phieu = ?";
//                    try (PreparedStatement stmtUpdate = conn.prepareStatement(sqlUpdateTinhTrang)) {
//                        stmtUpdate.setString(1, maPhieu);
//                        stmtUpdate.executeUpdate();
//
//                        // Cập nhật thông tin sách
//                        String sqlUpdateSach = """
//                    UPDATE `thông tin sách`
//                    SET so_luong_hien_con = so_luong_hien_con + 1,
//                        so_luong_muon = so_luong_muon - 1
//                    WHERE ISBN = ?;
//                    """;
//                        try (PreparedStatement stmtUpdateSach = conn.prepareStatement(sqlUpdateSach)) {
//                            stmtUpdateSach.setString(1, isbn);
//                            stmtUpdateSach.executeUpdate();
//                        }
//
//                        showInfo("Trả sách thành công! Trạng thái đã được cập nhật thành 'đã trả'.");
//                    }
//                } else {
//                    showError("Không tìm thấy phiếu mượn với trạng thái 'đang mượn'. Vui lòng kiểm tra lại.");
//                }
//            }
//        } catch (SQLException e) {
//            showError("Lỗi cơ sở dữ liệu: " + e.getMessage());
//        }
//    }
//
//
//    private void showError(String message) {
//        Alert alert = new Alert(Alert.AlertType.ERROR);
//        alert.setTitle("Lỗi");
//        alert.setContentText(message);
//        alert.showAndWait();
//    }
//
//    private void showInfo(String message) {
//        Alert alert = new Alert(Alert.AlertType.INFORMATION);
//        alert.setTitle("Thông báo");
//        alert.setContentText(message);
//        alert.showAndWait();
//    }
//}
package com.example.oop25;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.*;

public class ReturnBookController {

    @FXML
    private TableColumn<?, ?> ISBN;

    @FXML
    private TableColumn<?, ?> publisherColumn;

    @FXML
    private TableView<Book> returnBookTable;

    @FXML
    private TextField receiptIdField;

    @FXML
    private TextField readerIdField;

    @FXML
    private DatePicker borrowDateField;

    @FXML
    private DatePicker returnDateField;

    @FXML
    private TableColumn<?, ?> availableCopiesColumn;

    @FXML
    private TableColumn<?, ?> borrowedCopiesColumn;

    @FXML
    private TextField readerNameField;

    @FXML
    private TableColumn<?, ?> bookTitleColumn;

    @FXML
    private TableColumn<?, ?> authorColumn;

    @FXML
    void onExitClick(MouseEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("ManageBookLoan.fxml"));
        Scene scene = new Scene(loader.load());
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setScene(scene);
        stage.show();
    }

    @FXML
    void onReturnClick(MouseEvent event) {
        String receiptId = receiptIdField.getText().trim();

        if (receiptId.isEmpty()) {
            showError("Vui lòng nhập mã phiếu.");
            return;
        }

        try (Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/library", "root", "1234")) {
            String sqlCheckReceipt = "SELECT ISBN FROM `lượt mượn` WHERE ma_phieu = ?";
            try (PreparedStatement stmtCheck = conn.prepareStatement(sqlCheckReceipt)) {
                stmtCheck.setString(1, receiptId);
                ResultSet rs = stmtCheck.executeQuery();

                if (rs.next()) {
                    String isbn = rs.getString("ISBN");

                    String sqlBook = "SELECT * FROM `thông tin sách` WHERE ISBN = ?";
                    try (PreparedStatement stmtBook = conn.prepareStatement(sqlBook)) {
                        stmtBook.setString(1, isbn);
                        ResultSet rsBook = stmtBook.executeQuery();

                        if (rsBook.next()) {
                            displayBookInfo(rsBook);
                        } else {
                            showError("Không tìm thấy sách với mã ISBN: " + isbn);
                        }
                    }
                } else {
                    showError("Mã phiếu không hợp lệ.");
                }
            }
        } catch (SQLException e) {
            showError("Lỗi cơ sở dữ liệu: " + e.getMessage());
        }
    }

    @FXML
    private void displayBookInfo(ResultSet rsBook) throws SQLException {
        Book book = new Book(
                rsBook.getString("ISBN"),
                rsBook.getString("ten_sach"),
                rsBook.getString("ten_tac_gia"),
                rsBook.getString("NXB"),
                rsBook.getInt("so_luong_hien_con"),
                rsBook.getInt("so_luong_muon")
        ) {
            @Override
            public int getAvailableQuantity() {
                return 0;
            }

            @Override
            public int getBorrowedQuantity() {
                return 0;
            }

            @Override
            public void setAvailableQuantity(int availableQuantity) {

            }
        };

        ObservableList<Book> bookList = FXCollections.observableArrayList();
        bookList.add(book);

        bookTitleColumn.setCellValueFactory(new PropertyValueFactory<>("bookTitle"));
        authorColumn.setCellValueFactory(new PropertyValueFactory<>("author"));
        publisherColumn.setCellValueFactory(new PropertyValueFactory<>("publisher"));
        ISBN.setCellValueFactory(new PropertyValueFactory<>("isbn"));
        availableCopiesColumn.setCellValueFactory(new PropertyValueFactory<>("availableCopies"));
        borrowedCopiesColumn.setCellValueFactory(new PropertyValueFactory<>("borrowedCopies"));

        returnBookTable.setItems(bookList);
    }

    @FXML
    void onReturnBookClick(MouseEvent event) {
        String receiptId = receiptIdField.getText().trim();

        if (receiptId.isEmpty()) {
            showError("Vui lòng nhập mã phiếu.");
            return;
        }

        try (Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/library", "root", "1234")) {
            // Kiểm tra mã phiếu và trạng thái hiện tại
            String sqlCheckReceipt = "SELECT ISBN FROM `lượt mượn` WHERE ma_phieu = ? AND tinh_trang = 'đang mượn'";
            try (PreparedStatement stmtCheck = conn.prepareStatement(sqlCheckReceipt)) {
                stmtCheck.setString(1, receiptId);
                ResultSet rs = stmtCheck.executeQuery();

                if (rs.next()) {
                    String isbn = rs.getString("ISBN");

                    // Cập nhật trạng thái phiếu mượn
                    String sqlUpdateStatus = "UPDATE `lượt mượn` SET tinh_trang = 'đã trả' WHERE ma_phieu = ?";
                    try (PreparedStatement stmtUpdate = conn.prepareStatement(sqlUpdateStatus)) {
                        stmtUpdate.setString(1, receiptId);
                        stmtUpdate.executeUpdate();

                        // Cập nhật thông tin sách
                        String sqlUpdateBook = """
                    UPDATE `thông tin sách`
                    SET so_luong_hien_con = so_luong_hien_con + 1,
                        so_luong_muon = so_luong_muon - 1
                    WHERE ISBN = ?;
                    """;
                        try (PreparedStatement stmtUpdateBook = conn.prepareStatement(sqlUpdateBook)) {
                            stmtUpdateBook.setString(1, isbn);
                            stmtUpdateBook.executeUpdate();
                        }

                        showInfo("Trả sách thành công! Trạng thái đã được cập nhật thành 'đã trả'.");
                    }
                } else {
                    showError("Không tìm thấy phiếu mượn với trạng thái 'đang mượn'. Vui lòng kiểm tra lại.");
                }
            }
        } catch (SQLException e) {
            showError("Lỗi cơ sở dữ liệu: " + e.getMessage());
        }
    }

    private void showError(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Lỗi");
        alert.setContentText(message);
        alert.showAndWait();
    }

    private void showInfo(String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Thông báo");
        alert.setContentText(message);
        alert.showAndWait();
    }
}
