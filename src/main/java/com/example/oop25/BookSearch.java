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
    private TextField txtMaSach;

    @FXML
    private TextField txtTenSach;

    @FXML
    private TextField txtTenTacGia;

    @FXML
    private TextField txtNXB;

    @FXML
    private TextField txtSoLuongHienCon;

    @FXML
    private TextField txtSoLuongMuon;

    @FXML
    private TableView<Sach> tableViewBooks;

    @FXML
    private TableColumn<Sach, String> colMaSach;

    @FXML
    private TableColumn<Sach, String> colTenSach;

    @FXML
    private TableColumn<Sach, String> colTenTacGia;

    @FXML
    private TableColumn<Sach, String> colNXB;

    @FXML
    private TableColumn<Sach, Integer> colSoLuongHienCon;

    @FXML
    private TableColumn<Sach, Integer> colSoLuongMuon;

    private ObservableList<Sach> bookList;

    private final String DB_URL = "jdbc:mysql://localhost:3306/library";
    private final String DB_USER = "root";
    private final String DB_PASSWORD = "1234";

    @FXML
    void click_loc(MouseEvent event) {
        // Lấy thông tin từ các TextField
        String maSach = txtMaSach.getText().trim();
        String tenSach = txtTenSach.getText().trim();
        String tenTacGia = txtTenTacGia.getText().trim();
        String nxb = txtNXB.getText().trim();
        String soLuongHienCon = txtSoLuongHienCon.getText().trim();
        String soLuongMuon = txtSoLuongMuon.getText().trim();

        // Xây dựng câu truy vấn SQL động
        String query = "SELECT * FROM `thông tin sách` WHERE 1=1";
        if (!maSach.isEmpty()) query += " AND ISBN LIKE '%" + maSach + "%'";
        if (!tenSach.isEmpty()) query += " AND ten_sach LIKE '%" + tenSach + "%'";
        if (!tenTacGia.isEmpty()) query += " AND ten_tac_gia LIKE '%" + tenTacGia + "%'";
        if (!nxb.isEmpty()) query += " AND NXB LIKE '%" + nxb + "%'";
        if (!soLuongHienCon.isEmpty()) query += " AND so_luong_hien_con = " + soLuongHienCon;
        if (!soLuongMuon.isEmpty()) query += " AND so_luong_muon = " + soLuongMuon;

        // Tải dữ liệu từ cơ sở dữ liệu dựa trên điều kiện
        loadFilteredBooks(query);
    }

    @FXML
    void click_quaylaiquanlysach(MouseEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("Quanlysach.fxml"));
            Node source = (Node) event.getSource();
            Stage stage = (Stage) source.getScene().getWindow();
            Scene scene = new Scene(loader.load());
            stage.setScene(scene);
            stage.setTitle("Quản lý sách");
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Lỗi", "Không thể quay lại giao diện Quản lý sách!");
        }
    }

    @FXML
    public void initialize() {
        // Cấu hình cột bảng
        colMaSach.setCellValueFactory(new PropertyValueFactory<>("isbn"));
        colTenSach.setCellValueFactory(new PropertyValueFactory<>("tenSach"));
        colTenTacGia.setCellValueFactory(new PropertyValueFactory<>("tenTacGia"));
        colNXB.setCellValueFactory(new PropertyValueFactory<>("nxb"));
        colSoLuongHienCon.setCellValueFactory(new PropertyValueFactory<>("soLuongHienCon"));
        colSoLuongMuon.setCellValueFactory(new PropertyValueFactory<>("soLuongMuon"));

        // Khởi tạo danh sách
        bookList = FXCollections.observableArrayList();
        tableViewBooks.setItems(bookList);
    }

    private void loadFilteredBooks(String query) {
        bookList.clear();
        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {

            while (rs.next()) {
                bookList.add(new Sach(
                        rs.getString("ISBN"),
                        rs.getString("ten_sach"),
                        rs.getString("ten_tac_gia"),
                        rs.getString("NXB"),
                        rs.getInt("so_luong_hien_con"),
                        rs.getInt("so_luong_muon")
                ));
            }

            tableViewBooks.setItems(bookList);

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
