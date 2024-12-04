// muon sach
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

public class borrowBook {

    @FXML
    private TableView<Sach> bang_thong_tin_sach;

    @FXML
    private TableColumn<Sach, Boolean> chon;

    @FXML
    private TableColumn<Sach, String> ten_sach, ten_tac_gia, NXB, ISBN;

    @FXML
    private TableColumn<Sach, Integer> so_luong_hien_con, so_luong_muon;

    @FXML
    private TextField madocgia, ten_docgia;

    @FXML
    private DatePicker ngay_muon, ngay_tra;

    @FXML
    public void initialize() {
        // Cấu hình cột "chon" là CheckBox
        chon.setCellValueFactory(cellData -> cellData.getValue().chonProperty());
        chon.setCellFactory(column -> new TableCell<Sach, Boolean>() {
            private final CheckBox checkBox = new CheckBox();

            {
                // Xử lý sự kiện tick vào checkbox
                checkBox.setOnAction(event -> {
                    Sach sach = getTableView().getItems().get(getIndex());
                    sach.setChon(checkBox.isSelected());
                });
            }

            @Override
            protected void updateItem(Boolean item, boolean empty) {
                super.updateItem(item, empty);

                if (empty) {
                    setGraphic(null);
                } else {
                    checkBox.setSelected(item != null && item);
                    setGraphic(checkBox);
                }
            }
        });

        // Đặt ngày mặc định cho ngày mượn
        ngay_muon.setValue(LocalDate.now()); // Đặt giá trị mặc định là ngày hôm nay

        // Cấu hình các cột còn lại
        ten_sach.setCellValueFactory(new PropertyValueFactory<>("tenSach"));
        ten_tac_gia.setCellValueFactory(new PropertyValueFactory<>("tenTacGia"));
        NXB.setCellValueFactory(new PropertyValueFactory<>("nxb"));
        ISBN.setCellValueFactory(new PropertyValueFactory<>("isbn"));
        so_luong_hien_con.setCellValueFactory(new PropertyValueFactory<>("soLuongHienCon"));
        so_luong_muon.setCellValueFactory(new PropertyValueFactory<>("soLuongMuon"));

        // Tải dữ liệu từ cơ sở dữ liệu
        loadDataFromDatabase();
    }


    @FXML
    public void loadDataFromDatabase() {
        ObservableList<Sach> danhSachSach = FXCollections.observableArrayList();
        String sql = "SELECT * FROM `thông tin sách`";

        try (Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/library", "root", "1234");
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                danhSachSach.add(new Sach(
                        rs.getString("ISBN"),
                        rs.getString("ten_sach"),
                        rs.getString("ten_tac_gia"),
                        rs.getString("NXB"),
                        rs.getInt("so_luong_hien_con"),
                        rs.getInt("so_luong_muon")
                ));
            }
        } catch (SQLException e) {
            showError("Lỗi khi tải dữ liệu từ cơ sở dữ liệu: " + e.getMessage());
        }

