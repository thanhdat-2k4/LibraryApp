////tra sach

package com.example.oop25;

import javafx.beans.property.*;
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
    private TableColumn<Book, String> ISBN;

    @FXML
    private TableColumn<Book, String> publisherColumn;

    @FXML
    private TableView<Book> returnBookTable;

    @FXML
    private TextField receiptIdField;

    @FXML
    private TextField readerIdField;

    @FXML
    private TableColumn<Book, Integer> availableCopiesColumn;

    @FXML
    private TableColumn<Book, Integer> borrowedCopiesColumn;

    @FXML
    private TextField readerNameField;

    @FXML
    private TableColumn<Book, String> bookTitleColumn;

    @FXML
    private TableColumn<Book, String> authorColumn;

    @FXML
    void onExitClick(MouseEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("quanlimuontra.fxml"));
        Scene scene = new Scene(loader.load());
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setScene(scene);
        stage.show();
    }

    @FXML
    void searchBookDetails(MouseEvent event) {
        String receiptId = receiptIdField.getText().trim();
        String readerId = readerIdField.getText().trim();
        String readerName = readerNameField.getText().trim();

        if (receiptId.isEmpty() && readerId.isEmpty() && readerName.isEmpty()) {
            showError("Vui lòng nhập ít nhất một trong các thông tin: Mã phiếu, Mã độc giả hoặc Tên độc giả.");
            return;
        }

        // Truy vấn SQL chỉ lấy sách có trạng thái "đang mượn"
        String sqlSearch = """
        SELECT lm.ma_phieu, lm.ISBN, lm.ngay_muon, lm.ngay_tra, lm.tinh_trang, 
               ts.ten_sach, ts.ten_tac_gia, ts.NXB, ts.so_luong_hien_con, ts.so_luong_muon,
               dg.ten_docgia, dg.madocgia
        FROM `lượt mượn` lm
        JOIN `thông tin sách` ts ON lm.ISBN = ts.ISBN
        JOIN `danh sách độc giả` dg ON lm.madocgia = dg.madocgia
        WHERE lm.tinh_trang = 'đang mượn'
            AND (? IS NULL OR lm.ma_phieu LIKE ?)
            AND (? IS NULL OR dg.madocgia LIKE ?)
            AND (? IS NULL OR dg.ten_docgia LIKE ?);
        """;

        ObservableList<Book> bookList = FXCollections.observableArrayList();

        try (Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/library", "root", "1234");
             PreparedStatement stmt = conn.prepareStatement(sqlSearch)) {

            stmt.setString(1, receiptId.isEmpty() ? null : "%" + receiptId + "%");
            stmt.setString(2, receiptId.isEmpty() ? null : "%" + receiptId + "%");
            stmt.setString(3, readerId.isEmpty() ? null : "%" + readerId + "%");
            stmt.setString(4, readerId.isEmpty() ? null : "%" + readerId + "%");
            stmt.setString(5, readerName.isEmpty() ? null : "%" + readerName + "%");
            stmt.setString(6, readerName.isEmpty() ? null : "%" + readerName + "%");

            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                Book book = new Book(
                        rs.getString("ISBN"),
                        rs.getString("ten_sach"),
                        rs.getString("ten_tac_gia"),
                        rs.getString("NXB"),
                        rs.getInt("so_luong_hien_con"),
                        rs.getInt("so_luong_muon")
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

                bookList.add(book);
            }

            if (bookList.isEmpty()) {
                showInfo("Không tìm thấy sách đang mượn.");
            } else {
                returnBookTable.setItems(bookList);
            }

        } catch (SQLException e) {
            showError("Lỗi cơ sở dữ liệu: " + e.getMessage());
        }
    }

    @FXML
    void onReturnBookClick(MouseEvent event) {
        Book selectedBook = returnBookTable.getSelectionModel().getSelectedItem();

        if (selectedBook == null) {
            showError("Vui lòng chọn một sách để trả.");
            return;
        }

        String isbn = selectedBook.getBookId();
        String receiptId = receiptIdField.getText().trim();

        try (Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/library", "root", "1234")) {
            // Kiểm tra trạng thái mượn (chỉ lấy những sách có trạng thái "đang mượn")
            String sqlCheckReceipt = "SELECT ma_phieu, tinh_trang FROM `lượt mượn` WHERE ISBN = ? AND tinh_trang = 'đang mượn'";
            try (PreparedStatement stmtCheck = conn.prepareStatement(sqlCheckReceipt)) {
                stmtCheck.setString(1, isbn);
                ResultSet rs = stmtCheck.executeQuery();

                if (rs.next()) {
                    // Nếu sách đang mượn, cập nhật trạng thái thành "đã trả"
                    String sqlUpdateStatus = "UPDATE `lượt mượn` SET tinh_trang = 'đã trả' WHERE ISBN = ? AND ma_phieu = ?";
                    try (PreparedStatement stmtUpdateStatus = conn.prepareStatement(sqlUpdateStatus)) {
                        stmtUpdateStatus.setString(1, isbn);
                        stmtUpdateStatus.setString(2, rs.getString("ma_phieu"));
                        stmtUpdateStatus.executeUpdate();

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

                        showInfo("Trả sách thành công!");
                        searchBookDetails(event); // Tải lại bảng sau khi trả sách
                    }
                } else {
                    showError("Sách không còn trạng thái 'đang mượn'.");
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

    @FXML
    private void initialize() {
        // Thiết lập các cột trong bảng
        bookTitleColumn.setCellValueFactory(new PropertyValueFactory<>("bookTitle"));
        authorColumn.setCellValueFactory(new PropertyValueFactory<>("authorName"));
        publisherColumn.setCellValueFactory(new PropertyValueFactory<>("publisher"));
        ISBN.setCellValueFactory(new PropertyValueFactory<>("bookId"));
        availableCopiesColumn.setCellValueFactory(new PropertyValueFactory<>("availableCopies"));
        borrowedCopiesColumn.setCellValueFactory(new PropertyValueFactory<>("borrowedCopies"));
    }
}
