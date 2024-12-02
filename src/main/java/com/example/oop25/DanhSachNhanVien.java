package com.example.oop25;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Comparator;

public class DanhSachNhanVien {

    @FXML
    private TableView<NhanVien> tableView;

    @FXML
    private TableColumn<NhanVien, String> columnMaNV;

    @FXML
    private TableColumn<NhanVien, String> columnHoTen;

    @FXML
    private TableColumn<NhanVien, String> columnNgaySinh;

    @FXML
    private TableColumn<NhanVien, String> columnDiaChi;

    @FXML
    private TableColumn<NhanVien, String> columnSDT;

    @FXML
    private TableColumn<NhanVien, String> columnCCCD;

    private ObservableList<NhanVien> data = FXCollections.observableArrayList();

    // Phương thức initialize để khởi tạo TableView và dữ liệu
    public void initialize() {
        // Liên kết các cột với thuộc tính trong lớp NhanVien
        columnMaNV.setCellValueFactory(new PropertyValueFactory<>("maNV"));
        columnHoTen.setCellValueFactory(new PropertyValueFactory<>("hoTen"));
        columnNgaySinh.setCellValueFactory(new PropertyValueFactory<>("ngaySinh"));
        columnDiaChi.setCellValueFactory(new PropertyValueFactory<>("diaChi"));
        columnSDT.setCellValueFactory(new PropertyValueFactory<>("sdt"));
        columnCCCD.setCellValueFactory(new PropertyValueFactory<>("cccd"));

        // Load dữ liệu từ MySQL
        loadDataFromDatabase();

        // Thêm sự kiện khi chọn dòng trong TableView
        tableView.setOnMouseClicked(event -> {
            NhanVien selectedNhanVien = tableView.getSelectionModel().getSelectedItem();
            if (selectedNhanVien != null) {
                openThongTinCaNhan(selectedNhanVien);
            }
        });
    }

    private void loadDataFromDatabase() {
        String url = "jdbc:mysql://localhost:3306/quanlynhanvien";
        String user = "root";
        String password = "123456789";

        try (Connection connection = DriverManager.getConnection(url, user, password);
             Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(
                     "SELECT ma_nv, ho_ten, ngay_sinh, dia_chi, sdt, cccd FROM nhanvien")) {

            while (resultSet.next()) {
                String maNV = resultSet.getString("ma_nv");
                String hoTen = resultSet.getString("ho_ten");
                String ngaySinh = resultSet.getString("ngay_sinh");
                String diaChi = resultSet.getString("dia_chi");
                String sdt = resultSet.getString("sdt");
                String cccd = resultSet.getString("cccd");

                // Thêm dữ liệu vào danh sách
                data.add(new NhanVien(maNV, hoTen, ngaySinh, diaChi, sdt, cccd));
            }

            // Sắp xếp dữ liệu theo mã nhân viên (ma_nv) tăng dần
            data.sort(Comparator.comparingInt(nv -> Integer.parseInt(nv.getMaNV())));

            // Đặt dữ liệu vào TableView
            tableView.setItems(data);

        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Lỗi kết nối hoặc truy vấn cơ sở dữ liệu!");
        }
    }

    // Mở màn hình Thông Tin Cá Nhân
    private void openThongTinCaNhan(NhanVien selectedNhanVien) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("thongtincanhan.fxml"));
            Scene scene = new Scene(fxmlLoader.load(), 1000, 600);
            Stage stage = new Stage();
            stage.setTitle("Thông Tin Cá Nhân");

            // Lấy controller của thongtincanhan.fxml và truyền thông tin nhân viên
            ThongTinCaNhan controller = fxmlLoader.getController();
            controller.setThongTin(selectedNhanVien);

            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void click_quaylai(ActionEvent actionEvent) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("canhan.fxml"));
            Scene scene = new Scene(fxmlLoader.load(), 1000, 600);
            Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
            stage.setTitle("Cá Nhân");
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}