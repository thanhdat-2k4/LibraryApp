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

public class BookAdder {

    // Thông tin kết nối cơ sở dữ liệu
    private static final String DB_URL = "jdbc:mysql://localhost:3306/library";
    private static final String DB_USER = "root";
    private static final String DB_PASSWORD = "1234";

    @FXML
    private TableView<Book> tableViewBooks;

    @FXML
    private TableColumn<Book, String> colBookId;

    @FXML
    private TableColumn<Book, String> colBookTitle;

    @FXML
    private TableColumn<Book, String> colAuthorName;

    @FXML
    private TableColumn<Book, String> colPublisher;

    @FXML
    private TableColumn<Book, Integer> colAvailableCopies;

    @FXML
    private TableColumn<Book, Integer> colBorrowedCopies;

    @FXML
    private TextField tfBookId;          // Mã sách (ISBN)
    @FXML
    private TextField tfBookTitle;       // Tên sách
    @FXML
    private TextField tfAuthorName;      // Tên tác giả
    @FXML
    private TextField tfPublisher;       // Nhà xuất bản
    @FXML
    private TextField tfAvailableCopies; // Số lượng sách còn lại

    @FXML
    public void initialize() {
        colBookId.setCellValueFactory(data -> data.getValue().bookIdProperty());
        colBookTitle.setCellValueFactory(data -> data.getValue().bookTitleProperty());
        colAuthorName.setCellValueFactory(data -> data.getValue().authorNameProperty());
        colPublisher.setCellValueFactory(data -> data.getValue().publisherProperty());
        colAvailableCopies.setCellValueFactory(data -> data.getValue().availableCopiesProperty().asObject());
        colBorrowedCopies.setCellValueFactory(data -> data.getValue().borrowedCopiesProperty().asObject());

        loadBooks();
    }

