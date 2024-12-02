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

public class Xoasach {

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
    private TextField tfISBN;

    @FXML
    private TextField tfTenSach;

    @FXML
    private TableView<Sach> tableViewBooks;

    // Kiểm tra sách có tồn tại hay không
    private boolean isBookExist(String isbn, String tenSach) {
        String sql = "SELECT COUNT(*) FROM `thông tin sách` WHERE ISBN = ? AND ten_sach = ?";
        try (Connection connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

            preparedStatement.setString(1, isbn);
            preparedStatement.setString(2, tenSach);
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

    // Kiểm tra xem sách có đang được mượn không
    private boolean isBookBorrowed(String isbn) {
        String sql = "SELECT so_luong_muon FROM `thông tin sách` WHERE ISBN = ?";
        try (Connection connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

            preparedStatement.setString(1, isbn);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                return resultSet.getInt("so_luong_muon") > 0;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Lỗi", "Không thể kiểm tra tình trạng sách trong cơ sở dữ liệu!\nChi tiết: " + e.getMessage());
        }
        return false;
    }

    // Xóa sách khỏi cơ sở dữ liệu
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

    // Sự kiện nhấn nút "Xác nhận xóa"
    @FXML
    void click_xacnhanxoa(MouseEvent event) {
        String isbn = tfISBN.getText().trim();
        String tenSach = tfTenSach.getText().trim();

        // Kiểm tra giá trị rỗng
        if (isbn.isEmpty() || tenSach.isEmpty()) {
            showAlert(Alert.AlertType.WARNING, "Lỗi", "Vui lòng điền đầy đủ thông tin!");
            return;
        }

        // Kiểm tra sách có tồn tại không
        if (!isBookExist(isbn, tenSach)) {
            showAlert(Alert.AlertType.WARNING, "Lỗi", "Sách không tồn tại trên hệ thống!");
            return;
        }

        // Kiểm tra sách đang được mượn hay không
        if (isBookBorrowed(isbn)) {
            // Hiển thị cửa sổ xác nhận
            Alert confirmAlert = new Alert(Alert.AlertType.CONFIRMATION);
            confirmAlert.setTitle("Xác nhận");
            confirmAlert.setHeaderText("Sách đang được mượn!");
            confirmAlert.setContentText("Bạn có chắc chắn muốn xóa quyển sách đang được mượn này không?");
            ButtonType buttonYes = new ButtonType("Có");
            ButtonType buttonNo = new ButtonType("Không", ButtonBar.ButtonData.CANCEL_CLOSE);
            confirmAlert.getButtonTypes().setAll(buttonYes, buttonNo);

            confirmAlert.showAndWait().ifPresent(response -> {
                if (response == buttonYes) {
                    // Xóa sách
                    deleteBook(isbn);
                    loadBooksFromDatabase();
                }
            });
        } else {
            // Xóa sách nếu không bị mượn
            deleteBook(isbn);
            loadBooksFromDatabase();
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
        colISBN.setCellValueFactory(new PropertyValueFactory<>("isbn"));
        colTenSach.setCellValueFactory(new PropertyValueFactory<>("tenSach"));
        colTenTacGia.setCellValueFactory(new PropertyValueFactory<>("tenTacGia"));
        colNXB.setCellValueFactory(new PropertyValueFactory<>("nxb"));
        colSoLuongHienCon.setCellValueFactory(new PropertyValueFactory<>("soLuongHienCon"));
        colSoLuongMuon.setCellValueFactory(new PropertyValueFactory<>("soLuongMuon"));
        // Thiết lập các cột cho bảng
        loadBooksFromDatabase();
    }
}
