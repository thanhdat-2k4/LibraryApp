package com.example.oop25;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
        import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

import java.sql.*;

public class BookAdder {

    // Khai báo thông tin kết nối cơ sở dữ liệu
    private static final String DB_URL = "jdbc:mysql://localhost:3306/library";
    private static final String DB_USER = "root";
    private static final String DB_PASSWORD = "1234";

    @FXML
    private TableView<Sach> tableViewBooks;

    @FXML
    private TableColumn<Sach, String> colISBN;

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

    // Thêm sách vào cơ sở dữ liệu
    private void addBookToDatabase(String isbn, String tenSach, String tenTacGia, String nxb, int soLuong) {
        String sql = "INSERT INTO `thông tin sách` (ISBN, ten_sach, ten_tac_gia, NXB, so_luong_hien_con, so_luong_muon) " +
                "VALUES (?, ?, ?, ?, ?, 0)";

        try (Connection connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

            // Gán giá trị cho câu lệnh SQL
            preparedStatement.setString(1, isbn);
            preparedStatement.setString(2, tenSach);
            preparedStatement.setString(3, tenTacGia);
            preparedStatement.setString(4, nxb);
            preparedStatement.setInt(5, soLuong);

            // Thực hiện câu lệnh và kiểm tra kết quả
            int rowsAffected = preparedStatement.executeUpdate();
            if (rowsAffected > 0) {
                showAlert(Alert.AlertType.INFORMATION, "Thành công", "Sách đã được thêm vào cơ sở dữ liệu!");
            } else {
                showAlert(Alert.AlertType.WARNING, "Thất bại", "Không thêm được sách vào cơ sở dữ liệu.");
            }
        } catch (SQLException e) {
            // In ra lỗi chi tiết và hiển thị thông báo lỗi
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Lỗi", "Không thể thêm sách vào cơ sở dữ liệu!\nChi tiết: " + e.getMessage());
        }
    }



    @FXML
    private TextField tfISBN;      // Trường nhập ISBN
    @FXML
    private TextField tfTenSach;   // Trường nhập tên sách
    @FXML
    private TextField tfTenTacGia;    // Trường nhập tên tác giả
    @FXML
    private TextField tfNXB;       // Trường nhập nhà xuất bản
    @FXML
    private TextField tfSoLuong;   // Trường nhập số lượng (có thể để trống hoặc mặc định)


    // Hàm xử lý sự kiện tự động điền
    @FXML
    void click_tudongdien(MouseEvent event) {
        TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle("Tự động điền thông tin");
        dialog.setHeaderText("Nhập ISBN của sách");
        dialog.setContentText("ISBN:");

        // Nhập ISBN từ hộp thoại
        String isbn = dialog.showAndWait().orElse(null);
        if (isbn != null && !isbn.isEmpty()) {
            // Lấy thông tin sách từ API
            String bookInfo = Tudongdien.Tudongdienthongtin(isbn);
            if (bookInfo != null) {
                String[] details = bookInfo.split(";");
                String title = details[0];
                String author = details[1];
                String publisher = details[2];

                // Điền thông tin vào các trường
                tfISBN.setText(isbn);
                tfTenSach.setText(title);
                tfTenTacGia.setText(author);
                tfNXB.setText(publisher);

                showAlert(Alert.AlertType.INFORMATION, "Thông tin sách",
                        "Thông tin đã được tự động điền:\n" +
                                "ISBN: " + isbn + "\n" +
                                "Tên sách: " + title + "\n" +
                                "Tác giả: " + author + "\n" +
                                "NXB: " + publisher);
            } else {
                showAlert(Alert.AlertType.WARNING, "Không tìm thấy", "Không tìm thấy thông tin cho ISBN: " + isbn);
            }
        }
    }

