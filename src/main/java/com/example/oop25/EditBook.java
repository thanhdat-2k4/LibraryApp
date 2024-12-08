// sua sach

package com.example.oop25;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

import java.sql.*;
import java.util.Optional;

public class EditBook {

    // Database connection details
    private static final String DB_URL = "jdbc:mysql://localhost:3306/library";
    private static final String DB_USER = "root";
    private static final String DB_PASSWORD = "1234";

    @FXML
    private TableColumn<Book, String> colISBN;

    @FXML
    private TableColumn<Book, String> colPublisher;

    @FXML
    private TableColumn<Book, Integer> colAvailableCopies;

    @FXML
    private TableColumn<Book, Integer> colBorrowedCopies;

    @FXML
    private TableColumn<Book, String> colBookTitle;

    @FXML
    private TableColumn<Book, String> colAuthor;

    @FXML
    private TableView<Book> tableViewBooks;

    @FXML
    private TextField tfISBN;

    @FXML
    private TextField tfPublisher;

    @FXML
    private TextField tfAvailableCopies;

    @FXML
    private TextField tfBorrowedCopies;

    @FXML
    private TextField tfBookTitle;

    @FXML
    private TextField tfAuthor;

    // Check if the book exists in the database
    private boolean isBookExist(String isbn) {
        String sql = "SELECT COUNT(*) FROM `thông tin sách` WHERE ISBN = ?";
        try (Connection connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

            preparedStatement.setString(1, isbn);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                return resultSet.getInt(1) > 0; // Return true if the book exists
            }
        } catch (SQLException e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Lỗi", "Không thể kiểm tra sách trong cơ sở dữ liệu!\nChi tiết: " + e.getMessage());
        }
        return false;
    }

    // Update book information in the database
    private void updateBook(String isbn, String bookTitle, String author, String publisher, int availableCopies, int borrowedCopies) {
        String sql = "UPDATE `thông tin sách` SET ten_sach = ?, ten_tac_gia = ?, NXB = ?, so_luong_hien_con = ?, so_luong_muon = ? WHERE ISBN = ?";

        try (Connection connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

            preparedStatement.setString(1, bookTitle);
            preparedStatement.setString(2, author);
            preparedStatement.setString(3, publisher);
            preparedStatement.setInt(4, availableCopies);
            preparedStatement.setInt(5, borrowedCopies);
            preparedStatement.setString(6, isbn);

            int rowsAffected = preparedStatement.executeUpdate();
            if (rowsAffected > 0) {
                showAlert(Alert.AlertType.INFORMATION, "Thành công", "Thông tin sách đã được cập nhật!");
            } else {
                showAlert(Alert.AlertType.WARNING, "Thất bại", "Không thể cập nhật thông tin sách!");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Lỗi", "Không thể cập nhật thông tin sách!\nChi tiết: " + e.getMessage());
        }
    }

    // "Confirm Edit" button click event
    @FXML
    void onConfirmEditClick(MouseEvent event) {
        try {
            // Get data from the input fields
            String isbn = tfISBN.getText().trim();
            String bookTitle = tfBookTitle.getText().trim();
            String author = tfAuthor.getText().trim();
            String publisher = tfPublisher.getText().trim();
            String availableCopiesStr = tfAvailableCopies.getText().trim();
            String borrowedCopiesStr = tfBorrowedCopies.getText().trim();

            // Check for empty values
            if (isbn.isEmpty() || bookTitle.isEmpty() || author.isEmpty() || publisher.isEmpty() || availableCopiesStr.isEmpty() || borrowedCopiesStr.isEmpty()) {
                showAlert(Alert.AlertType.WARNING, "Lỗi", "Vui lòng điền đầy đủ thông tin!");
                return;
            }

            // Convert quantities to integers
            int availableCopies = Integer.parseInt(availableCopiesStr);
            int borrowedCopies = Integer.parseInt(borrowedCopiesStr);

            // Check if the book exists
            if (!isBookExist(isbn)) {
                showAlert(Alert.AlertType.WARNING, "Thất bại", "Sách không tồn tại trên hệ thống!");
                return;
            }

            // Update the book information
            updateBook(isbn, bookTitle, author, publisher, availableCopies, borrowedCopies);

            // Refresh the book list on the UI
            loadBooksFromDatabase();

        } catch (NumberFormatException e) {
            showAlert(Alert.AlertType.ERROR, "Lỗi", "Số lượng phải là một số hợp lệ!");
        } catch (Exception e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Lỗi", "Có lỗi xảy ra khi cập nhật sách: " + e.getMessage());
        }
    }

    // Load book list from the database
    private void loadBooksFromDatabase() {
        ObservableList<Book> bookList = FXCollections.observableArrayList();
        String query = "SELECT * FROM `thông tin sách`";

        try (Connection connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
             Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(query)) {

            while (resultSet.next()) {
                String isbn = resultSet.getString("ISBN");
                String bookTitle = resultSet.getString("ten_sach");
                String author = resultSet.getString("ten_tac_gia");
                String publisher = resultSet.getString("NXB");
                int availableCopies = resultSet.getInt("so_luong_hien_con");
                int borrowedCopies = resultSet.getInt("so_luong_muon");

                bookList.add(new Book(isbn, bookTitle, author, publisher, availableCopies, borrowedCopies) {
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

            tableViewBooks.setItems(bookList);

        } catch (SQLException e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Lỗi", "Không thể tải danh sách sách từ cơ sở dữ liệu!");
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
            showAlert(Alert.AlertType.ERROR, "Lỗi", "Không thể quay lại giao diện quản lý sách!");
        }
    }

    // Show alert messages
    private void showAlert(Alert.AlertType alertType, String title, String message) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setContentText(message);
        alert.showAndWait();
    }

    // Initialize the table and load data
    @FXML
    public void initialize() {
        colISBN.setCellValueFactory(data -> data.getValue().bookIdProperty());
        colBookTitle.setCellValueFactory(data -> data.getValue().bookTitleProperty());
        colAuthor.setCellValueFactory(data -> data.getValue().authorNameProperty());
        colPublisher.setCellValueFactory(data -> data.getValue().publisherProperty());
        colAvailableCopies.setCellValueFactory(data -> data.getValue().availableCopiesProperty().asObject());
        colBorrowedCopies.setCellValueFactory(data -> data.getValue().borrowedCopiesProperty().asObject());

        loadBooksFromDatabase();
    }
// tu dong dien
@FXML
void autoFillBookDetails(MouseEvent event) {
    // Tạo một hộp thoại để người dùng nhập ISBN
    TextInputDialog dialog = new TextInputDialog();
    dialog.setTitle("Nhập ISBN");
    dialog.setHeaderText("Nhập ISBN của sách bạn muốn tìm");
    dialog.setContentText("ISBN:");

    // Lấy giá trị người dùng nhập vào
    String isbn = dialog.showAndWait().orElse(null);

    // Kiểm tra nếu ISBN không được nhập
    if (isbn == null || isbn.trim().isEmpty()) {
        showAlert(Alert.AlertType.WARNING, "Lỗi", "Vui lòng nhập ISBN hợp lệ!");
        return;
    }

    // Tìm kiếm sách từ cơ sở dữ liệu với ISBN đã nhập
    String query = "SELECT * FROM `thông tin sách` WHERE ISBN = ?";
    try (Connection connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
         PreparedStatement preparedStatement = connection.prepareStatement(query)) {

        preparedStatement.setString(1, isbn);
        ResultSet resultSet = preparedStatement.executeQuery();

        // Nếu tìm thấy sách
        if (resultSet.next()) {
            // Lấy thông tin sách từ kết quả truy vấn
            String bookTitle = resultSet.getString("ten_sach");
            String author = resultSet.getString("ten_tac_gia");
            String publisher = resultSet.getString("NXB");
            int availableCopies = resultSet.getInt("so_luong_hien_con");
            int borrowedCopies = resultSet.getInt("so_luong_muon");

            // Điền thông tin vào các TextField
            tfISBN.setText(isbn); // Điền ISBN vào TextField tfISBN
            tfBookTitle.setText(bookTitle);
            tfAuthor.setText(author);
            tfPublisher.setText(publisher);
            tfAvailableCopies.setText(String.valueOf(availableCopies));
            tfBorrowedCopies.setText(String.valueOf(borrowedCopies));

        } else {
            // Nếu không tìm thấy sách
            showAlert(Alert.AlertType.WARNING, "Không tìm thấy sách", "Không có sách nào với ISBN này trong cơ sở dữ liệu.");
        }
    } catch (SQLException e) {
        e.printStackTrace();
        showAlert(Alert.AlertType.ERROR, "Lỗi", "Không thể truy vấn cơ sở dữ liệu!\nChi tiết: " + e.getMessage());
    }
}



}