    // Thêm sách vào cơ sở dữ liệu
    private void addBook(String bookId, String bookTitle, String authorName, String publisher, int availableCopies) {
        String sql = "INSERT INTO `thông tin sách` (ISBN, ten_sach, ten_tac_gia, NXB, so_luong_hien_con, so_luong_muon) " +
                "VALUES (?, ?, ?, ?, ?, 0)";
        try (Connection connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

            preparedStatement.setString(1, bookId);
            preparedStatement.setString(2, bookTitle);
            preparedStatement.setString(3, authorName);
            preparedStatement.setString(4, publisher);
            preparedStatement.setInt(5, availableCopies);

            int rowsAffected = preparedStatement.executeUpdate();
            if (rowsAffected > 0) {
                showAlert(Alert.AlertType.INFORMATION, "Thành công", "Sách đã được thêm thành công!");
            } else {
                showAlert(Alert.AlertType.WARNING, "Thất bại", "Không thể thêm sách.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Lỗi", "Không thể thêm sách vào cơ sở dữ liệu.\nChi tiết: " + e.getMessage());
        }
    }

    // Tải tất cả sách từ cơ sở dữ liệu
    private void loadBooks() {
        ObservableList<Book> bookList = FXCollections.observableArrayList();
        String query = "SELECT * FROM `thông tin sách`";

        try (Connection connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
             Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(query)) {

            while (resultSet.next()) {
                String bookId = resultSet.getString("ISBN");
                String bookTitle = resultSet.getString("ten_sach");
                String authorName = resultSet.getString("ten_tac_gia");
                String publisher = resultSet.getString("NXB");
                int availableCopies = resultSet.getInt("so_luong_hien_con");
                int borrowedCopies = resultSet.getInt("so_luong_muon");

                bookList.add(new Book(bookId, bookTitle, authorName, publisher, availableCopies, borrowedCopies) {
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
        } catch (Exception e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Lỗi", "Không thể tải danh sách sách từ cơ sở dữ liệu.");
        }
    }

    // Kiểm tra xem sách đã tồn tại trong cơ sở dữ liệu hay chưa
    private boolean doesBookExist(String bookId, String bookTitle) {
        String sql = "SELECT COUNT(*) FROM `thông tin sách` WHERE ISBN = ? OR ten_sach = ?";
        try (Connection connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

            preparedStatement.setString(1, bookId);
            preparedStatement.setString(2, bookTitle);

            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                return resultSet.getInt(1) > 0;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Lỗi", "Không thể kiểm tra xem sách đã tồn tại.\nChi tiết: " + e.getMessage());
        }
        return false;
    }

    @FXML
    void autoFillBookDetails(MouseEvent event) {
        TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle("Tự động điền thông tin");
        dialog.setHeaderText("Nhập ISBN của sách");
        dialog.setContentText("ISBN:");

        // Nhập ISBN từ hộp thoại
        String isbn = dialog.showAndWait().orElse(null);
        if (isbn != null && !isbn.isEmpty()) {
            // Loại bỏ tất cả các ký tự không phải số trong ISBN (chỉ giữ lại số)
            isbn = isbn.replaceAll("[^\\d]", "");

            // Lấy thông tin sách từ API
            String bookInfo = Tudongdien.Tudongdienthongtin(isbn);
            if (bookInfo != null) {
                String[] details = bookInfo.split(";");
                String title = details[0];
                String author = details[1];
                String publisher = details[2];

                // Điền thông tin vào các trường
                tfBookId.setText(isbn);
                tfBookTitle.setText(title);
                tfAuthorName.setText(author);
                tfPublisher.setText(publisher);

                showAlert(Alert.AlertType.INFORMATION, "Thông tin sách",
                        "Thông tin đã được tự động điền:\n" +
                                "ISBN: " + isbn + "\n" +
                                "Tên sách: " + title + "\n" +
                                "Tác giả: " + author + "\n" +
                                "NXB: " + publisher);
            } else {
                showAlert(Alert.AlertType.WARNING, "Không tìm thấy", "Không tìm thấy thông tin cho ISBN: " + isbn);
            }
        }
    }



    @FXML
    void confirmAddBook(MouseEvent event) {
        try {
            String bookId = tfBookId.getText().trim();
            String bookTitle = tfBookTitle.getText().trim();
            String authorName = tfAuthorName.getText().trim();
            String publisher = tfPublisher.getText().trim();
            String availableCopiesStr = tfAvailableCopies.getText().trim();

            if (bookId.isEmpty() || bookTitle.isEmpty() || authorName.isEmpty() || publisher.isEmpty() || availableCopiesStr.isEmpty()) {
                showAlert(Alert.AlertType.WARNING, "Lỗi", "Vui lòng điền đầy đủ thông tin.");
                return;
            }

            int availableCopies = Integer.parseInt(availableCopiesStr);

            // Kiểm tra nếu số lượng sách nhỏ hơn 0
            if (availableCopies < 0) {
                showAlert(Alert.AlertType.WARNING, "Lỗi", "Số lượng sách phải lớn hơn hoặc bằng 0.");
                return;
            }

            if (doesBookExist(bookId, bookTitle)) {
                showAlert(Alert.AlertType.WARNING, "Trùng lặp", "Sách đã tồn tại.");
                return;
            }

            addBook(bookId, bookTitle, authorName, publisher, availableCopies);
            loadBooks();
        } catch (NumberFormatException e) {
            showAlert(Alert.AlertType.ERROR, "Lỗi", "Số lượng sách phải là một số hợp lệ.");
        } catch (Exception e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Lỗi", "Không thể thêm sách.");
        }
    }

    @FXML
    void goBackToBookManager(MouseEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("Quanlysach.fxml"));
            Parent root = loader.load();
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Lỗi", "Không thể quay lại.");
        }
    }

    // Hàm hiển thị thông báo lỗi
    private void showAlert(Alert.AlertType alertType, String title, String message) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
