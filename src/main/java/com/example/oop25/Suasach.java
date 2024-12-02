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

public class Suasach {

    // Thông tin kết nối cơ sở dữ liệu
    private static final String DB_URL = "jdbc:mysql://localhost:3306/library";
    private static final String DB_USER = "root";
    private static final String DB_PASSWORD = "1234";

    @FXML
    private TableColumn<Sach, String> colISBN;

    @FXML
    private TableColumn<Sach, String> colNXB;

    @FXML
    private TableColumn<Sach, Integer> colSoLuongHienCon;

    @FXML
    private TableColumn<Sach, Integer> colSoLuongMuon;

    @FXML
    private TableColumn<Sach, String> colTenSach;

    @FXML
    private TableColumn<Sach, String> colTenTacGia;

    @FXML
    private TableView<Sach> tableViewBooks;

    @FXML
    private TextField tfISBN;

    @FXML
    private TextField tfNXB;

    @FXML
    private TextField tfSoLuongHienCon;

    @FXML
    private TextField tfSoLuongMuon;

    @FXML
    private TextField tfTenSach;

    @FXML
    private TextField tfTenTacGia;

    // Kiểm tra xem sách có tồn tại trong cơ sở dữ liệu hay không
    private boolean isBookExist(String isbn) {
        String sql = "SELECT COUNT(*) FROM `thông tin sách` WHERE ISBN = ?";
        try (Connection connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

            preparedStatement.setString(1, isbn);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                return resultSet.getInt(1) > 0; // Trả về true nếu sách tồn tại
            }
        } catch (SQLException e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Lỗi", "Không thể kiểm tra sách trong cơ sở dữ liệu!\nChi tiết: " + e.getMessage());
        }
        return false;
    }

    // Cập nhật thông tin sách trong cơ sở dữ liệu
    private void updateBook(String isbn, String tenSach, String tenTacGia, String nxb, int soLuongHienCon, int soLuongMuon) {
        String sql = "UPDATE `thông tin sách` SET ten_sach = ?, ten_tac_gia = ?, NXB = ?, so_luong_hien_con = ?, so_luong_muon = ? WHERE ISBN = ?";

        try (Connection connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

            preparedStatement.setString(1, tenSach);
            preparedStatement.setString(2, tenTacGia);
            preparedStatement.setString(3, nxb);
            preparedStatement.setInt(4, soLuongHienCon);
            preparedStatement.setInt(5, soLuongMuon);
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

    // Sự kiện nhấn nút "Xác nhận sửa"
    @FXML
    void click_xacnhansua(MouseEvent event) {
        try {
            // Lấy dữ liệu từ các trường nhập liệu
            String isbn = tfISBN.getText().trim();
            String tenSach = tfTenSach.getText().trim();
            String tenTacGia = tfTenTacGia.getText().trim();
            String nxb = tfNXB.getText().trim();
            String soLuongHienConStr = tfSoLuongHienCon.getText().trim();
            String soLuongMuonStr = tfSoLuongMuon.getText().trim();

            // Kiểm tra giá trị rỗng
            if (isbn.isEmpty() || tenSach.isEmpty() || tenTacGia.isEmpty() || nxb.isEmpty() || soLuongHienConStr.isEmpty() || soLuongMuonStr.isEmpty()) {
                showAlert(Alert.AlertType.WARNING, "Lỗi", "Vui lòng điền đầy đủ thông tin!");
                return;
            }

            // Chuyển đổi số lượng sang kiểu int
            int soLuongHienCon = Integer.parseInt(soLuongHienConStr);
            int soLuongMuon = Integer.parseInt(soLuongMuonStr);

            // Kiểm tra xem sách có tồn tại hay không
            if (!isBookExist(isbn)) {
                showAlert(Alert.AlertType.WARNING, "Thất bại", "Sách không tồn tại trên hệ thống!");
                return;
            }

            // Cập nhật thông tin sách
            updateBook(isbn, tenSach, tenTacGia, nxb, soLuongHienCon, soLuongMuon);

            // Làm mới danh sách trên giao diện
            loadBooksFromDatabase();

        } catch (NumberFormatException e) {
            showAlert(Alert.AlertType.ERROR, "Lỗi", "Số lượng phải là một số hợp lệ!");
        } catch (Exception e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Lỗi", "Có lỗi xảy ra khi cập nhật sách: " + e.getMessage());
        }
    }

    // Tải danh sách sách từ cơ sở dữ liệu
    private void loadBooksFromDatabase() {
        ObservableList<Sach> bookList = FXCollections.observableArrayList();
        String query = "SELECT * FROM `thông tin sách`";

        try (Connection connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
             Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(query)) {

            while (resultSet.next()) {
                String isbn = resultSet.getString("ISBN");
                String tenSach = resultSet.getString("ten_sach");
                String tenTacGia = resultSet.getString("ten_tac_gia");
                String nxb = resultSet.getString("NXB");
                int soLuongHienCon = resultSet.getInt("so_luong_hien_con");
                int soLuongMuon = resultSet.getInt("so_luong_muon");

                bookList.add(new Sach(isbn, tenSach, tenTacGia, nxb, soLuongHienCon, soLuongMuon));
            }

            tableViewBooks.setItems(bookList);

        } catch (SQLException e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Lỗi", "Không thể tải danh sách sách từ cơ sở dữ liệu!");
        }
    }

    // Quay lại giao diện quản lý sách
    @FXML
    void click_quaylaiquanlysach(MouseEvent event) {
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

    // Hiển thị thông báo
    private void showAlert(Alert.AlertType alertType, String title, String message) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setContentText(message);
        alert.showAndWait();
    }

    // Khởi tạo bảng và tải dữ liệu
    @FXML
    public void initialize() {
        colISBN.setCellValueFactory(data -> data.getValue().isbnProperty());
        colTenSach.setCellValueFactory(data -> data.getValue().tenSachProperty());
        colTenTacGia.setCellValueFactory(data -> data.getValue().tenTacGiaProperty());
        colNXB.setCellValueFactory(data -> data.getValue().nxbProperty());
        colSoLuongHienCon.setCellValueFactory(data -> data.getValue().soLuongHienConProperty().asObject());
        colSoLuongMuon.setCellValueFactory(data -> data.getValue().soLuongMuonProperty().asObject());

        loadBooksFromDatabase();
    }
}
