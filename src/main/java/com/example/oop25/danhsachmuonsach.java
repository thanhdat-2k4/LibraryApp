package com.example.oop25;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.sql.*;
import java.util.ResourceBundle;

public class danhsachmuonsach implements Initializable {

    @FXML
    private ComboBox<String> loai_search;

    @FXML
    private TextField search;

    @FXML
    private TableColumn<Sach, String> ISBN;

    @FXML
    private TableColumn<Sach, String> NXB;

    @FXML
    private TableColumn<Sach, String> ten_sach;

    @FXML
    private TableColumn<Sach, String> ten_tac_gia;

    @FXML
    private TableColumn<Sach, Integer> so_luong_hien_con;

    @FXML
    private TableColumn<Sach, Integer> so_luong_muon;

    @FXML
    private TableView<Sach> danh_sach_muon;

    @FXML
    void click_in(MouseEvent event) {
        showAlert("Thông báo", "In danh sách", "Tính năng này chưa được triển khai.");
    }



    @FXML
    void click_thoat(MouseEvent event) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("trangchuquanlidocgia.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        Stage stage = (Stage) ((Control) event.getSource()).getScene().getWindow();
        stage.setScene(scene);
        stage.show();
    }

    @FXML
    void click_tra(MouseEvent event) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("DanhSachTraSach.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        Stage stage = (Stage) ((Control) event.getSource()).getScene().getWindow();
        stage.setScene(scene);
        stage.show();
    }

    @FXML
    void nhap_search(KeyEvent event) {
        if (event.getCode() == KeyCode.ENTER) {
            String keyword = search.getText().trim();
            String selectedSearchType = loai_search.getValue();

            // Kiểm tra đầu vào
            if (keyword.isEmpty() || selectedSearchType == null) {
                showAlert("Lỗi", "Thiếu thông tin tìm kiếm!", "Vui lòng chọn phương pháp tìm kiếm và nhập từ khóa.");
                return;
            }

            String sql;
            if (selectedSearchType.equals("Tìm kiếm theo mã độc giả ")) {
                // Tìm kiếm theo mã độc giả
                sql = """
                SELECT
                    Sach.ISBN,
                    Sach.ten_sach,
                    Sach.ten_tac_gia,
                    Sach.NXB,
                    (SELECT COUNT(*) FROM `lượt mượn` WHERE ISBN = Sach.ISBN) AS so_luong_muon,
                    (SELECT 5 - COUNT(*) FROM `lượt mượn` WHERE ISBN = Sach.ISBN) AS so_luong_hien_con
                FROM
                    `danh sách độc giả` docgia
                JOIN
                    `lượt mượn` luotmuon ON docgia.madocgia = luotmuon.madocgia
                JOIN
                    `thông tin sách` Sach ON luotmuon.ISBN = Sach.ISBN
                WHERE
                    docgia.madocgia = ?;
                """;
            } else if (selectedSearchType.equals("Tìm kiếm theo tên")) {
                // Tìm kiếm theo tên độc giả
                sql = """
                SELECT
                    Sach.ISBN,
                    Sach.ten_sach,
                    Sach.ten_tac_gia,
                    Sach.NXB,
                    (SELECT COUNT(*) FROM `lượt mượn` WHERE ISBN = Sach.ISBN) AS so_luong_muon,
                    (SELECT 5 - COUNT(*) FROM `lượt mượn` WHERE ISBN = Sach.ISBN) AS so_luong_hien_con
                FROM
                    `danh sách độc giả` docgia
                JOIN
                    `lượt mượn` luotmuon ON docgia.madocgia = luotmuon.madocgia
                JOIN
                    `thông tin sách` Sach ON luotmuon.ISBN = Sach.ISBN
                WHERE
                    docgia.ten_docgia = ?;
                """;
            } else {
                showAlert("Lỗi", "Lựa chọn không hợp lệ!", "Vui lòng chọn lại phương pháp tìm kiếm.");
                return;
            }

            // Kết nối cơ sở dữ liệu
            try (Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/library", "root", "1234");
                 PreparedStatement statement = connection.prepareStatement(sql)) {

                // Gán tham số vào truy vấn
                statement.setString(1, keyword);

                // Thực thi truy vấn
                ResultSet resultSet = statement.executeQuery();

                // Danh sách lưu trữ kết quả
                ObservableList<Sach> results = FXCollections.observableArrayList();

                while (resultSet.next()) {
                    String isbn = resultSet.getString("ISBN");
                    String tenSach = resultSet.getString("ten_sach");
                    String tenTacGia = resultSet.getString("ten_tac_gia");
                    String nxb = resultSet.getString("NXB");
                    int soLuongMuon = resultSet.getInt("so_luong_muon");
                    int soLuongHienCon = resultSet.getInt("so_luong_hien_con");

                    // Thêm vào danh sách
                    results.add(new Sach(isbn, tenSach, tenTacGia, nxb, soLuongHienCon, soLuongMuon));
                }

                // Cập nhật TableView
                if (!results.isEmpty()) {
                    danh_sach_muon.setItems(results);
                    showAlert("Thông báo", "Tìm kiếm thành công!", "Đã tìm thấy các sách liên quan.");
                } else {
                    danh_sach_muon.setItems(FXCollections.observableArrayList()); // Xóa kết quả trước
                    showAlert("Thông báo", "Không tìm thấy kết quả!", "Vui lòng kiểm tra lại từ khóa.");
                }

            } catch (SQLException e) {
                e.printStackTrace();
                showAlert("Lỗi", "Lỗi truy vấn cơ sở dữ liệu!", e.getMessage());
            }
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        // Cấu hình ComboBox
        ObservableList<String> phuongphap = FXCollections.observableArrayList(
                "Tìm kiếm theo mã độc giả ",
                "Tìm kiếm theo tên"
        );
        loai_search.setItems(phuongphap);

        // Cấu hình các cột trong TableView
        ISBN.setCellValueFactory(new PropertyValueFactory<>("isbn"));
        ten_sach.setCellValueFactory(new PropertyValueFactory<>("tenSach"));
        ten_tac_gia.setCellValueFactory(new PropertyValueFactory<>("tenTacGia"));
        NXB.setCellValueFactory(new PropertyValueFactory<>("nxb"));
        so_luong_hien_con.setCellValueFactory(new PropertyValueFactory<>("soLuongHienCon"));
        so_luong_muon.setCellValueFactory(new PropertyValueFactory<>("soLuongMuon"));
    }

    // Hiển thị thông báo
    public static void showAlert(String title, String header, String content) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(header);
        alert.setContentText(content);
        alert.showAndWait();
    }
}
