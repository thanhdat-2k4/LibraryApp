// thong ke sach con lai
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
import java.time.LocalDate;

public class LibraryRemainingBooksStats {

    @FXML
    private TableColumn<RemainingBook, String> ISBN;

    @FXML
    private TableColumn<RemainingBook, String> bookTitle;

    @FXML
    private TableColumn<RemainingBook, Integer> orderNumber;

    @FXML
    private TableColumn<RemainingBook, Integer> remainingQuantity;

    @FXML
    private TableColumn<RemainingBook, Integer> totalQuantity;

    @FXML
    private TableView<RemainingBook> tableView;

    @FXML
    private MenuButton menuNgay;

    @FXML
    private MenuButton menuThang;

    @FXML
    private MenuButton menuNam;

    private ObservableList<RemainingBook> dataList;

    private Connection connection;

    @FXML
    void initialize() {
        setupDatabaseConnection();
        setupTableView();
        setupMenuButtons();
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
        LocalDate selectedDate = getSelectedDate();
        if (selectedDate != null) {
            loadTableData(selectedDate);
        } else {
            showAlert("Thông báo", "Vui lòng chọn ngày, tháng, và năm trước khi xem danh sách.", Alert.AlertType.WARNING);
        }
    }

    private void setupTableView() {
        // Sử dụng rowIndex để tạo số thứ tự
        orderNumber.setCellValueFactory((cellData) -> {
            int rowIndex = tableView.getItems().indexOf(cellData.getValue()) + 1; // Số thứ tự bắt đầu từ 1
            return new SimpleIntegerProperty(rowIndex).asObject();
        });

        ISBN.setCellValueFactory(cellData -> cellData.getValue().bookIdProperty());
        bookTitle.setCellValueFactory(cellData -> cellData.getValue().bookTitleProperty());
        remainingQuantity.setCellValueFactory(cellData -> cellData.getValue().availableCopiesProperty().asObject());
        totalQuantity.setCellValueFactory(cellData -> cellData.getValue().totalQuantityProperty().asObject());

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

    private void setupMenuButtons() {
        // Thêm ngày (1-31)
        for (int i = 1; i <= 31; i++) {
            MenuItem dayItem = new MenuItem(String.valueOf(i));
            dayItem.setOnAction(event -> menuNgay.setText(dayItem.getText()));
            menuNgay.getItems().add(dayItem);
        }

        // Thêm tháng (1-12)
        for (int i = 1; i <= 12; i++) {
            MenuItem monthItem = new MenuItem(String.valueOf(i));
            monthItem.setOnAction(event -> menuThang.setText(monthItem.getText()));
            menuThang.getItems().add(monthItem);
        }

        // Thêm năm (giới hạn khoảng 10 năm)
        for (int i = LocalDate.now().getYear() - 10; i <= LocalDate.now().getYear(); i++) {
            MenuItem yearItem = new MenuItem(String.valueOf(i));
            yearItem.setOnAction(event -> menuNam.setText(yearItem.getText()));
            menuNam.getItems().add(yearItem);
        }
    }

    private LocalDate getSelectedDate() {
        try {
            int day = Integer.parseInt(menuNgay.getText());
            int month = Integer.parseInt(menuThang.getText());
            int year = Integer.parseInt(menuNam.getText());
            return LocalDate.of(year, month, day);
        } catch (Exception e) {
            return null; // Trả về null nếu thông tin không hợp lệ
        }
    }

    private void loadTableData(LocalDate selectedDate) {
        dataList.clear();

        String query = """
                SELECT
                    s.ISBN,
                    s.ten_sach,
                    s.ten_tac_gia,
                    s.NXB,
                    s.so_luong_hien_con,
                    s.so_luong_muon
                FROM
                    `thông tin sách` s
                WHERE
                    s.ISBN NOT IN (
                        SELECT l.ISBN
                        FROM `lượt mượn` l
                        WHERE l.ngay_muon <= ? AND (l.ngay_tra IS NULL OR l.ngay_tra > ?)
                    )
                """;

        try (PreparedStatement pstmt = connection.prepareStatement(query)) {
            pstmt.setDate(1, Date.valueOf(selectedDate));
            pstmt.setDate(2, Date.valueOf(selectedDate));

            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                String isbn = rs.getString("ISBN");
                String title = rs.getString("ten_sach");
                String author = rs.getString("ten_tac_gia");
                String publisher = rs.getString("NXB");
                int availableQuantity = rs.getInt("so_luong_hien_con");
                int borrowedQuantity = rs.getInt("so_luong_muon");

                dataList.add(new RemainingBook(isbn, title, author, publisher, availableQuantity, borrowedQuantity));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void showAlert(String title, String message, Alert.AlertType alertType) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
