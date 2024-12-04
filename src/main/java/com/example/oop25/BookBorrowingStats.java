////thong ke danh sach muon

package com.example.oop25;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class BookBorrowingStats {

    @FXML
    private TableColumn<Sach, String> ISBN;  // Sử dụng Sach thay vì BookBorrowingStatsData

    @FXML
    private TableColumn<Sach, String> bookTitle;

    @FXML
    private TableColumn<Sach, Integer> borrowedQuantity;

    @FXML
    private TableColumn<Sach, Integer> totalBooks;

    @FXML
    private MenuButton menuNam;

    @FXML
    private MenuButton menuNgay;

    @FXML
    private MenuButton menuThang;

    @FXML
    private TableColumn<Sach, Integer> orderNumber;

    @FXML
    private TableView<Sach> tableView;  // Sử dụng Sach thay vì BookBorrowingStatsData

    private ObservableList<Sach> dataList;
    private Connection connection;

    private String selectedYear, selectedMonth, selectedDay;

    @FXML
    void initialize() {
        setupTableView();
        setupDatabaseConnection();
        populateMenuButtons();
    }

    @FXML
    void goBack(MouseEvent event) throws IOException {
        // về trang chủ qua lí ng dùng.
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("thongke.fxml"));
        Scene scene = new Scene(fxmlLoader.load() );
        Stage stage = new Stage();

        stage.setScene(scene);
        stage.show();
        // xóa khung

        ((Node)(event.getSource())).getScene().getWindow().hide();
    }

    @FXML
    void viewList(MouseEvent event) {
        if (!validateSelections()) {
            showAlert("Vui lòng chọn đầy đủ năm, tháng và ngày.");
            return;
        }
        loadTableData();
    }

    private void setupTableView() {
        // Cột ISBN
        ISBN.setCellValueFactory(cellData -> cellData.getValue().isbnProperty());
        // Cột Tên sách
        bookTitle.setCellValueFactory(cellData -> cellData.getValue().tenSachProperty());
        // Cột Số lượng mượn
        borrowedQuantity.setCellValueFactory(cellData -> cellData.getValue().soLuongMuonProperty().asObject());
        // Cột Tổng số sách (sử dụng IntegerProperty cho phép binding vào TableView)
        totalBooks.setCellValueFactory(cellData ->
                new SimpleIntegerProperty(cellData.getValue().getSoLuongHienCon() + cellData.getValue().getSoLuongMuon()).asObject());
        // Cột Số thứ tự
        orderNumber.setCellValueFactory(cellData ->
                new SimpleIntegerProperty(dataList.indexOf(cellData.getValue()) + 1).asObject());

        // Tạo ObservableList cho TableView
        dataList = FXCollections.observableArrayList();
        tableView.setItems(dataList);
    }

    private void setupDatabaseConnection() {
        try {
            connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/library", "root", "1234");
            System.out.println("Kết nối cơ sở dữ liệu thành công!");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void populateMenuButtons() {
        // Populate years
        for (int year = 2010; year <= 2024; year++) {
            MenuItem yearItem = new MenuItem(String.valueOf(year));
            yearItem.setOnAction(event -> {
                selectedYear = yearItem.getText();
                menuNam.setText(selectedYear);
            });
            menuNam.getItems().add(yearItem);
        }

        // Populate months
        for (int month = 1; month <= 12; month++) {
            MenuItem monthItem = new MenuItem(String.format("%02d", month));
            monthItem.setOnAction(event -> {
                selectedMonth = monthItem.getText();
                menuThang.setText(selectedMonth);
            });
            menuThang.getItems().add(monthItem);
        }

        // Populate days
        for (int day = 1; day <= 31; day++) {
            MenuItem dayItem = new MenuItem(String.format("%02d", day));
            dayItem.setOnAction(event -> {
                selectedDay = dayItem.getText();
                menuNgay.setText(selectedDay);
            });
            menuNgay.getItems().add(dayItem);
        }
    }

    private boolean validateSelections() {
        return selectedYear != null && selectedMonth != null && selectedDay != null;
    }

    private void loadTableData() {
        dataList.clear();

        String query = """
                SELECT 
                    s.ISBN,
                    s.ten_sach,
                    s.so_luong_muon,
                    s.so_luong_hien_con
                FROM 
                    `thông tin sách` s
                JOIN 
                    `lượt mượn` l ON s.ISBN = l.ISBN
                WHERE 
                    l.ngay_muon <= ?
                """;

        try (PreparedStatement pstmt = connection.prepareStatement(query)) {
            // Gán tham số ngày tháng năm
            String selectedDate = String.format("%s-%02d-%02d", selectedYear, Integer.parseInt(selectedMonth), Integer.parseInt(selectedDay));
            pstmt.setDate(1, java.sql.Date.valueOf(selectedDate));

            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                String isbn = rs.getString("ISBN");
                String bookTitle = rs.getString("ten_sach");
                int borrowedQuantity = rs.getInt("so_luong_muon");
                int availableQuantity = rs.getInt("so_luong_hien_con");

                Sach book = new Sach(isbn, bookTitle, "", "", availableQuantity, borrowedQuantity);
                dataList.add(book);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void showAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
