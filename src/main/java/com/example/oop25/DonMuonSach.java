package com.example.oop25;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.CheckBoxTableCell;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.*;
import java.time.LocalDate;


public class DonMuonSach {


    @FXML
    private TableColumn<Sach,Boolean> chon;

    @FXML
    private TextField Ten;

    @FXML
    private TextField madocgia;

    @FXML
    private DatePicker ngay_muon;

    @FXML
    private DatePicker ngay_tra;


    @FXML
    private TableView<Sach> bang_thong_tin_sach;  // Sử dụng kiểu `Sach` cho TableView

    @FXML
    private TableColumn<Sach, String> ten_sach;         // Cột Tên Sách
    @FXML
    private TableColumn<Sach, String> ten_tac_gia;      // Cột Tên Tác Giả
    @FXML
    private TableColumn<Sach, String> NXB;              // Cột Nhà Xuất Bản
    @FXML
    private TableColumn<Sach, Integer> ISBN;            // Cột ISBN
    @FXML
    private TableColumn<Sach, Integer> so_luong_hien_con;  // Cột Số Lượng Hiện Còn
    @FXML
    private TableColumn<Sach, Integer> so_luong_muon;      // Cột Số Lượng Mượn

    @FXML
    public void loadDataFromDatabase() {
        ObservableList<Sach> danhSachSach = FXCollections.observableArrayList();

        // Kết nối cơ sở dữ liệu MySQL
        try (Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/library", "root", "1234")) {
            System.out.println("Kết nối cơ sở dữ liệu thành công!");

            // Truy vấn dữ liệu từ bảng `thông tin sách`
            String sql = "SELECT * FROM `thông tin sách`";
            try (PreparedStatement stmt = conn.prepareStatement(sql)) {
                ResultSet rs = stmt.executeQuery();
                while (rs.next()) {
                    // Lấy dữ liệu từ cơ sở dữ liệu và tạo đối tượng Sach
                    int isbn = rs.getInt("ISBN");
                    String tenSach = rs.getString("ten_sach");
                    String tenTacGia = rs.getString("ten_tac_gia");
                    String nxb = rs.getString("NXB");
                    int soLuongHienCon = rs.getInt("so_luong_hien_con");
                    int soLuongMuon = rs.getInt("so_luong_muon");

                    Sach sach = new Sach(isbn, tenSach, tenTacGia, nxb, soLuongHienCon, soLuongMuon);
                    danhSachSach.add(sach); // Thêm sách vào danh sách
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        // Liên kết dữ liệu với TableView
        bang_thong_tin_sach.setItems(danhSachSach);

        // Đặt các cột trong TableView
        ten_sach.setCellValueFactory(new PropertyValueFactory<>("tenSach"));
        ten_tac_gia.setCellValueFactory(new PropertyValueFactory<>("tenTacGia"));
        NXB.setCellValueFactory(new PropertyValueFactory<>("nxb"));
        ISBN.setCellValueFactory(new PropertyValueFactory<>("isbn"));
        so_luong_hien_con.setCellValueFactory(new PropertyValueFactory<>("soLuongHienCon"));
        so_luong_muon.setCellValueFactory(new PropertyValueFactory<>("soLuongMuon"));
    }


    @FXML
    public void initialize() {
        // Thiết lập ngày hiện tại cho DatePicker ngay_muon
        ngay_muon.setValue(LocalDate.now());
        loadDataFromDatabase(); // Gọi hàm để tải dữ liệu khi ứng dụng khởi động
        chon.setCellValueFactory(new PropertyValueFactory<>("chon"));  // "chon" là thuộc tính trong lớp Sach
        chon.setCellFactory(CheckBoxTableCell.forTableColumn(chon));  // Sử dụng CheckBoxTableCell

    }



    @FXML
    void click_quaylai(MouseEvent event) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("trangchuquanlidocgia.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        Stage stage = null;
        stage.setTitle("Hello!");
        stage.setScene(scene);
        stage.show();
    }

    @FXML
    void click_them(MouseEvent event) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("themdocgia.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        Stage stage = null;
        stage.setTitle("Hello!");
        stage.setScene(scene);
        stage.show();
    }

    @FXML
    void click_luu(MouseEvent event) {
        String ten = Ten.getText().trim();
        String maDocGia = madocgia.getText().trim();
        LocalDate ngayMuon = ngay_muon.getValue();
        LocalDate ngayTra = ngay_tra.getValue();

        // Kiểm tra ngày trả
        if (ngayTra.isBefore(ngayMuon)) {
            showError("Ngày trả phải sau hoặc bằng ngày mượn.");
            return;
        }

        // Kết nối cơ sở dữ liệu
        try (Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/library", "root", "1234")) {
            System.out.println("Kết nối cơ sở dữ liệu thành công!");

            // Kiểm tra Mã độc giả và tên độc giả trong cơ sở dữ liệu
            String sqlCheckDocGia = "SELECT ten_docgia FROM `danh sách độc giả` WHERE madocgia = ?";
            try (PreparedStatement stmt = conn.prepareStatement(sqlCheckDocGia)) {
                stmt.setString(1, maDocGia);
                ResultSet rs = stmt.executeQuery();
                if (rs.next()) {
                    String tenDocGiaTrongDB = rs.getString("ten_docgia");
                    if (!tenDocGiaTrongDB.equalsIgnoreCase(ten)) {
                        showError("Tên độc giả không khớp với mã độc giả.");
                        return;
                    }
                } else {
                    showError("Mã độc giả không tồn tại.");
                    return;
                }
            }

            // Duyệt qua các sách được chọn từ TableView
            ObservableList<Sach> danhSachSachDaChon = (ObservableList<Sach>) bang_thong_tin_sach.getItems(); // ObservableList<Sach>
            for (Sach sach : danhSachSachDaChon) {
                // Nếu sách được chọn (CheckBox được tick)
                if (sach.getChon().isSelected()) {
                    // Kiểm tra số lượng sách hiện có
                    if (sach.getSoLuongHienCon() <= 0) {
                        showError("Sách '" + sach.getTenSach() + "' hiện không có sẵn.");
                        return;
                    }

                    // In thông tin sách đã chọn
                    System.out.println(sach.toString());

                    // Cập nhật thông tin vào bảng `luot_muon`
                    String sqlInsert = "INSERT INTO luot_muon (ISBN, madocgia, ngay_muon, ngay_tra) VALUES (?, ?, ?, ?)";
                    try (PreparedStatement insertStmt = conn.prepareStatement(sqlInsert)) {
                        insertStmt.setString(1, String.valueOf(sach.getIsbn())); // ISBN của sách
                        insertStmt.setString(2, maDocGia); // Mã độc giả
                        insertStmt.setString(3, ngayMuon.toString()); // Ngày mượn
                        insertStmt.setString(4, ngayTra.toString()); // Ngày trả
                        insertStmt.executeUpdate(); // Thực thi câu lệnh
                        System.out.println("Đã thêm sách ISBN: " + sach.getIsbn());

                        // Cập nhật số lượng sách trong bảng `thong tin sach`
                        String sqlUpdateSach = "UPDATE `thong tin sach` SET so_luong_hien_con = ?, so_luong_muon = ? WHERE ISBN = ?";
                        try (PreparedStatement updateStmt = conn.prepareStatement(sqlUpdateSach)) {
                            updateStmt.setInt(1, sach.getSoLuongHienCon() - 1); // Giảm số lượng hiện có
                            updateStmt.setInt(2, sach.getSoLuongMuon() + 1); // Tăng số lượng mượn
                            updateStmt.setString(3, String.valueOf(sach.getIsbn())); // ISBN của sách
                            updateStmt.executeUpdate();
                            System.out.println("Đã cập nhật số lượng sách ISBN: " + sach.getIsbn());
                        } catch (SQLException e) {
                            e.printStackTrace();
                            showError("Lỗi khi cập nhật số lượng sách.");
                            return;
                        }
                    } catch (SQLException e) {
                        e.printStackTrace();
                        showError("Lỗi khi thêm sách vào cơ sở dữ liệu.");
                        return;
                    }
                }
            }

            // Hiển thị thông báo thành công
            showInfo("Thêm thành công!\nNgày mượn: " + ngayMuon + "\nNgày trả: " + ngayTra);
        } catch (SQLException e) {
            e.printStackTrace();
            showError("Lỗi kết nối cơ sở dữ liệu: " + e.getMessage());
        }
    }




    // Hiển thị thông báo thành công
    public void showInfo(String s) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Thông báo");
        alert.setHeaderText(null);
        alert.setContentText(s);
        alert.showAndWait();
    }

    // Hiển thị thông báo lỗi
    public void showError(String s) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Lỗi");
        alert.setHeaderText(null);
        alert.setContentText(s);
        alert.showAndWait();
    }
}