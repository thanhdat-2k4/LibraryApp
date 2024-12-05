package com.example.oop25;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.scene.Node;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.fxml.FXMLLoader;
import java.sql.*;
import java.io.IOException;

public class BookSearch {

    @FXML
    private TextField txtBookId;

    @FXML
    private TextField txtBookTitle;

    @FXML
    private TextField txtAuthorName;

    @FXML
    private TextField txtPublisher;

    @FXML
    private TextField txtAvailableQuantity;

    @FXML
    private TextField txtBorrowedQuantity;

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
    private TableColumn<Book, Integer> colAvailableQuantity;

    @FXML
    private TableColumn<Book, Integer> colBorrowedQuantity;

    private ObservableList<Book> bookList;

    private final String DB_URL = "jdbc:mysql://localhost:3306/library";
    private final String DB_USER = "root";
    private final String DB_PASSWORD = "1234";

    @FXML
    void onFilterClick(MouseEvent event) {
        // Get input from text fields
        String bookId = txtBookId.getText().trim();
        String bookTitle = txtBookTitle.getText().trim();
        String authorName = txtAuthorName.getText().trim();
        String publisher = txtPublisher.getText().trim();
        String availableQuantity = txtAvailableQuantity.getText().trim();
        String borrowedQuantity = txtBorrowedQuantity.getText().trim();

        // Build dynamic SQL query
        StringBuilder queryBuilder = new StringBuilder("SELECT * FROM `thông tin sách` WHERE 1=1");

        if (!bookId.isEmpty()) queryBuilder.append(" AND ISBN LIKE '%").append(bookId).append("%'");
        if (!bookTitle.isEmpty()) queryBuilder.append(" AND ten_sach LIKE '%").append(bookTitle).append("%'");
        if (!authorName.isEmpty()) queryBuilder.append(" AND ten_tac_gia LIKE '%").append(authorName).append("%'");
        if (!publisher.isEmpty()) queryBuilder.append(" AND NXB LIKE '%").append(publisher).append("%'");
        if (!availableQuantity.isEmpty()) queryBuilder.append(" AND so_luong_hien_con = ").append(availableQuantity);
        if (!borrowedQuantity.isEmpty()) queryBuilder.append(" AND so_luong_muon = ").append(borrowedQuantity);

        String query = queryBuilder.toString();

        // Load filtered books from the database
        loadFilteredBooks(query);
    }

    @FXML
    void onBackToBookManagement(MouseEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("Quanlysach.fxml"));
            Node source = (Node) event.getSource();
            Stage stage = (Stage) source.getScene().getWindow();
            Scene scene = new Scene(loader.load());
            stage.setScene(scene);
            stage.setTitle("Book Management");
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Error", "Unable to return to the Book Management interface!");
        }
    }

    @FXML
    public void initialize() {
        // Configure table columns
        colBookId.setCellValueFactory(new PropertyValueFactory<>("bookId"));
        colBookTitle.setCellValueFactory(new PropertyValueFactory<>("bookTitle"));
        colAuthorName.setCellValueFactory(new PropertyValueFactory<>("authorName"));
        colPublisher.setCellValueFactory(new PropertyValueFactory<>("publisher"));
        colAvailableQuantity.setCellValueFactory(new PropertyValueFactory<>("availableCopies"));
        colBorrowedQuantity.setCellValueFactory(new PropertyValueFactory<>("borrowedCopies"));

        // Initialize book list
        bookList = FXCollections.observableArrayList();
        tableViewBooks.setItems(bookList);

        // Load all books on initialization
        loadFilteredBooks("SELECT * FROM `thông tin sách`");
    }

    private void loadFilteredBooks(String query) {
        bookList.clear();
        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {

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
                        return this.getAvailableCopies();
                    }

                    @Override
                    public int getBorrowedQuantity() {
                        return this.getBorrowedCopies();
                    }

                    @Override
                    public void setAvailableQuantity(int availableQuantity) {
                        this.setAvailableCopies(availableQuantity);
                    }
                });
            }

            tableViewBooks.setItems(bookList);

            // Hiển thị thông báo nếu không có sách
            if (bookList.isEmpty()) {
                showAlert(Alert.AlertType.INFORMATION, "Kết quả tìm kiếm", "Không tìm thấy sách nào phù hợp.");
            }

        } catch (SQLException e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Lỗi", "Không thể tải dữ liệu từ cơ sở dữ liệu!");
        }
    }


    private void showAlert(Alert.AlertType alertType, String title, String message) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
