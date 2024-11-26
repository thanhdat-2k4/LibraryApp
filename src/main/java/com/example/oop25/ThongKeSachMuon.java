package com.example.oop25;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.*;
import java.time.Year;
import java.util.stream.IntStream;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.util.Duration;

public class ThongKeSachMuon {

    @FXML
    private MenuButton menuNam, menuThang, menuNgay;

    @FXML
    private TableView<SachMuon> tableView;

    @FXML
    private TableColumn<SachMuon, Integer> colSTT;
    @FXML
    private TableColumn<SachMuon, String> colMaSach;
    @FXML
    private TableColumn<SachMuon, String> colTenSach;
    @FXML
    private TableColumn<SachMuon, Integer> colSoLuongMuon;

    @FXML
    private Button clickXemDanhSach, clickQuayLai;

    @FXML
    private Label messageLabel; // Thêm Label thông báo lỗi

    private String selectedYear, selectedMonth, selectedDay;
    private Connection connection;

    public ThongKeSachMuon() {
        try {
            connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/thongkesach", "root", "123456789");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void initialize() {
        populateMenuButtons();
        setupTableView();
    }

    private void populateMenuButtons() {
        // Thêm năm
        IntStream.range(2020, Year.now().getValue() + 1).forEach(year -> {
            MenuItem item = new MenuItem(String.valueOf(year));
            item.setOnAction(event -> {
                selectedYear = item.getText();
                menuNam.setText(item.getText());
            });
            menuNam.getItems().add(item);
        });

        // Thêm tháng
        IntStream.rangeClosed(1, 12).forEach(month -> {
            MenuItem item = new MenuItem(String.valueOf(month));
            item.setOnAction(event -> {
                selectedMonth = item.getText();
                menuThang.setText(item.getText());
            });
            menuThang.getItems().add(item);
        });

        // Thêm ngày
        IntStream.rangeClosed(1, 31).forEach(day -> {
            MenuItem item = new MenuItem(String.valueOf(day));
            item.setOnAction(event -> {
                selectedDay = item.getText();
                menuNgay.setText(item.getText());
            });
            menuNgay.getItems().add(item);
        });
    }

    private void setupTableView() {
        colSTT.setCellValueFactory(new PropertyValueFactory<>("stt"));
        colMaSach.setCellValueFactory(new PropertyValueFactory<>("maSach"));
        colTenSach.setCellValueFactory(new PropertyValueFactory<>("tenSach"));
        colSoLuongMuon.setCellValueFactory(new PropertyValueFactory<>("soLuongMuon"));
    }

    private void loadTableData() {
        ObservableList<SachMuon> data = FXCollections.observableArrayList();
        String query = "SELECT * FROM SachMuon WHERE 1=1";

        // Thêm điều kiện lọc nếu có năm, tháng, ngày được chọn
        if (selectedYear != null && !selectedYear.isEmpty()) {
            query += " AND YEAR(ngay_muon) = ?";
        }
        if (selectedMonth != null && !selectedMonth.isEmpty()) {
            query += " AND MONTH(ngay_muon) = ?";
        }
        if (selectedDay != null && !selectedDay.isEmpty()) {
            query += " AND DAY(ngay_muon) = ?";
        }

        try (PreparedStatement pstmt = connection.prepareStatement(query)) {
            int index = 1;

            // Set giá trị cho các tham số trong PreparedStatement
            if (selectedYear != null && !selectedYear.isEmpty()) {
                pstmt.setString(index++, selectedYear);
            }
            if (selectedMonth != null && !selectedMonth.isEmpty()) {
                pstmt.setString(index++, selectedMonth);
            }
            if (selectedDay != null && !selectedDay.isEmpty()) {
                pstmt.setString(index++, selectedDay);
            }

            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                data.add(new SachMuon(
                        rs.getInt("stt"),
                        rs.getString("ma_sach"),
                        rs.getString("ten_sach"),
                        rs.getInt("so_luong_muon")
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        tableView.setItems(data);
    }

    // Sự kiện khi bấm nút "Xem Danh Sách"
    @FXML
    private void click_xemdanhsach(ActionEvent event) {
        // Kiểm tra nếu có MenuButton nào chưa được chọn
        if (selectedYear == null || selectedYear.isEmpty()) {
            messageLabel.setText("Vui lòng chọn năm.");
            showMessageForDuration();
        } else if (selectedMonth == null || selectedMonth.isEmpty()) {
            messageLabel.setText("Vui lòng chọn tháng.");
            showMessageForDuration();
        } else if (selectedDay == null || selectedDay.isEmpty()) {
            messageLabel.setText("Vui lòng chọn ngày.");
            showMessageForDuration();
        } else {
            messageLabel.setText(""); // Xóa thông báo lỗi
            loadTableData(); // Nếu tất cả được chọn, tải dữ liệu
        }
    }

    private void showMessageForDuration() {
        // Hiển thị messageLabel
        messageLabel.setVisible(true);

        // Tạo một Timeline để ẩn messageLabel sau 3 giây
        Timeline timeline = new Timeline(
                new KeyFrame(Duration.seconds(3), e -> messageLabel.setVisible(false))
        );
        timeline.setCycleCount(1); // Chạy 1 lần
        timeline.play(); // Bắt đầu Timeline
    }

    // Sự kiện khi bấm nút "Quay Lại"
    @FXML
    private void click_quaylai(ActionEvent event) {
        try {
            // Quay lại trang thongke.fxml
            FXMLLoader loader = new FXMLLoader(getClass().getResource("thongke.fxml"));
            Parent root = loader.load();
            Scene scene = new Scene(root);
            Stage stage = (Stage) clickQuayLai.getScene().getWindow();
            stage.setScene(scene);
            stage.setTitle("Thống Kê");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}