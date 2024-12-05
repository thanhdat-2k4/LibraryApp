// xoa sach

//package com.example.oop25;
//
//import javafx.collections.FXCollections;
//import javafx.collections.ObservableList;
//import javafx.fxml.FXML;
//import javafx.fxml.FXMLLoader;
//import javafx.scene.Node;
//import javafx.scene.Parent;
//import javafx.scene.Scene;
//import javafx.scene.control.*;
//import javafx.scene.control.cell.PropertyValueFactory;
//import javafx.scene.input.MouseEvent;
//import javafx.stage.Stage;
//
//import java.sql.*;
//
//public class Xoasach {
//
//    // Thông tin kết nối cơ sở dữ liệu
//    private static final String DB_URL = "jdbc:mysql://localhost:3306/library";
//    private static final String DB_USER = "root";
//    private static final String DB_PASSWORD = "1234";
//
//    @FXML
//    private TableColumn<Sach, String> colISBN;
//
//    @FXML
//    private TableColumn<Sach, String> colNXB;
//
//    @FXML
//    private TableColumn<Sach, Integer> colSoLuongHienCon;
//
//    @FXML
//    private TableColumn<Sach, Integer> colSoLuongMuon;
//
//    @FXML
//    private TableColumn<Sach, String> colTenSach;
//
//    @FXML
//    private TableColumn<Sach, String> colTenTacGia;
//
//    @FXML
//    private TextField tfISBN;
//
//    @FXML
//    private TextField tfTenSach;
//
//    @FXML
//    private TableView<Sach> tableViewBooks;
//
//    // Kiểm tra sách có tồn tại hay không
//    private boolean isBookExist(String isbn, String tenSach) {
//        String sql = "SELECT COUNT(*) FROM `thông tin sách` WHERE ISBN = ? AND ten_sach = ?";
//        try (Connection connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
//             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
//
//            preparedStatement.setString(1, isbn);
//            preparedStatement.setString(2, tenSach);
//            ResultSet resultSet = preparedStatement.executeQuery();
//            if (resultSet.next()) {
//                return resultSet.getInt(1) > 0; // Trả về true nếu sách tồn tại
//            }
//        } catch (SQLException e) {
//            e.printStackTrace();
//            showAlert(Alert.AlertType.ERROR, "Lỗi", "Không thể kiểm tra sách trong cơ sở dữ liệu!\nChi tiết: " + e.getMessage());
//        }
//        return false;
//    }
//
//    // Kiểm tra xem sách có đang được mượn không
//    private boolean isBookBorrowed(String isbn) {
//        String sql = "SELECT so_luong_muon FROM `thông tin sách` WHERE ISBN = ?";
//        try (Connection connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
//             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
//
//            preparedStatement.setString(1, isbn);
//            ResultSet resultSet = preparedStatement.executeQuery();
//            if (resultSet.next()) {
//                return resultSet.getInt("so_luong_muon") > 0;
//            }
//        } catch (SQLException e) {
//            e.printStackTrace();
//            showAlert(Alert.AlertType.ERROR, "Lỗi", "Không thể kiểm tra tình trạng sách trong cơ sở dữ liệu!\nChi tiết: " + e.getMessage());
//        }
//        return false;
//    }
//
//    // Xóa sách khỏi cơ sở dữ liệu
//    private void deleteBook(String isbn) {
//        String sql = "DELETE FROM `thông tin sách` WHERE ISBN = ?";
//        try (Connection connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
//             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
//
//            preparedStatement.setString(1, isbn);
//            int rowsAffected = preparedStatement.executeUpdate();
//            if (rowsAffected > 0) {
//                showAlert(Alert.AlertType.INFORMATION, "Thành công", "Sách đã được xóa khỏi hệ thống!");
//            } else {
//                showAlert(Alert.AlertType.WARNING, "Thất bại", "Không thể xóa sách!");
//            }
//        } catch (SQLException e) {
//            e.printStackTrace();
//            showAlert(Alert.AlertType.ERROR, "Lỗi", "Không thể xóa sách khỏi cơ sở dữ liệu!\nChi tiết: " + e.getMessage());
//        }
//    }
//
//    // Sự kiện nhấn nút "Xác nhận xóa"
//    @FXML
//    void click_xacnhanxoa(MouseEvent event) {
//        String isbn = tfISBN.getText().trim();
//        String tenSach = tfTenSach.getText().trim();
//
//        // Kiểm tra giá trị rỗng
//        if (isbn.isEmpty() || tenSach.isEmpty()) {
//            showAlert(Alert.AlertType.WARNING, "Lỗi", "Vui lòng điền đầy đủ thông tin!");
//            return;
//        }
//
//        // Kiểm tra sách có tồn tại không
//        if (!isBookExist(isbn, tenSach)) {
//            showAlert(Alert.AlertType.WARNING, "Lỗi", "Sách không tồn tại trên hệ thống!");
//            return;
//        }
//
//        // Kiểm tra sách đang được mượn hay không
//        if (isBookBorrowed(isbn)) {
//            // Hiển thị cửa sổ xác nhận
//            Alert confirmAlert = new Alert(Alert.AlertType.CONFIRMATION);
//            confirmAlert.setTitle("Xác nhận");
//            confirmAlert.setHeaderText("Sách đang được mượn!");
//            confirmAlert.setContentText("Bạn có chắc chắn muốn xóa quyển sách đang được mượn này không?");
//            ButtonType buttonYes = new ButtonType("Có");
//            ButtonType buttonNo = new ButtonType("Không", ButtonBar.ButtonData.CANCEL_CLOSE);
//            confirmAlert.getButtonTypes().setAll(buttonYes, buttonNo);
//
//            confirmAlert.showAndWait().ifPresent(response -> {
//                if (response == buttonYes) {
//                    // Xóa sách
//                    deleteBook(isbn);
//                    loadBooksFromDatabase();
//                }
//            });
//        } else {
//            // Xóa sách nếu không bị mượn
//            deleteBook(isbn);
//            loadBooksFromDatabase();
//        }
//    }
//
//    // Tải danh sách sách từ cơ sở dữ liệu
//    private void loadBooksFromDatabase() {
//        ObservableList<Sach> bookList = FXCollections.observableArrayList();
//        String query = "SELECT * FROM `thông tin sách`";
//
//        try (Connection connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
//             Statement statement = connection.createStatement();
//             ResultSet resultSet = statement.executeQuery(query)) {
//
//            while (resultSet.next()) {
//                String isbn = resultSet.getString("ISBN");
//                String tenSach = resultSet.getString("ten_sach");
//                String tenTacGia = resultSet.getString("ten_tac_gia");
//                String nxb = resultSet.getString("NXB");
//                int soLuongHienCon = resultSet.getInt("so_luong_hien_con");
//                int soLuongMuon = resultSet.getInt("so_luong_muon");
//
//                bookList.add(new Sach(isbn, tenSach, tenTacGia, nxb, soLuongHienCon, soLuongMuon));
//            }
//
//            tableViewBooks.setItems(bookList);
//
//        } catch (SQLException e) {
//            e.printStackTrace();
//            showAlert(Alert.AlertType.ERROR, "Lỗi", "Không thể tải danh sách sách từ cơ sở dữ liệu!");
//        }
//    }
//
//    // Quay lại giao diện quản lý sách
//    @FXML
//    void click_quaylaiquanlysach(MouseEvent event) {
//        try {
//            FXMLLoader loader = new FXMLLoader(getClass().getResource("Quanlysach.fxml"));
//            Parent root = loader.load();
//
//            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
//            Scene scene = new Scene(root);
//            stage.setScene(scene);
//            stage.show();
//
//        } catch (Exception e) {
//            e.printStackTrace();
//            showAlert(Alert.AlertType.ERROR, "Lỗi", "Không thể quay lại giao diện quản lý sách!");
//        }
//    }
//
//    // Hiển thị thông báo
//    private void showAlert(Alert.AlertType alertType, String title, String message) {
//        Alert alert = new Alert(alertType);
//        alert.setTitle(title);
//        alert.setContentText(message);
//        alert.showAndWait();
//    }
//
//    // Khởi tạo bảng và tải dữ liệu
//    @FXML
//    public void initialize() {
//        colISBN.setCellValueFactory(new PropertyValueFactory<>("isbn"));
//        colTenSach.setCellValueFactory(new PropertyValueFactory<>("tenSach"));
//        colTenTacGia.setCellValueFactory(new PropertyValueFactory<>("tenTacGia"));
//        colNXB.setCellValueFactory(new PropertyValueFactory<>("nxb"));
//        colSoLuongHienCon.setCellValueFactory(new PropertyValueFactory<>("soLuongHienCon"));
//        colSoLuongMuon.setCellValueFactory(new PropertyValueFactory<>("soLuongMuon"));
//        // Thiết lập các cột cho bảng
//        loadBooksFromDatabase();
//    }
//}
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

    // Database connection information
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
    private TextField tfISBN;

    @FXML
    private TextField tfBookName;

    @FXML
    private TableView<Book> tableViewBooks;

    // Check if the book exists in the database
    private boolean doesBookExist(String bookId, String bookTitle) {
        String sql = "SELECT COUNT(*) FROM `thông tin sách` WHERE ISBN = ? AND ten_sach = ?";
        try (Connection connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

            preparedStatement.setString(1, bookId);
            preparedStatement.setString(2, bookTitle);
            ResultSet resultSet = preparedStatement.executeQuery();
            return resultSet.next() && resultSet.getInt(1) > 0; // Return true if the book exists

        } catch (SQLException e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Error", "Unable to check if the book exists in the database!\nDetails: " + e.getMessage());
            return false;
        }
    }

    // Check if the book is currently borrowed
    private boolean isBookBorrowed(String bookId) {
        String sql = "SELECT so_luong_muon FROM `thông tin sách` WHERE ISBN = ?";
        try (Connection connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

            preparedStatement.setString(1, bookId);
            ResultSet resultSet = preparedStatement.executeQuery();
            return resultSet.next() && resultSet.getInt("so_luong_muon") > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Error", "Unable to check the book's status in the database!\nDetails: " + e.getMessage());
            return false;
        }
    }

    // Delete the book from the database
    private void deleteBook(String isbn) {
        String sql = "DELETE FROM `thông tin sách` WHERE ISBN = ?";
        try (Connection connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

            preparedStatement.setString(1, isbn);
            int rowsAffected = preparedStatement.executeUpdate();
            if (rowsAffected > 0) {
                showAlert(Alert.AlertType.INFORMATION, "Success", "The book has been deleted from the system!");
            } else {
                showAlert(Alert.AlertType.WARNING, "Failure", "Unable to delete the book!");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Error", "Unable to delete the book from the database!\nDetails: " + e.getMessage());
        }
    }

    // Event handler for "Confirm Delete" button click
    @FXML
    void onConfirmDeleteClick(MouseEvent event) {
        String isbn = tfISBN.getText().trim();
        String bookName = tfBookName.getText().trim();

        // Check for empty fields
        if (isbn.isEmpty() || bookName.isEmpty()) {
            showAlert(Alert.AlertType.WARNING, "Error", "Please fill in all the information!");
            return;
        }

        // Check if the book exists
        if (!doesBookExist(isbn, bookName)) {
            showAlert(Alert.AlertType.WARNING, "Error", "The book does not exist in the system!");
            return;
        }

        // Check if the book is borrowed
        if (isBookBorrowed(isbn)) {
            Alert confirmAlert = new Alert(Alert.AlertType.CONFIRMATION);
            confirmAlert.setTitle("Confirmation");
            confirmAlert.setHeaderText("The book is currently borrowed!");
            confirmAlert.setContentText("Are you sure you want to delete the borrowed book?");
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

    // Load the list of books from the database
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
            showAlert(Alert.AlertType.ERROR, "Error", "Unable to load the list of books from the database!");
        }
    }

    // Go back to the book management screen
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
            showAlert(Alert.AlertType.ERROR, "Error", "Unable to return to the book management screen!");
        }
    }

    // Display an alert message
    private void showAlert(Alert.AlertType alertType, String title, String message) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setContentText(message);
        alert.showAndWait();
    }

    // Initialize the table and load data
    @FXML
    public void initialize() {
        colISBN.setCellValueFactory(new PropertyValueFactory<>("bookId"));
        colBookName.setCellValueFactory(new PropertyValueFactory<>("bookTitle"));
        colAuthorName.setCellValueFactory(new PropertyValueFactory<>("authorName"));
        colPublisher.setCellValueFactory(new PropertyValueFactory<>("publisher"));
        colAvailableQuantity.setCellValueFactory(new PropertyValueFactory<>("availableCopies"));
        colBorrowedQuantity.setCellValueFactory(new PropertyValueFactory<>("borrowedCopies"));

        loadBooksFromDatabase();
    }
}
