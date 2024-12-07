////xoa doc gia
//package com.example.oop25;
//
//import javafx.collections.FXCollections;
//import javafx.collections.ObservableList;
//import javafx.fxml.FXML;
//import javafx.fxml.FXMLLoader;
//import javafx.fxml.Initializable;
//import javafx.scene.Node;
//import javafx.scene.Scene;
//import javafx.scene.control.*;
//import javafx.scene.control.cell.PropertyValueFactory;
//import javafx.scene.input.KeyCode;
//import javafx.scene.input.KeyEvent;
//import javafx.scene.input.MouseEvent;
//import javafx.stage.Stage;
//
//import java.io.IOException;
//import java.net.URL;
//import java.sql.*;
//import java.util.ResourceBundle;
//
//public class deleteReaderInfo implements Initializable {
//
//    @FXML
//    private Button print;
//
//    @FXML
//    private TextField search;
//
//    @FXML
//    private ComboBox<String> searchType;
//
//    @FXML
//    private TableView<DocGia> ReaderDashboard;
//
//    @FXML
//    private TableColumn<DocGia, String> madocgia;
//
//    @FXML
//    private TableColumn<DocGia, String> ten_docgia;
//
//    @FXML
//    private TableColumn<DocGia, String> thong_tin;
//
//    @FXML
//    private TableColumn<DocGia, String> ngay_giahan;
//
//    @FXML
//    private TableColumn<DocGia, String> ngay_hethan;
//
//    @FXML
//    private TableColumn<DocGia, String> ghi_chu;
//
//    private Connection connection;
//
//    @Override
//    public void initialize(URL url, ResourceBundle resourceBundle) {
//        connectDatabase();
//
//        ObservableList<String> phuongphap = FXCollections.observableArrayList(
//                "Tìm kiếm theo mã thẻ",
//                "Tìm kiếm theo tên",
//                "Tìm kiếm theo ngày gia hạn",
//                "Tìm kiếm theo ngày hết hạn");
//
//        // Gắn dữ liệu vào TableView
//        madocgia.setCellValueFactory(new PropertyValueFactory<>("madocgia"));
//        ten_docgia.setCellValueFactory(new PropertyValueFactory<>("ten_docgia"));
//        thong_tin.setCellValueFactory(new PropertyValueFactory<>("thong_tin"));
//        ngay_giahan.setCellValueFactory(new PropertyValueFactory<>("ngay_giahan"));
//        ngay_hethan.setCellValueFactory(new PropertyValueFactory<>("ngay_hethan"));
//        ghi_chu.setCellValueFactory(new PropertyValueFactory<>("ghi_chu"));
//
//        searchType.setItems(phuongphap);
//
//        // Load data from the database
//        loadReaderData();
//    }
//
//    private void connectDatabase() {
//        try {
//            String url = "jdbc:mysql://localhost:3306/library";
//            String user = "root";
//            String password = "1234";
//            connection = DriverManager.getConnection(url, user, password);
//            System.out.println("Database connected successfully!");
//        } catch (SQLException e) {
//            e.printStackTrace();
//        }
//    }
//
//    // Method to load data from the database into the TableView
//    private void loadReaderData() {
//        ObservableList<DocGia> docGiaList = FXCollections.observableArrayList();
//        String sqlQuery = "SELECT * FROM `danh sách độc giả`";
//
//        try (Statement statement = connection.createStatement()) {
//            ResultSet resultSet = statement.executeQuery(sqlQuery);
//            while (resultSet.next()) {
//                docGiaList.add(new DocGia(
//                        resultSet.getString("madocgia"),
//                        resultSet.getString("ten_docgia"),
//                        resultSet.getString("thong_tin"),
//                        resultSet.getString("ngay_giahan"),
//                        resultSet.getString("ngay_hethan"),
//                        resultSet.getString("ghi_chu")
//                ));
//            }
//        } catch (SQLException e) {
//            e.printStackTrace();
//        }
//
//        // Cập nhật TableView với dữ liệu
//        ReaderDashboard.setItems(docGiaList);
//    }
//
//    @FXML
//    void Enter_search(KeyEvent event) throws SQLException {
//        if (event.getCode() == KeyCode.ENTER) {
//            int searchIndex = searchType.getSelectionModel().getSelectedIndex();
//            ObservableList<DocGia> docGiaList = FXCollections.observableArrayList();
//
//            String sqlQuery = null;
//
//            // Xây dựng câu lệnh SQL dựa trên kiểu tìm kiếm
//            switch (searchIndex) {
//                case 0: // Tìm kiếm theo mã độc giả
//                    sqlQuery = "SELECT * FROM `danh sách độc giả` WHERE madocgia = ?";
//                    break;
//                case 1: // Tìm kiếm theo tên độc giả
//                    sqlQuery = "SELECT * FROM `danh sách độc giả` WHERE ten_docgia = ?";
//                    break;
//                case 2: // Tìm kiếm theo ngày gia hạn
//                    sqlQuery = "SELECT * FROM `danh sách độc giả` WHERE ngay_giahan = ?";
//                    break;
//                case 3: // Tìm kiếm theo ngày hết hạn
//                    sqlQuery = "SELECT * FROM `danh sách độc giả` WHERE ngay_hethan = ?";
//                    break;
//                default:
//                    showAlert("Chưa chọn loại tìm kiếm", "Lỗi", "Hãy chọn một loại tìm kiếm trước khi tiếp tục.");
//                    return;
//            }
//
//            if (sqlQuery != null) {
//                try (PreparedStatement statement = connection.prepareStatement(sqlQuery)) {
//                    statement.setString(1, search.getText());
//                    ResultSet resultSet = statement.executeQuery();
//
//                    while (resultSet.next()) {
//                        docGiaList.add(new DocGia(
//                                resultSet.getString("madocgia"),
//                                resultSet.getString("ten_docgia"),
//                                resultSet.getString("thong_tin"),
//                                resultSet.getString("ngay_giahan"),
//                                resultSet.getString("ngay_hethan"),
//                                resultSet.getString("ghi_chu")
//                        ));
//                    }
//                }
//            }
//
//            // Cập nhật TableView
//            ReaderDashboard.setItems(docGiaList);
//
//            // Hiển thị thông báo
//            if (!docGiaList.isEmpty()) {
//                showAlert("Thông báo", "Tìm kiếm thành công", "Đã tìm thấy kết quả.");
//            } else {
//                showAlert("Thông báo", "Không tìm thấy kết quả", "Không có thông tin phù hợp.");
//            }
//        }
//    }
//
//    private void showAlert(String title, String header, String content) {
//        Alert alert = new Alert(Alert.AlertType.INFORMATION);
//        alert.setTitle(title);
//        alert.setHeaderText(header);
//        alert.setContentText(content);
//        alert.showAndWait();
//    }
//
//    @FXML
//    void click_delete(MouseEvent event) {
//        DocGia selectedReader = ReaderDashboard.getSelectionModel().getSelectedItem();
//        if (selectedReader != null) {
//            String deleteQuery = "DELETE FROM `danh sách độc giả` WHERE madocgia = ?";
//            try (PreparedStatement statement = connection.prepareStatement(deleteQuery)) {
//                statement.setString(1, selectedReader.getMadocgia());
//                statement.executeUpdate();
//                ReaderDashboard.getItems().remove(selectedReader);
//            } catch (SQLException e) {
//                e.printStackTrace();
//            }
//        } else {
//            showAlert("Thông báo", "Không có hàng nào được chọn", "Vui lòng chọn một hàng để xóa.");
//        }
//    }
//
//    @FXML
//    void click_exit(MouseEvent event) throws  IOException {
//        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("trangchuquanlidocgia.fxml"));
//        Scene scene = new Scene(fxmlLoader.load());
//        Stage stage = new Stage();
//        stage.setScene(scene);
//        stage.show();
//        ((Node)(event.getSource())).getScene().getWindow().hide();
//    }
//
//    @FXML
//    void click_in(MouseEvent event) throws IOException {
//        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("danhsachkhithemdocgia.fxml"));
//        Scene scene = new Scene(fxmlLoader.load());
//        Stage stage = new Stage();
//        stage.setScene(scene);
//        stage.show();
//        ((Node)(event.getSource())).getScene().getWindow().hide();
//    }
//}
package com.example.oop25;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
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

