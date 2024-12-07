//// muon sach

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
import java.time.LocalDate;

public class BorrowBook {

    @FXML
    private TableView<Book> bookTable;

    @FXML
    private TableColumn<Book, Boolean> selectColumn;

    @FXML
    private TableColumn<Book, String> titleColumn, authorColumn, publisherColumn, isbnColumn;

    @FXML
    private TableColumn<Book, Integer> availableQuantityColumn, borrowedQuantityColumn;

    @FXML
    private TextField readerIdField, readerNameField;

    @FXML
    private DatePicker borrowDatePicker, returnDatePicker;

    @FXML
    public void initialize() {
        // Configure the "select" column as a CheckBox
        selectColumn.setCellValueFactory(cellData -> cellData.getValue().chonProperty());
        selectColumn.setCellFactory(column -> new TableCell<Book, Boolean>() {
            private final CheckBox checkBox = new CheckBox();

            {
                // Handle the checkbox tick event
                checkBox.setOnAction(event -> {
                    Book book = getTableView().getItems().get(getIndex());
                    book.setChon(checkBox.isSelected());
                });
            }

            @Override
            protected void updateItem(Boolean item, boolean empty) {
                super.updateItem(item, empty);

                if (empty) {
                    setGraphic(null);
                } else {
                    checkBox.setSelected(item != null && item);
                    setGraphic(checkBox);
                }
            }
        });

        // Set default date for borrow date
        borrowDatePicker.setValue(LocalDate.now()); // Set default value as today

        // Configure other columns
        titleColumn.setCellValueFactory(new PropertyValueFactory<>("bookTitle"));
        authorColumn.setCellValueFactory(new PropertyValueFactory<>("authorName"));
        publisherColumn.setCellValueFactory(new PropertyValueFactory<>("publisher"));
        isbnColumn.setCellValueFactory(new PropertyValueFactory<>("bookId"));
        availableQuantityColumn.setCellValueFactory(new PropertyValueFactory<>("availableCopies"));
        borrowedQuantityColumn.setCellValueFactory(new PropertyValueFactory<>("borrowedCopies"));

        // Load data from database
        loadDataFromDatabase();
    }

    @FXML
    public void loadDataFromDatabase() {
        ObservableList<Book> bookList = FXCollections.observableArrayList();
        String sql = "SELECT * FROM `thông tin sách`";

        try (Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/library", "root", "1234");
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                bookList.add(new Book(
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
                });
            }
        } catch (SQLException e) {
            showError("Lỗi khi tải dữ liệu từ cơ sở dữ liệu: " + e.getMessage());
        }

        bookTable.setItems(bookList);
    }

    @FXML
    public void onAddNewBorrow(MouseEvent event) {
        // Reset input fields
        readerIdField.clear();
        readerNameField.clear();
        borrowDatePicker.setValue(LocalDate.now());
        returnDatePicker.setValue(null);

        // Reset selection state of books
        for (Book book : bookTable.getItems()) {
            book.setChon(false);
        }
        bookTable.refresh(); // Refresh the table

        showInfo("Đã reset thông tin. Bạn có thể nhập đơn mượn mới.");
    }

    @FXML
    public void onBackToMainPage(MouseEvent event) {
        try {
            // Go back to the main screen
            FXMLLoader loader = new FXMLLoader(getClass().getResource("quanlimuontra.fxml"));
            Scene scene = new Scene(loader.load());
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            showError("Không thể quay lại màn hình chính: " + e.getMessage());
        }
    }

    @FXML
    public void onSaveBorrowing(MouseEvent event) {
        String readerName = readerNameField.getText().trim();
        String readerId = readerIdField.getText().trim();
        LocalDate borrowDate = borrowDatePicker.getValue();
        LocalDate returnDate = returnDatePicker.getValue();

        if (!validateInputs(readerName, readerId, borrowDate, returnDate)) return;

        try (Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/library", "root", "1234")) {
            if (!checkReader(conn, readerName, readerId)) return;

            ObservableList<Book> bookList = bookTable.getItems();
            boolean hasSelectedBooks = false;

            for (Book book : bookList) {
                if (book.isChon()) {
                    hasSelectedBooks = true;

                    if (book.getAvailableCopies() <= 0) {
                        showError("Sách '" + book.getBookTitle() + "' hiện không còn.");
                        continue;
                    }
                    processBook(conn, book, readerId, borrowDate, returnDate);
                }
            }

            if (!hasSelectedBooks) {
                showError("Vui lòng chọn ít nhất một sách để mượn.");
                return;
            }

            showInfo("Lưu thông tin mượn sách thành công!");
            loadDataFromDatabase(); // Reload data after updating
        } catch (SQLException e) {
            showError("Lỗi cơ sở dữ liệu: " + e.getMessage());
        }
    }

    private boolean validateInputs(String readerName, String readerId, LocalDate borrowDate, LocalDate returnDate) {
        if (readerName.isEmpty() || readerId.isEmpty() || borrowDate == null || returnDate == null) {
            showError("Vui lòng nhập đầy đủ thông tin, bao gồm cả ngày trả.");
            return false;
        }
        if (returnDate.isBefore(borrowDate)) {
            showError("Ngày trả phải sau hoặc bằng ngày mượn.");
            return false;
        }
        return true;
    }

    private boolean checkReader(Connection conn, String readerName, String readerId) throws SQLException {
        String sql = "SELECT ten_docgia FROM `danh sách độc giả` WHERE madocgia = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, readerId);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                if (!rs.getString("ten_docgia").equalsIgnoreCase(readerName)) {
                    showError("Tên độc giả không khớp với mã độc giả.");
                    return false;
                }
            } else {
                showError("Mã độc giả không tồn tại.");
                return false;
            }
        }
        return true;
    }

    private void processBook(Connection conn, Book book, String readerId, LocalDate borrowDate, LocalDate returnDate) throws SQLException {
        String borrowReceiptId = readerId + "_" + book.getBookId();

        // Add status column with value "borrowed"
        String sqlInsert = "INSERT INTO `lượt mượn` (ma_phieu, ISBN, madocgia, ngay_muon, ngay_tra, tinh_trang) VALUES (?, ?, ?, ?, ?, ?)";
        String sqlUpdate = "UPDATE `thông tin sách` SET so_luong_hien_con = so_luong_hien_con - 1, so_luong_muon = so_luong_muon + 1 WHERE ISBN = ?";

        try (PreparedStatement stmtInsert = conn.prepareStatement(sqlInsert);
             PreparedStatement stmtUpdate = conn.prepareStatement(sqlUpdate)) {

            // Set values for the INSERT statement
            stmtInsert.setString(1, borrowReceiptId);
            stmtInsert.setString(2, book.getBookId());
            stmtInsert.setString(3, readerId);
            stmtInsert.setDate(4, Date.valueOf(borrowDate));
            stmtInsert.setDate(5, Date.valueOf(returnDate));
            stmtInsert.setString(6, "mượn");

            // Execute INSERT query
            stmtInsert.executeUpdate();

            // Set values for the UPDATE statement
            stmtUpdate.setString(1, book.getBookId());

            // Execute UPDATE query
            stmtUpdate.executeUpdate();
        }
    }

    private void showError(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR, message, ButtonType.OK);
        alert.showAndWait();
    }

    private void showInfo(String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION, message, ButtonType.OK);
        alert.showAndWait();
    }
}
