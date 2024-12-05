//// danh sach trả sách
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
//import javafx.scene.input.KeyCode;
//import javafx.scene.input.KeyEvent;
//import javafx.scene.input.MouseEvent;
//import javafx.stage.Stage;
//
//import java.io.IOException;
//import java.sql.*;
//
//public class DachSachTraSach {
//
//    @FXML
//    private TableColumn<Sach, String> ISBN;
//
//    @FXML
//    private TableColumn<Sach, String> NXB;
//
//    @FXML
//    private TableView<Sach> danh_sach_tra;
//
//    @FXML
//    private ComboBox<String> loai_search;
//
//
//    @FXML
//    private TextField search;
//
//    @FXML
//    private TableColumn<Sach, Integer> so_luong_hien_con;
//
//    @FXML
//    private TableColumn<Sach, Integer> so_luong_muon;
//
//    @FXML
//    private TableColumn<Sach, String> ten_sach;
//
//    @FXML
//    private TableColumn<Sach, String> ten_tac_gia;
//
//    @FXML
//    void click_muon(MouseEvent event) {
//        try {
//            // Quay lại màn hình chính
//            FXMLLoader loader = new FXMLLoader(getClass().getResource("danhsachmuonsach.fxml"));
//            Scene scene = new Scene(loader.load());
//            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
//            stage.setScene(scene);
//            stage.show();
//        } catch (IOException e) {
//            showError("Không thể quay lại màn hình chính: " + e.getMessage());
//        }
//    }
//
//    @FXML
//    void click_in(MouseEvent event) {
//        // Câu truy vấn SQL để lấy các sách có tình trạng "đang mượn"
//        String sql = """
//        SELECT
//            t.ISBN,
//            t.ten_sach,
//            t.ten_tac_gia,
//            t.NXB,
//            t.so_luong_hien_con,
//            t.so_luong_muon
//        FROM
//            `thông tin sách` t
//        JOIN
//            `lượt mượn` lm ON t.ISBN = lm.ISBN
//        WHERE
//            lm.tinh_trang = 'đã trả'
//        GROUP BY
//            t.ISBN, t.ten_sach, t.ten_tac_gia, t.NXB, t.so_luong_hien_con, t.so_luong_muon;
//    """;
//
//        try (Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/library", "root", "1234");
//             Statement statement = connection.createStatement();
//             ResultSet resultSet = statement.executeQuery(sql)) {
//
//            ObservableList<Sach> results = FXCollections.observableArrayList();
//
//            while (resultSet.next()) {
//                String isbn = resultSet.getString("ISBN");
//                String tenSach = resultSet.getString("ten_sach");
//                String tenTacGia = resultSet.getString("ten_tac_gia");
//                String nxb = resultSet.getString("NXB");
//                int soLuongHienCon = resultSet.getInt("so_luong_hien_con");
//                int soLuongMuon = resultSet.getInt("so_luong_muon");
//
//                results.add(new Sach(isbn, tenSach, tenTacGia, nxb, soLuongHienCon, soLuongMuon));
//            }
//
//            if (!results.isEmpty()) {
//                danh_sach_tra.setItems(results);
//                showAlert("Thông báo", "Danh sách mượn!", "Dữ liệu đã được tải lên bảng.");
//            } else {
//                danh_sach_tra.setItems(FXCollections.observableArrayList());
//                showAlert("Thông báo", "Không có dữ liệu!", "Không tìm thấy sách nào đang mượn.");
//            }
//
//        } catch (SQLException e) {
//            e.printStackTrace();
//            showAlert("Lỗi", "Không thể tải dữ liệu!", e.getMessage());
//        }
//    }
//
//
//    @FXML
//    void click_thoat(MouseEvent event) {
//        try {
//            // Quay lại màn hình chính
//            FXMLLoader loader = new FXMLLoader(getClass().getResource("QuanLiMuonTra.fxml"));
//            Scene scene = new Scene(loader.load());
//            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
//            stage.setScene(scene);
//            stage.show();
//        } catch (IOException e) {
//            showError("Không thể quay lại màn hình chính: " + e.getMessage());
//        }
//    }
//
//
//
//    @FXML
//    void nhap_search(KeyEvent event) {
//        if (event.getCode() == KeyCode.ENTER) {
//            String keyword = search.getText().trim();
//            String selectedSearchType = loai_search.getValue();
//
//            // Kiểm tra đầu vào
//            if (keyword.isEmpty() || selectedSearchType == null) {
//                showAlert("Lỗi", "Thiếu thông tin tìm kiếm!", "Vui lòng chọn phương pháp tìm kiếm và nhập từ khóa.");
//                return;
//            }
//
//            String sql = "";
//            if (selectedSearchType.equals("Tìm kiếm theo mã phiếu")) {
//                // Tìm kiếm theo mã phiếu
//                sql = """
//            SELECT
//                Sach.ISBN,
//                Sach.ten_sach,
//                Sach.ten_tac_gia,
//                Sach.NXB,
//                (SELECT COUNT(*) FROM `lượt mượn` WHERE ISBN = Sach.ISBN AND tinh_trang = 'đã trả') AS so_luong_muon,
//                (SELECT so_luong_hien_con - COUNT(*) FROM `lượt mượn` WHERE ISBN = Sach.ISBN AND tinh_trang = 'đã trả') AS so_luong_hien_con,
//                luotmuon.ma_phieu
//            FROM
//                `lượt mượn` luotmuon
//            JOIN
//                `thông tin sách` Sach ON luotmuon.ISBN = Sach.ISBN
//            WHERE
//                luotmuon.ma_phieu = ? AND luotmuon.tinh_trang = 'đã trả';
//            """;
//            } else if (selectedSearchType.equals("Tìm kiếm theo tên độc giả")) {
//                // Tìm kiếm theo tên độc giả
//                sql = """
//            SELECT
//                Sach.ISBN,
//                Sach.ten_sach,
//                Sach.ten_tac_gia,
//                Sach.NXB,
//                (SELECT COUNT(*) FROM `lượt mượn` WHERE ISBN = Sach.ISBN AND tinh_trang = 'đã trả') AS so_luong_muon,
//                (SELECT so_luong_hien_con - COUNT(*) FROM `lượt mượn` WHERE ISBN = Sach.ISBN AND tinh_trang = 'đã trả') AS so_luong_hien_con,
//                luotmuon.ma_phieu
//            FROM
//                `danh sách độc giả` docgia
//            JOIN
//                `lượt mượn` luotmuon ON docgia.madocgia = luotmuon.madocgia
//            JOIN
//                `thông tin sách` Sach ON luotmuon.ISBN = Sach.ISBN
//            WHERE
//                docgia.ten_docgia = ? AND luotmuon.tinh_trang = 'đã trả';
//            """;
//            } else {
//                showAlert("Lỗi", "Lựa chọn không hợp lệ!", "Vui lòng chọn lại phương pháp tìm kiếm.");
//                return;
//            }
//
//            // Kết nối cơ sở dữ liệu
//            try (Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/library", "root", "1234");
//                 PreparedStatement statement = connection.prepareStatement(sql)) {
//
//                // Gán tham số vào truy vấn
//                statement.setString(1, keyword);
//
//                // Thực thi truy vấn
//                ResultSet resultSet = statement.executeQuery();
//
//                // Danh sách lưu trữ kết quả
//                ObservableList<Sach> results = FXCollections.observableArrayList();
//
//                while (resultSet.next()) {
//                    String isbn = resultSet.getString("ISBN");
//                    String tenSach = resultSet.getString("ten_sach");
//                    String tenTacGia = resultSet.getString("ten_tac_gia");
//                    String nxb = resultSet.getString("NXB");
//                    int soLuongMuon = resultSet.getInt("so_luong_muon");
//                    int soLuongHienCon = resultSet.getInt("so_luong_hien_con");
//
//                    // Thêm vào danh sách
//                    results.add(new Sach(isbn, tenSach, tenTacGia, nxb, soLuongHienCon, soLuongMuon));
//                }
//
//                // Cập nhật TableView
//                if (!results.isEmpty()) {
//                    danh_sach_tra.setItems(results);
//                    showAlert("Thông báo", "Tìm kiếm thành công!", "Đã tìm thấy các sách liên quan.");
//                } else {
//                    danh_sach_tra.setItems(FXCollections.observableArrayList()); // Xóa kết quả trước
//                    showAlert("Thông báo", "Không tìm thấy kết quả!", "Vui lòng kiểm tra lại từ khóa.");
//                }
//
//            } catch (SQLException e) {
//                e.printStackTrace();
//                showAlert("Lỗi", "Lỗi truy vấn cơ sở dữ liệu!", e.getMessage());
//            }
//        }
//    }
//
//
//    @FXML
//    public void initialize() {
//        // Cấu hình các cột của TableView
//        ISBN.setCellValueFactory(new PropertyValueFactory<>("isbn"));
//        ten_sach.setCellValueFactory(new PropertyValueFactory<>("tenSach"));
//        ten_tac_gia.setCellValueFactory(new PropertyValueFactory<>("tenTacGia"));
//        NXB.setCellValueFactory(new PropertyValueFactory<>("nxb"));
//        so_luong_hien_con.setCellValueFactory(new PropertyValueFactory<>("soLuongHienCon"));
//        so_luong_muon.setCellValueFactory(new PropertyValueFactory<>("soLuongMuon"));
//
//
//        // Cấu hình danh sách các lựa chọn cho ComboBox
//        loai_search.setItems(FXCollections.observableArrayList("Tìm kiếm theo mã phiếu", "Tìm kiếm theo ISBN"));
//    }
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
//
//    // Hiển thị thông báo
//    private void showAlert(String title, String header, String content) {
//        Alert alert = new Alert(Alert.AlertType.INFORMATION);
//        alert.setTitle(title);
//        alert.setHeaderText(header);
//        alert.setContentText(content);
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
            FXMLLoader loader = new FXMLLoader(getClass().getResource("LoanManagement.fxml"));
            Scene scene = new Scene(loader.load());
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            showError("Không thể chuyển đến màn hình chính: " + e.getMessage());
        }
    }

    @FXML
    void handleSearch(KeyEvent event) {
        if (event.getCode() == KeyCode.ENTER) {
            String keyword = search.getText().trim();
            String selectedType = searchType.getValue();

            if (keyword.isEmpty() || selectedType == null) {
                showAlert("Lỗi", "Thông tin tìm kiếm thiếu", "Vui lòng chọn phương thức tìm kiếm và nhập từ khóa.");
                return;
            }

            String sql;
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
            } else {
                showAlert("Lỗi", "Lựa chọn không hợp lệ", "Vui lòng chọn phương thức tìm kiếm hợp lệ.");
                return;
            }

            try (Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/library", "root", "1234");
                 PreparedStatement statement = connection.prepareStatement(sql)) {

                statement.setString(1, keyword);
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

        searchType.setItems(FXCollections.observableArrayList("Tìm kiếm theo mã phiếu", "Tìm kiếm theo tên độc giả"));
    }

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
