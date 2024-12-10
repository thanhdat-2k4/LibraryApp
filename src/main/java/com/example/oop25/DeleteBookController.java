////// xoa sach

package com.example.oop25;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

import java.sql.*;

public class DeleteBookController {

    private static final String DB_URL = "jdbc:mysql://localhost:3306/library";
    private static final String DB_USER = "root";
    private static final String DB_PASSWORD = "1234";

    @FXML
    private TableColumn<Book, String> colISBN;

    @FXML
    private TableColumn<Book, String> colPublisher;

    @FXML
    private TableColumn<Book, Integer> colAvailableQuantity;

    @FXML
    private TableColumn<Book, Integer> colBorrowedQuantity;

    @FXML
    private TableColumn<Book, String> colBookName;

    @FXML
    private TableColumn<Book, String> colAuthorName;

    @FXML
    private TableView<Book> tableViewBooks;

    private Book selectedBook; // Lưu trữ sách được chọn

    private void showAlert(Alert.AlertType alertType, String title, String message) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setContentText(message);
        alert.showAndWait();
    }

    private boolean doesBookExist(String bookId, String bookTitle) {
        String sql = "SELECT COUNT(*) FROM `thông tin sách` WHERE ISBN = ? AND ten_sach = ?";
        try (Connection connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

            preparedStatement.setString(1, bookId);
            preparedStatement.setString(2, bookTitle);
            ResultSet resultSet = preparedStatement.executeQuery();
            return resultSet.next() && resultSet.getInt(1) > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Lỗi", "Không thể kiểm tra sách trong cơ sở dữ liệu!\nChi tiết: " + e.getMessage());
            return false;
        }
    }

    private boolean isBookBorrowed(String bookId) {
        String sql = "SELECT so_luong_muon FROM `thông tin sách` WHERE ISBN = ?";
        try (Connection connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

            preparedStatement.setString(1, bookId);
            ResultSet resultSet = preparedStatement.executeQuery();
            return resultSet.next() && resultSet.getInt("so_luong_muon") > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Lỗi", "Không thể kiểm tra trạng thái sách!\nChi tiết: " + e.getMessage());
            return false;
        }
    }

    private void deleteBook(String isbn) {
        String sql = "DELETE FROM `thông tin sách` WHERE ISBN = ?";
        try (Connection connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

            preparedStatement.setString(1, isbn);
            int rowsAffected = preparedStatement.executeUpdate();
            if (rowsAffected > 0) {
                showAlert(Alert.AlertType.INFORMATION, "Thành công", "Sách đã được xóa khỏi hệ thống!");
            } else {
                showAlert(Alert.AlertType.WARNING, "Thất bại", "Không thể xóa sách!");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Lỗi", "Không thể xóa sách khỏi cơ sở dữ liệu!\nChi tiết: " + e.getMessage());
        }
    }

    @FXML
    void onConfirmDeleteClick(MouseEvent event) {
        if (selectedBook == null) {
            showAlert(Alert.AlertType.WARNING, "Lỗi", "Vui lòng chọn một sách để xóa!");
            return;
        }

        String isbn = selectedBook.getBookId();

        if (isBookBorrowed(isbn)) {
            Alert confirmAlert = new Alert(Alert.AlertType.CONFIRMATION);
            confirmAlert.setTitle("Xác nhận");
            confirmAlert.setHeaderText("Sách hiện đang được mượn!");
            confirmAlert.setContentText("Bạn có chắc chắn muốn xóa sách đang được mượn không?");
            confirmAlert.getButtonTypes().setAll(ButtonType.YES, ButtonType.NO);

            confirmAlert.showAndWait().ifPresent(response -> {
                if (response == ButtonType.YES) {
                    deleteBook(isbn);
                    loadBooksFromDatabase();
                }
            });
        } else {
            deleteBook(isbn);
            loadBooksFromDatabase();
        }
    }

    private void loadBooksFromDatabase() {
        ObservableList<Book> bookList = FXCollections.observableArrayList();
        String query = "SELECT * FROM `thông tin sách`";

        try (Connection connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
             Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(query)) {

            while (resultSet.next()) {
                bookList.add(new Book(
                        resultSet.getString("ISBN"),
                        resultSet.getString("ten_sach"),
                        resultSet.getString("ten_tac_gia"),
                        resultSet.getString("NXB"),
                        resultSet.getInt("so_luong_hien_con"),
                        resultSet.getInt("so_luong_muon")
                ) {
                    @Override
                    public int getAvailableQuantity() {
                        return getAvailableCopies();
                    }

                    @Override
                    public int getBorrowedQuantity() {
                        return getBorrowedCopies();
                    }

                    @Override
                    public void setAvailableQuantity(int availableQuantity) {
                        setAvailableCopies(availableQuantity);
                    }
                });
            }

            tableViewBooks.setItems(bookList);

        } catch (SQLException e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Lỗi", "Không thể tải danh sách sách từ cơ sở dữ liệu!");
        }
    }

    @FXML
    void onBackToBookManagementClick(MouseEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("Quanlysach.fxml"));
            Parent root = loader.load();

            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.show();

        } catch (Exception e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Lỗi", "Không thể quay lại màn hình quản lý sách!");
        }
    }

    @FXML
    public void initialize() {
        colISBN.setCellValueFactory(new PropertyValueFactory<>("bookId"));
        colBookName.setCellValueFactory(new PropertyValueFactory<>("bookTitle"));
        colAuthorName.setCellValueFactory(new PropertyValueFactory<>("authorName"));
        colPublisher.setCellValueFactory(new PropertyValueFactory<>("publisher"));
        colAvailableQuantity.setCellValueFactory(new PropertyValueFactory<>("availableCopies"));
        colBorrowedQuantity.setCellValueFactory(new PropertyValueFactory<>("borrowedCopies"));

        tableViewBooks.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            selectedBook = newValue;
        });

        loadBooksFromDatabase();
    }
}