public class DeleteReaderInfo implements Initializable {

    @FXML
    private Button printButton;

    @FXML
    private TextField searchField;

    @FXML
    private ComboBox<String> searchTypeComboBox;

    @FXML
    private TableView<Reader> readerDashboardTableView;

    @FXML
    private TableColumn<Reader, String> readerIdColumn;

    @FXML
    private TableColumn<Reader, String> readerNameColumn;

    @FXML
    private TableColumn<Reader, String> contactInfoColumn;

    @FXML
    private TableColumn<Reader, String> renewalDateColumn;

    @FXML
    private TableColumn<Reader, String> expirationDateColumn;

    @FXML
    private TableColumn<Reader, String> noteColumn;

    private Connection connection;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        connectToDatabase();

        ObservableList<String> searchMethods = FXCollections.observableArrayList(
                "Tìm kiếm theo mã thẻ",
                "Tìm kiếm theo tên",
                "Tìm kiếm theo ngày gia hạn",
                "Tìm kiếm theo ngày hết hạn");

        // Gắn dữ liệu vào TableView
        readerIdColumn.setCellValueFactory(new PropertyValueFactory<>("readerId"));
        readerNameColumn.setCellValueFactory(new PropertyValueFactory<>("readerName"));
        contactInfoColumn.setCellValueFactory(new PropertyValueFactory<>("contactInfo"));
        renewalDateColumn.setCellValueFactory(new PropertyValueFactory<>("renewalDate"));
        expirationDateColumn.setCellValueFactory(new PropertyValueFactory<>("expirationDate"));
        noteColumn.setCellValueFactory(new PropertyValueFactory<>("note"));