    private boolean isBookExist(String isbn, String tenSach) {
        String sql = "SELECT COUNT(*) FROM `thông tin sách` WHERE ISBN = ? OR ten_sach = ?";
        try (Connection connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

            // Gán giá trị cho câu lệnh SQL
            preparedStatement.setString(1, isbn);
            preparedStatement.setString(2, tenSach);

            // Thực thi câu lệnh và kiểm tra kết quả
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                return resultSet.getInt(1) > 0; // Trả về true nếu đã tồn tại
            }
        } catch (SQLException e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Lỗi", "Không thể kiểm tra sách trong cơ sở dữ liệu!\nChi tiết: " + e.getMessage());
        }
        return false;
    }



    @FXML
    void click_xacnhanthem(MouseEvent event) {
        try {
            // Lấy giá trị từ các trường nhập liệu
            String isbn = tfISBN.getText().trim();
            String tenSach = tfTenSach.getText().trim();
            String tenTacGia = tfTenTacGia.getText().trim();
            String nxb = tfNXB.getText().trim();
            String soLuongStr = tfSoLuong.getText().trim();

            // Kiểm tra giá trị rỗng
            if (isbn.isEmpty() || tenSach.isEmpty() || tenTacGia.isEmpty() || nxb.isEmpty() || soLuongStr.isEmpty()) {
                showAlert(Alert.AlertType.WARNING, "Lỗi", "Vui lòng điền đầy đủ thông tin!");
                return;
            }

            // Chuyển đổi số lượng sang kiểu int
            int soLuong = Integer.parseInt(soLuongStr);

            // Kiểm tra xem sách đã tồn tại hay chưa
            if (isBookExist(isbn, tenSach)) {
                showAlert(Alert.AlertType.WARNING, "Thất bại", "Sách đã tồn tại trong cơ sở dữ liệu!");
                return;
            }

            // Gọi phương thức thêm sách
            addBookToDatabase(isbn, tenSach, tenTacGia, nxb, soLuong);

            // Làm mới danh sách sách trên giao diện nếu thêm thành công
            loadBooksFromDatabase();

        } catch (NumberFormatException e) {
            showAlert(Alert.AlertType.ERROR, "Lỗi", "Số lượng phải là một số hợp lệ!");
        } catch (Exception e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Lỗi", "Có lỗi xảy ra khi thêm sách: " + e.getMessage());
        }
    }


    private void clearFields() {
        tfISBN.clear();
        tfTenSach.clear();
        tfTenTacGia.clear();
        tfNXB.clear();
        tfSoLuong.clear();
    }

    @FXML
    void click_quaylaiquanlysach(MouseEvent event) {
        try {
            // Tải tệp FXML của giao diện quản lý sách
            FXMLLoader loader = new FXMLLoader(getClass().getResource("Quanlysach.fxml"));
            Parent root = loader.load();

            // Lấy Stage hiện tại và thiết lập lại Scene
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

    @FXML
    public void initialize() {
        colISBN.setCellValueFactory(data -> data.getValue().isbnProperty());
        colTenSach.setCellValueFactory(data -> data.getValue().tenSachProperty());
        colTenTacGia.setCellValueFactory(data -> data.getValue().tenTacGiaProperty());
        colNXB.setCellValueFactory(data -> data.getValue().nxbProperty());
        colSoLuongHienCon.setCellValueFactory(data -> data.getValue().soLuongHienConProperty().asObject());
        colSoLuongMuon.setCellValueFactory(data -> data.getValue().soLuongMuonProperty().asObject());

        loadBooksFromDatabase();
    }

    // Tải danh sách sách từ cơ sở dữ liệu
    private void loadBooksFromDatabase() {
        ObservableList<Sach> bookList = FXCollections.observableArrayList();

        String query = "SELECT * FROM `thông tin sách`";
        try (Connection connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
             Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(query)) {

            while (resultSet.next()) {
                String isbn = resultSet.getString("ISBN"); // Nếu ISBN là chuỗi
                String tenSach = resultSet.getString("ten_sach");
                String tenTacGia = resultSet.getString("ten_tac_gia");
                String nxb = resultSet.getString("NXB");
                Integer soLuongHienCon = resultSet.getObject("so_luong_hien_con", Integer.class); // Xử lý giá trị null
                Integer soLuongMuon = resultSet.getObject("so_luong_muon", Integer.class); // Xử lý giá trị null

                // Nếu soLuongHienCon hoặc soLuongMuon là null, gán giá trị mặc định là 0
                soLuongHienCon = (soLuongHienCon != null) ? soLuongHienCon : 0;
                soLuongMuon = (soLuongMuon != null) ? soLuongMuon : 0;

                bookList.add(new Sach(isbn, tenSach, tenTacGia, nxb, soLuongHienCon, soLuongMuon));
            }

            tableViewBooks.setItems(bookList);
        } catch (Exception e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Lỗi", "Không thể tải danh sách sách từ cơ sở dữ liệu!");
        }
    }

}
