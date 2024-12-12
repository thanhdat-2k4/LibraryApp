//thong ke nguoi muon

package com.example.oop25;

import javafx.beans.property.SimpleStringProperty;
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
import java.time.LocalDate;

public class BorrowerStatistics {

    @FXML
    private TableColumn<Borrower, String> borrowBookTitle;

    @FXML
    private TableColumn<Borrower, String> borrowStatus;

    @FXML
    private TableView<Borrower> borrowers;

    @FXML
    private MenuButton btnSelectDate;

    @FXML
    private MenuButton btnSelectMonth;

    @FXML
    private MenuButton btnSelectYear;

    @FXML
    private TableColumn<Borrower, String> orderNumber;

    @FXML
    private TableColumn<Borrower, String> reader;

    @FXML
    private TableColumn<Borrower, String> readerID;

    private ObservableList<Borrower> dataList;

    private String selectedYear, selectedMonth, selectedDay;

    private Connection connection;

    @FXML
    void initialize() {
        setupDatabaseConnection();
        setupTableView();
        populateMenuButtons();
    }

    // Thiết lập các MenuItem cho năm, tháng, ngày
    private void populateMenuButtons() {
        // Populate years
        for (int year = 2014; year <= 2024; year++) {
            MenuItem yearItem = new MenuItem(String.valueOf(year));
            yearItem.setOnAction(event -> {
                selectedYear = yearItem.getText();
                btnSelectYear.setText(selectedYear);
            });
            btnSelectYear.getItems().add(yearItem);
        }

        // Populate months
        for (int month = 1; month <= 12; month++) {
            MenuItem monthItem = new MenuItem(String.format("%02d", month));
            monthItem.setOnAction(event -> {
                selectedMonth = monthItem.getText();
                btnSelectMonth.setText(selectedMonth);
            });
            btnSelectMonth.getItems().add(monthItem);
        }

        // Populate days
        for (int day = 1; day <= 31; day++) {
            MenuItem dayItem = new MenuItem(String.format("%02d", day));
            dayItem.setOnAction(event -> {
                selectedDay = dayItem.getText();
                btnSelectDate.setText(selectedDay);
            });
            btnSelectDate.getItems().add(dayItem);
        }
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

    private boolean validateSelections() {
        return selectedYear != null && selectedMonth != null && selectedDay != null;
    }

    private void loadTableData() {
        dataList.clear();

        String query = """
                SELECT 
                    l.ma_phieu,
                    d.ten_docgia,
                    d.madocgia,
                    b.ten_sach,
                    l.tinh_trang
                FROM 
                    `lượt mượn` l
                JOIN 
                    `danh sách độc giả` d ON l.madocgia = d.madocgia
                JOIN 
                    `thông tin sách` b ON l.ISBN = b.ISBN
                WHERE 
                    l.ngay_muon <= ? AND (l.ngay_tra IS NULL OR l.ngay_tra > ?)
                """;

        try (PreparedStatement pstmt = connection.prepareStatement(query)) {
            String selectedDate = String.format("%s-%s-%s", selectedYear, selectedMonth, selectedDay);
            pstmt.setDate(1, java.sql.Date.valueOf(selectedDate));
            pstmt.setDate(2, java.sql.Date.valueOf(selectedDate));

            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                String readerID = rs.getString("madocgia");
                String readerName = rs.getString("ten_docgia");
                String borrowedBookTitle = rs.getString("ten_sach");
                String borrowStatus = rs.getString("tinh_trang");

                dataList.add(new Borrower(readerID, readerName, borrowedBookTitle, borrowStatus));
            }

            // Sau khi tải xong dữ liệu, cập nhật TableView
            borrowers.setItems(dataList);

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void setupTableView() {
        orderNumber.setCellValueFactory(cellData -> {
            int rowIndex = borrowers.getItems().indexOf(cellData.getValue()) + 1;
            return new SimpleStringProperty(String.valueOf(rowIndex));
        });

       // readerID.setCellValueFactory(cellData -> cellData.getValue().readerIDProperty());

        readerID.setCellValueFactory(cellData -> {
            return new SimpleStringProperty(cellData.getValue().getReaderId());
        });


        reader.setCellValueFactory(cellData -> cellData.getValue().readerNameProperty());
        borrowBookTitle.setCellValueFactory(cellData -> cellData.getValue().borrowedBookTitleProperty());
        borrowStatus.setCellValueFactory(cellData -> cellData.getValue().borrowStatusProperty());

        dataList = FXCollections.observableArrayList();
    }

    private void setupDatabaseConnection() {
        try {
            connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/library", "root", "1234");
            System.out.println("Kết nối cơ sở dữ liệu thành công!");
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