        searchTypeComboBox.setItems(searchMethods);

        // Load data from the database
        loadReaderData();
    }

    private void connectToDatabase() {
        try {
            String url = "jdbc:mysql://localhost:3306/library";
            String user = "root";
            String password = "1234";
            connection = DriverManager.getConnection(url, user, password);
            System.out.println("Database connected successfully!");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Method to load data from the database into the TableView
    private void loadReaderData() {
        ObservableList<Reader> readerList = FXCollections.observableArrayList();
        String sqlQuery = "SELECT * FROM `danh sách độc giả`";

        try (Statement statement = connection.createStatement()) {
            ResultSet resultSet = statement.executeQuery(sqlQuery);
            while (resultSet.next()) {
                readerList.add(new Reader(
                        resultSet.getString("madocgia"),
                        resultSet.getString("ten_docgia"),
                        resultSet.getString("thong_tin"),
                        resultSet.getString("ngay_giahan"),
                        resultSet.getString("ngay_hethan"),
                        resultSet.getString("ghi_chu")
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        // Cập nhật TableView với dữ liệu
        readerDashboardTableView.setItems(readerList);
    }

    @FXML
    void handleSearchEnter(KeyEvent event) throws SQLException {
        if (event.getCode() == KeyCode.ENTER) {
            int searchIndex = searchTypeComboBox.getSelectionModel().getSelectedIndex();
            ObservableList<Reader> readerList = FXCollections.observableArrayList();

            String sqlQuery = null;

            // Xây dựng câu lệnh SQL dựa trên kiểu tìm kiếm
            switch (searchIndex) {
                case 0: // Tìm kiếm theo mã độc giả
                    sqlQuery = "SELECT * FROM `danh sách độc giả` WHERE madocgia = ?";
                    break;
                case 1: // Tìm kiếm theo tên độc giả
                    sqlQuery = "SELECT * FROM `danh sách độc giả` WHERE ten_docgia = ?";
                    break;
                case 2: // Tìm kiếm theo ngày gia hạn
                    sqlQuery = "SELECT * FROM `danh sách độc giả` WHERE ngay_giahan = ?";
                    break;
                case 3: // Tìm kiếm theo ngày hết hạn
                    sqlQuery = "SELECT * FROM `danh sách độc giả` WHERE ngay_hethan = ?";
                    break;
                default:
                    showAlert("Chưa chọn loại tìm kiếm", "Lỗi", "Hãy chọn một loại tìm kiếm trước khi tiếp tục.");
                    return;
            }

            if (sqlQuery != null) {
                try (PreparedStatement statement = connection.prepareStatement(sqlQuery)) {
                    statement.setString(1, searchField.getText());
                    ResultSet resultSet = statement.executeQuery();

                    while (resultSet.next()) {
                        readerList.add(new Reader(
                                resultSet.getString("madocgia"),
                                resultSet.getString("ten_docgia"),
                                resultSet.getString("thong_tin"),
                                resultSet.getString("ngay_giahan"),
                                resultSet.getString("ngay_hethan"),
                                resultSet.getString("ghi_chu")
                        ));
                    }
                }
            }

            // Cập nhật TableView
            readerDashboardTableView.setItems(readerList);

            // Hiển thị thông báo
            if (!readerList.isEmpty()) {
                showAlert("Thông báo", "Tìm kiếm thành công", "Đã tìm thấy kết quả.");
            } else {
                showAlert("Thông báo", "Không tìm thấy kết quả", "Không có thông tin phù hợp.");
            }
        }
    }

    private void showAlert(String title, String header, String content) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(header);
        alert.setContentText(content);
        alert.showAndWait();
    }

    @FXML
    void handleDeleteClick(MouseEvent event) {
        Reader selectedReader = readerDashboardTableView.getSelectionModel().getSelectedItem();
        if (selectedReader != null) {
            String deleteQuery = "DELETE FROM `danh sách độc giả` WHERE madocgia = ?";
            try (PreparedStatement statement = connection.prepareStatement(deleteQuery)) {
                statement.setString(1, selectedReader.getReaderId());
                statement.executeUpdate();
                readerDashboardTableView.getItems().remove(selectedReader);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        } else {
            showAlert("Thông báo", "Không có hàng nào được chọn", "Vui lòng chọn một hàng để xóa.");
        }
    }

    @FXML
    void handleExitClick(MouseEvent event) throws  IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("trangchuquanlidocgia.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        Stage stage = new Stage();
        stage.setScene(scene);
        stage.show();
        ((Node)(event.getSource())).getScene().getWindow().hide();
    }

    @FXML
    void handlePrintClick(MouseEvent event) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("danhsachkhithemdocgia.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        Stage stage = new Stage();
        stage.setScene(scene);
        stage.show();
        ((Node)(event.getSource())).getScene().getWindow().hide();
    }
}
