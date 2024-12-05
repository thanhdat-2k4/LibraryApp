// thong ke muon sach
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
import java.sql.*;

public class BookBorrowingStats {

    @FXML
    private TableColumn<BookBorrowingStatsData, String> ISBN;  // Sử dụng BookBorrowingStatsData thay vì Sach

    @FXML
    private TableColumn<BookBorrowingStatsData, String> bookTitle;

    @FXML
    private TableColumn<BookBorrowingStatsData, Integer> borrowedQuantity;

    @FXML
    private TableColumn<BookBorrowingStatsData, Integer> totalBooks;

    @FXML
    private MenuButton menuNam;

    @FXML
    private MenuButton menuNgay;

    @FXML
    private MenuButton menuThang;

    @FXML
    private TableColumn<BookBorrowingStatsData, Integer> orderNumber;

    @FXML
    private TableView<BookBorrowingStatsData> tableView;  // Sử dụng BookBorrowingStatsData thay vì Sach

    private ObservableList<BookBorrowingStatsData> dataList;
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
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("thongke.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        Stage stage = new Stage();

        stage.setScene(scene);
        stage.show();

        ((Node) (event.getSource())).getScene().getWindow().hide();
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
        ISBN.setCellValueFactory(cellData -> cellData.getValue().bookIdProperty());
        bookTitle.setCellValueFactory(cellData -> cellData.getValue().bookTitleProperty());
        borrowedQuantity.setCellValueFactory(cellData -> cellData.getValue().borrowedCopiesProperty().asObject());
        totalBooks.setCellValueFactory(cellData ->
                new SimpleIntegerProperty(cellData.getValue().getAvailableQuantity() + cellData.getValue().getBorrowedQuantity()).asObject());
        orderNumber.setCellValueFactory(cellData ->
                new SimpleIntegerProperty(dataList.indexOf(cellData.getValue()) + 1).asObject());

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
            String selectedDate = String.format("%s-%02d-%02d", selectedYear, Integer.parseInt(selectedMonth), Integer.parseInt(selectedDay));
            pstmt.setDate(1, java.sql.Date.valueOf(selectedDate));

            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                String isbn = rs.getString("ISBN");
                String bookTitle = rs.getString("ten_sach");
                int borrowedQuantity = rs.getInt("so_luong_muon");
                int availableQuantity = rs.getInt("so_luong_hien_con");

                // Tạo đối tượng BookBorrowingStatsData thay vì Sach
                BookBorrowingStatsData bookStats = new BookBorrowingStatsData(
                        dataList.size() + 1, isbn, bookTitle, borrowedQuantity, availableQuantity);
                dataList.add(bookStats);
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
