//// danh sach trả sách

package com.example.oop25;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.*;

public class ReturnBookList {

    @FXML
    private TableColumn<Book, String> bookId;

    @FXML
    private TableColumn<Book, String> publisher;

    @FXML
    private TableView<Book> returnBookTable;

    @FXML
    private ComboBox<String> searchType;

    @FXML
    private TextField search;

    @FXML
    private TableColumn<Book, Integer> availableCopies;

    @FXML
    private TableColumn<Book, Integer> borrowedCopies;

    @FXML
    private TableColumn<Book, String> bookTitle;

    @FXML
    private TableColumn<Book, String> authorName;

    @FXML
    void handleBorrowClick(MouseEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("danhsachmuonsach.fxml"));
            Scene scene = new Scene(loader.load());
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            showError("Không thể chuyển đến màn hình chính: " + e.getMessage());
        }
    }

    @FXML
    void handleLoadDataClick(MouseEvent event) {
        String sql = """
        SELECT 
            t.ISBN AS bookId, 
            t.ten_sach AS bookTitle, 
            t.ten_tac_gia AS authorName, 
            t.NXB AS publisher, 
            t.so_luong_hien_con AS availableCopies, 
            t.so_luong_muon AS borrowedCopies
        FROM 
            `thông tin sách` t
        JOIN 
            `lượt mượn` lm ON t.ISBN = lm.ISBN
        WHERE 
            lm.tinh_trang = 'đã trả'
        GROUP BY 
            t.ISBN, t.ten_sach, t.ten_tac_gia, t.NXB, t.so_luong_hien_con, t.so_luong_muon;
        """;

        try (Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/library", "root", "1234");
             Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(sql)) {

            ObservableList<Book> results = FXCollections.observableArrayList();

            while (resultSet.next()) {
                String id = resultSet.getString("bookId");
                String title = resultSet.getString("bookTitle");
                String author = resultSet.getString("authorName");
                String pub = resultSet.getString("publisher");
                int available = resultSet.getInt("availableCopies");
                int borrowed = resultSet.getInt("borrowedCopies");

                results.add(new Book(id, title, author, pub, available, borrowed) {
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

            if (!results.isEmpty()) {
                returnBookTable.setItems(results);
                showAlert("Thông báo", "Dữ liệu đã được tải", "Dữ liệu đã được tải vào bảng.");
            } else {
                returnBookTable.setItems(FXCollections.observableArrayList());
                showAlert("Thông báo", "Không có dữ liệu", "Không tìm thấy sách đã trả.");
            }

        } catch (SQLException e) {
            e.printStackTrace();
            showAlert("Lỗi", "Lỗi tải dữ liệu", e.getMessage());
        }
    }

    @FXML
    void handleExitClick(MouseEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("quanlimuontra.fxml"));
            Scene scene = new Scene(loader.load());
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            showError("Không thể chuyển đến màn hình chính: " + e.getMessage());
        }
    }

//    @FXML
//    void handleSearch(KeyEvent event) {
//        if (event.getCode() == KeyCode.ENTER) {
//            String keyword = search.getText().trim();
//            String selectedType = searchType.getValue();
//
//            if (keyword.isEmpty() || selectedType == null) {
//                showAlert("Lỗi", "Thông tin tìm kiếm thiếu", "Vui lòng chọn phương thức tìm kiếm và nhập từ khóa.");
//                return;
//            }
//
//            String sql;
//            if (selectedType.equals("Tìm kiếm theo mã phiếu")) {
//                sql = """
//                SELECT
//                    b.ISBN AS bookId,
//                    b.ten_sach AS bookTitle,
//                    b.ten_tac_gia AS authorName,
//                    b.NXB AS publisher,
//                    COUNT(*) AS borrowedCopies,
//                    (b.so_luong_hien_con - COUNT(*)) AS availableCopies
//                FROM
//                    `thông tin sách` b
//                JOIN
//                    `lượt mượn` lm ON b.ISBN = lm.ISBN
//                WHERE
//                    lm.ma_phieu = ? AND lm.tinh_trang = 'đã trả'
//                GROUP BY
//                    b.ISBN;
//                """;
//            } else if (selectedType.equals("Tìm kiếm theo tên độc giả")) {
//                sql = """
//                SELECT
//                    b.ISBN AS bookId,
//                    b.ten_sach AS bookTitle,
//                    b.ten_tac_gia AS authorName,
//                    b.NXB AS publisher,
//                    COUNT(*) AS borrowedCopies,
//                    (b.so_luong_hien_con - COUNT(*)) AS availableCopies
//                FROM
//                    `thông tin sách` b
//                JOIN
//                    `lượt mượn` lm ON b.ISBN = lm.ISBN
//                JOIN
//                    `danh sách độc giả` dg ON dg.madocgia = lm.madocgia
//                WHERE
//                    dg.ten_docgia = ? AND lm.tinh_trang = 'đã trả'
//                GROUP BY
//                    b.ISBN;
//                """;
//            } else {
//                showAlert("Lỗi", "Lựa chọn không hợp lệ", "Vui lòng chọn phương thức tìm kiếm hợp lệ.");
//                return;
//            }
//
//            try (Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/library", "root", "1234");
//                 PreparedStatement statement = connection.prepareStatement(sql)) {
//
//                statement.setString(1, keyword);
//                ResultSet resultSet = statement.executeQuery();
//
//                ObservableList<Book> results = FXCollections.observableArrayList();
//
//                while (resultSet.next()) {
//                    String id = resultSet.getString("bookId");
//                    String title = resultSet.getString("bookTitle");
//                    String author = resultSet.getString("authorName");
//                    String pub = resultSet.getString("publisher");
//                    int available = resultSet.getInt("availableCopies");
//                    int borrowed = resultSet.getInt("borrowedCopies");
//
//                    results.add(new Book(id, title, author, pub, available, borrowed) {
//                        @Override
//                        public int getAvailableQuantity() {
//                            return 0;
//                        }
//
//                        @Override
//                        public int getBorrowedQuantity() {
//                            return 0;
//                        }
//
//                        @Override
//                        public void setAvailableQuantity(int availableQuantity) {
//
//                        }
//                    });
//                }
//
//                if (!results.isEmpty()) {
//                    returnBookTable.setItems(results);
//                    showAlert("Thông báo", "Tìm kiếm thành công", "Đã tìm thấy sách phù hợp.");
//                } else {
//                    returnBookTable.setItems(FXCollections.observableArrayList());
//                    showAlert("Thông báo", "Không có kết quả", "Không có sách phù hợp với tiêu chí tìm kiếm.");
//                }
//
//            } catch (SQLException e) {
//                e.printStackTrace();
//                showAlert("Lỗi", "Lỗi truy vấn cơ sở dữ liệu", e.getMessage());
//            }
//        }
//    }

    @FXML
    void handleSearch(KeyEvent event) {
        if (event.getCode() == KeyCode.ENTER) {
            String keyword = search.getText().trim();
            String selectedType = searchType.getValue();

            if (keyword.isEmpty() || selectedType == null) {
                showAlert("Lỗi", "Thông tin tìm kiếm thiếu", "Vui lòng chọn phương thức tìm kiếm và nhập từ khóa.");
                return;
            }

            String sql = "";
            if (selectedType.equals("Tìm kiếm theo mã phiếu")) {
                sql = """
            SELECT
                b.ISBN AS bookId,
                b.ten_sach AS bookTitle,
                b.ten_tac_gia AS authorName,
                b.NXB AS publisher,
                COUNT(*) AS borrowedCopies,
                (b.so_luong_hien_con - COUNT(*)) AS availableCopies
            FROM
                `thông tin sách` b
            JOIN
                `lượt mượn` lm ON b.ISBN = lm.ISBN
            WHERE
                lm.ma_phieu = ? AND lm.tinh_trang = 'đã trả'
            GROUP BY
                b.ISBN;
            """;
            } else if (selectedType.equals("Tìm kiếm theo tên độc giả")) {
                sql = """
            SELECT
                b.ISBN AS bookId,
                b.ten_sach AS bookTitle,
                b.ten_tac_gia AS authorName,
                b.NXB AS publisher,
                COUNT(*) AS borrowedCopies,
                (b.so_luong_hien_con - COUNT(*)) AS availableCopies
            FROM
                `thông tin sách` b
            JOIN
                `lượt mượn` lm ON b.ISBN = lm.ISBN
            JOIN
                `danh sách độc giả` dg ON dg.madocgia = lm.madocgia
            WHERE
                dg.ten_docgia = ? AND lm.tinh_trang = 'đã trả'
            GROUP BY
                b.ISBN;
            """;
            } else if (selectedType.equals("Tìm kiếm theo mã độc giả")) {
                sql = """
            SELECT
                b.ISBN AS bookId,
                b.ten_sach AS bookTitle,
                b.ten_tac_gia AS authorName,
                b.NXB AS publisher,
                COUNT(*) AS borrowedCopies,
                (b.so_luong_hien_con - COUNT(*)) AS availableCopies
            FROM
                `thông tin sách` b
            JOIN
                `lượt mượn` lm ON b.ISBN = lm.ISBN
            WHERE
                lm.madocgia = ? AND lm.tinh_trang = 'đã trả'
            GROUP BY
                b.ISBN;
            """;
            } else {
                showAlert("Lỗi", "Lựa chọn không hợp lệ", "Vui lòng chọn phương thức tìm kiếm hợp lệ.");
                return;
            }

            try (Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/library", "root", "1234");
                 PreparedStatement statement = connection.prepareStatement(sql)) {

                statement.setString(1, keyword);  // Đặt giá trị tìm kiếm cho câu lệnh SQL
                ResultSet resultSet = statement.executeQuery();

                ObservableList<Book> results = FXCollections.observableArrayList();

                while (resultSet.next()) {
                    String id = resultSet.getString("bookId");
                    String title = resultSet.getString("bookTitle");
                    String author = resultSet.getString("authorName");
                    String pub = resultSet.getString("publisher");
                    int available = resultSet.getInt("availableCopies");
                    int borrowed = resultSet.getInt("borrowedCopies");

                    results.add(new Book(id, title, author, pub, available, borrowed) {
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

                if (!results.isEmpty()) {
                    returnBookTable.setItems(results);
                    showAlert("Thông báo", "Tìm kiếm thành công", "Đã tìm thấy sách phù hợp.");
                } else {
                    returnBookTable.setItems(FXCollections.observableArrayList());
                    showAlert("Thông báo", "Không có kết quả", "Không có sách phù hợp với tiêu chí tìm kiếm.");
                }

            } catch (SQLException e) {
                e.printStackTrace();
                showAlert("Lỗi", "Lỗi truy vấn cơ sở dữ liệu", e.getMessage());
            }
        }
    }

    @FXML
    public void initialize() {
        bookId.setCellValueFactory(new PropertyValueFactory<>("bookId"));
        bookTitle.setCellValueFactory(new PropertyValueFactory<>("bookTitle"));
        authorName.setCellValueFactory(new PropertyValueFactory<>("authorName"));
        publisher.setCellValueFactory(new PropertyValueFactory<>("publisher"));
        availableCopies.setCellValueFactory(new PropertyValueFactory<>("availableCopies"));
        borrowedCopies.setCellValueFactory(new PropertyValueFactory<>("borrowedCopies"));

        // Thêm lựa chọn mới vào ComboBox
        searchType.setItems(FXCollections.observableArrayList(
                "Tìm kiếm theo mã phiếu",
                "Tìm kiếm theo tên độc giả",
                "Tìm kiếm theo mã độc giả"  // Thêm lựa chọn này
        ));
    }


//    @FXML
//    public void initialize() {
//        bookId.setCellValueFactory(new PropertyValueFactory<>("bookId"));
//        bookTitle.setCellValueFactory(new PropertyValueFactory<>("bookTitle"));
//        authorName.setCellValueFactory(new PropertyValueFactory<>("authorName"));
//        publisher.setCellValueFactory(new PropertyValueFactory<>("publisher"));
//        availableCopies.setCellValueFactory(new PropertyValueFactory<>("availableCopies"));
//        borrowedCopies.setCellValueFactory(new PropertyValueFactory<>("borrowedCopies"));
//
//        searchType.setItems(FXCollections.observableArrayList("Tìm kiếm theo mã phiếu", "Tìm kiếm theo tên độc giả"));
//    }

    private void showError(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Lỗi");
        alert.setContentText(message);
        alert.showAndWait();
    }

    private void showAlert(String title, String header, String content) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(header);
        alert.setContentText(content);
        alert.showAndWait();
    }
}