        bang_thong_tin_sach.setItems(danhSachSach);
    }

    @FXML
    public void click_them(MouseEvent event) {
        // Reset các trường nhập thông tin
        madocgia.clear();
        ten_docgia.clear();
        ngay_muon.setValue(LocalDate.now());
        ngay_tra.setValue(null);

        // Reset trạng thái chọn của các sách
        for (Sach sach : bang_thong_tin_sach.getItems()) {
            sach.setChon(false);
        }
        bang_thong_tin_sach.refresh(); // Cập nhật lại bảng

        showInfo("Đã reset thông tin. Bạn có thể nhập đơn mượn mới.");
    }

    @FXML
    public void click_quaylai(MouseEvent event) {
        try {
            // Quay lại màn hình chính
            FXMLLoader loader = new FXMLLoader(getClass().getResource("QuanLiMuonTra.fxml"));
            Scene scene = new Scene(loader.load());
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            showError("Không thể quay lại màn hình chính: " + e.getMessage());
        }
    }

    @FXML
    public void click_luu(MouseEvent event) {
        String ten = ten_docgia.getText().trim();
        String maDocGia = madocgia.getText().trim();
        LocalDate ngayMuon = ngay_muon.getValue();
        LocalDate ngayTra = ngay_tra.getValue();

        if (!validateInputs(ten, maDocGia, ngayMuon, ngayTra)) return;

        try (Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/library", "root", "1234")) {
            if (!checkDocGia(conn, ten, maDocGia)) return;

            ObservableList<Sach> danhSachSach = bang_thong_tin_sach.getItems();
            boolean hasSelectedBooks = false;

            for (Sach sach : danhSachSach) {
                if (sach.isChon()) {
                    hasSelectedBooks = true;

                    if (sach.getSoLuongHienCon() <= 0) {
                        showError("Sách '" + sach.getTenSach() + "' hiện không còn.");
                        continue;
                    }
                    processSach(conn, sach, maDocGia, ngayMuon, ngayTra);
                }
            }

            if (!hasSelectedBooks) {
                showError("Vui lòng chọn ít nhất một sách để mượn.");
                return;
            }

            showInfo("Lưu thông tin mượn sách thành công!");
            loadDataFromDatabase(); // Tải lại dữ liệu sau khi cập nhật
        } catch (SQLException e) {
            showError("Lỗi cơ sở dữ liệu: " + e.getMessage());
        }
    }



    private boolean validateInputs(String ten, String maDocGia, LocalDate ngayMuon, LocalDate ngayTra) {
        if (ten.isEmpty() || maDocGia.isEmpty() || ngayMuon == null || ngayTra == null) {
            showError("Vui lòng nhập đầy đủ thông tin, bao gồm cả ngày trả.");
            return false;
        }
        if (ngayTra.isBefore(ngayMuon)) {
            showError("Ngày trả phải sau hoặc bằng ngày mượn.");
            return false;
        }
        return true;
    }

    private boolean checkDocGia(Connection conn, String ten, String maDocGia) throws SQLException {
        String sql = "SELECT ten_docgia FROM `danh sách độc giả` WHERE madocgia = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, maDocGia);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                if (!rs.getString("ten_docgia").equalsIgnoreCase(ten)) {
                    showError("Tên độc giả không khớp với mã độc giả.");
                    return false;
                }
            } else {
                showError("Mã độc giả không tồn tại.");
                return false;
            }
        }
        return true;
    }


    private void processSach(Connection conn, Sach sach, String maDocGia, LocalDate ngayMuon, LocalDate ngayTra) throws SQLException {
        String maPhieu = maDocGia + "_" + sach.getIsbn();

        // Thêm cột tinh_trang với giá trị "đang mượn"
        String sqlInsert = "INSERT INTO `lượt mượn` (ma_phieu, ISBN, madocgia, ngay_muon, ngay_tra, tinh_trang) VALUES (?, ?, ?, ?, ?, ?)";
        String sqlUpdate = "UPDATE `thông tin sách` SET so_luong_hien_con = so_luong_hien_con - 1, so_luong_muon = so_luong_muon + 1 WHERE ISBN = ?";

        try (PreparedStatement stmtInsert = conn.prepareStatement(sqlInsert);
             PreparedStatement stmtUpdate = conn.prepareStatement(sqlUpdate)) {

            // Gán các giá trị cho câu lệnh INSERT
            stmtInsert.setString(1, maPhieu);
            stmtInsert.setString(2, sach.getIsbn());
            stmtInsert.setString(3, maDocGia);
            stmtInsert.setDate(4, Date.valueOf(ngayMuon));
            stmtInsert.setDate(5, Date.valueOf(ngayTra));
            stmtInsert.setString(6, "đang mượn"); // Thêm giá trị cho cột tinh_trang

            // Thực thi câu lệnh INSERT
            stmtInsert.executeUpdate();

            // Gán giá trị và thực thi câu lệnh UPDATE
            stmtUpdate.setString(1, sach.getIsbn());
            stmtUpdate.executeUpdate();
        }
    }


    private void showInfo(String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Thông báo");
        alert.setContentText(message);
        alert.showAndWait();
    }

    private void showError(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Lỗi");
        alert.setContentText(message);
        alert.showAndWait();
    }
}

