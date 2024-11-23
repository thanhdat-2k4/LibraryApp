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
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.sql.*;
import java.util.ResourceBundle;


public class giaodientimkiemdocgia implements Initializable {



    @FXML
    private TextField search;

    @FXML
    private ComboBox<String> loai_search;

    @FXML
    private TableView<DocGia> bang_thong_tin_doc_gia;


    @FXML
    private TableColumn<DocGia, String> madocgia;

    @FXML
    private TableColumn<DocGia, String> ten_docgia;

    @FXML
    private TableColumn<DocGia, String> thong_tin;

    @FXML
    private TableColumn<DocGia, String> ngay_giahan;

    @FXML
    private TableColumn<DocGia, String> ngay_hethan;

    @FXML
    private TableColumn<DocGia, String> ghi_chu;





    @FXML
    void nhap_search(KeyEvent event) throws SQLException {
        if (event.getCode() == KeyCode.ENTER) {
            int i = loai_search.getSelectionModel().getSelectedIndex();

            // Tạo ObservableList để chứa các kết quả tìm kiếm
            ObservableList<DocGia> docGiaList = FXCollections.observableArrayList();

            if (i == 0) {
                // Tìm kiểm theo mã độc giả
                String codeSQL = "select * from `danh sách độc giả` where madocgia = ?;";
                try (Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/library", "root", "1234");
                     PreparedStatement searcher = connection.prepareStatement(codeSQL)) {
                    searcher.setString(1, search.getText());
                    ResultSet set = searcher.executeQuery();
                    while (set.next()) {
                        docGiaList.add(new DocGia(
                                set.getString("madocgia"),
                                set.getString("ten_docgia"),
                                set.getString("thong_tin"),
                                set.getString("ngay_giahan"),
                                set.getString("ngay_hethan"),
                                set.getString("ghi_chu")
                        ));
                    }
                }
            } else if (i == 1) {
                // Tìm kiếm theo tên độc giả
                String codeSQL = "select * from `danh sách độc giả` where ten_docgia = ?;";
                try (Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/library", "root", "1234");
                     PreparedStatement searcher = connection.prepareStatement(codeSQL)) {
                    searcher.setString(1, search.getText());
                    ResultSet set = searcher.executeQuery();
                    while (set.next()) {
                        docGiaList.add(new DocGia(
                                set.getString("madocgia"),
                                set.getString("ten_docgia"),
                                set.getString("thong_tin"),
                                set.getString("ngay_giahan"),
                                set.getString("ngay_hethan"),
                                set.getString("ghi_chu")
                        ));
                    }
                }
            } else if (i == 2) {
                // Tìm kiếm theo ngày gia hạn
                String codeSQL = "select * from `danh sách độc giả` where ngay_giahan = ?;";
                try (Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/library", "root", "1234");
                     PreparedStatement searcher = connection.prepareStatement(codeSQL)) {
                    searcher.setString(1, search.getText());
                    ResultSet set = searcher.executeQuery();
                    while (set.next()) {
                        docGiaList.add(new DocGia(
                                set.getString("madocgia"),
                                set.getString("ten_docgia"),
                                set.getString("thong_tin"),
                                set.getString("ngay_giahan"),
                                set.getString("ngay_hethan"),
                                set.getString("ghi_chu")
                        ));
                    }
                }
            } else if (i == 3) {
                // Tìm kiếm theo ngày hết hạn
                String codeSQL = "select * from `danh sách độc giả` where ngay_hethan = ?;";
                try (Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/library", "root", "1234");
                     PreparedStatement searcher = connection.prepareStatement(codeSQL)) {
                    searcher.setString(1, search.getText());
                    ResultSet set = searcher.executeQuery();
                    while (set.next()) {
                        docGiaList.add(new DocGia(
                                set.getString("madocgia"),
                                set.getString("ten_docgia"),
                                set.getString("thong_tin"),
                                set.getString("ngay_giahan"),
                                set.getString("ngay_hethan"),
                                set.getString("ghi_chu")
                        ));
                    }
                }
            } else {
                showAlert("Chưa chọn loại tìm kiếm", "Lỗi", "Lỗi");
            }

            // Cập nhật TableView với danh sách docGiaList
            bang_thong_tin_doc_gia.setItems(docGiaList);

            // Hiển thị thông báo cho người dùng nếu có kết quả
            if (!docGiaList.isEmpty()) {
                showAlert("Thông báo", "Tìm kiếm thành công", "Đã tìm thấy kết quả.");
            } else {
                showAlert("Thông báo", "Không có kết quả", "Không tìm thấy kết quả phù hợp.");
            }
        }
    }


    @FXML
    void click_sua(MouseEvent event) throws IOException, SQLException {
        // Lấy dòng được chọn trong bảng
        DocGia selectedDocGia = bang_thong_tin_doc_gia.getSelectionModel().getSelectedItem();

        if (selectedDocGia == null) {
            showAlert("Lỗi", "Chưa chọn độc giả", "Vui lòng chọn một độc giả để sửa.");
            return; // Nếu không có độc giả nào được chọn, không thực hiện gì
        }

        // Tạo hộp thoại sửa thông tin
        Dialog<DocGia> dialog = new Dialog<>();
        dialog.setTitle("Sửa thông tin độc giả");
        dialog.setHeaderText("Chỉnh sửa thông tin độc giả");

        // Nút "Save" và "Cancel"
        ButtonType saveButtonType = new ButtonType("Lưu", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(saveButtonType, ButtonType.CANCEL);

        // Giao diện chỉnh sửa
        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);

        TextField tenDocGiaField = new TextField(selectedDocGia.getTen_docgia());
        TextField thongTinField = new TextField(selectedDocGia.getThong_tin());
        TextField ngayGiaHanField = new TextField(selectedDocGia.getNgay_giahan());
        TextField ngayHetHanField = new TextField(selectedDocGia.getNgay_hethan());
        TextField ghiChuField = new TextField(selectedDocGia.getGhi_chu());

        grid.add(new Label("Tên độc giả:"), 0, 0);
        grid.add(tenDocGiaField, 1, 0);
        grid.add(new Label("Thông tin:"), 0, 1);
        grid.add(thongTinField, 1, 1);
        grid.add(new Label("Ngày gia hạn:"), 0, 2);
        grid.add(ngayGiaHanField, 1, 2);
        grid.add(new Label("Ngày hết hạn:"), 0, 3);
        grid.add(ngayHetHanField, 1, 3);
        grid.add(new Label("Ghi chú:"), 0, 4);
        grid.add(ghiChuField, 1, 4);

        dialog.getDialogPane().setContent(grid);

        // Lấy thông tin khi người dùng bấm "Save"
        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == saveButtonType) {
                selectedDocGia.setTen_docgia(tenDocGiaField.getText());
                selectedDocGia.setThong_tin(thongTinField.getText());
                selectedDocGia.setNgay_giahan(ngayGiaHanField.getText());
                selectedDocGia.setNgay_hethan(ngayHetHanField.getText());
                selectedDocGia.setGhi_chu(ghiChuField.getText());
                return selectedDocGia;
            }
            return null;
        });

        // Hiển thị hộp thoại
        dialog.showAndWait().ifPresent(updatedDocGia -> {
            // Cập nhật TableView
            bang_thong_tin_doc_gia.refresh();

            // Cập nhật cơ sở dữ liệu
            try {
                updateDocGiaInDatabase(updatedDocGia);
            } catch (SQLException e) {
                e.printStackTrace();
                showAlert("Lỗi", "Cập nhật thất bại", "Có lỗi xảy ra khi cập nhật cơ sở dữ liệu.");
            }
        });
    }


    @FXML
    void click_thoat(MouseEvent event) throws IOException {

        // Nếu tất cả hợp lệ, mở trang kế tiếp
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("trangchuquanlidocgia.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 600, 580);
        Stage stage = new Stage();
        stage.setTitle("trang chủ quản lí độc giả!");
        stage.setScene(scene);
        stage.show();

        // Thoát trang hiện tại
        ((Node) (event.getSource())).getScene().getWindow().hide();
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        ObservableList<String> phuongphap = FXCollections.observableArrayList(
                "Tìm kiếm theo mã thẻ",
                "Tìm kiếm theo tên",
                "Tìm kiếm theo ngày gia hạn",
                "Tìm kiếm theo ngày hết hạn");
        // Cập nhật cột trong TableView
         madocgia.setCellValueFactory(new PropertyValueFactory<>("madocgia"));
        ten_docgia.setCellValueFactory(new PropertyValueFactory<>("ten_docgia"));
        thong_tin.setCellValueFactory(new PropertyValueFactory<>("thong_tin"));
        ngay_giahan.setCellValueFactory(new PropertyValueFactory<>("ngay_giahan"));
        ngay_hethan.setCellValueFactory(new PropertyValueFactory<>("ngay_hethan"));
        ghi_chu.setCellValueFactory(new PropertyValueFactory<>("ghi_chu"));

        ;
        loai_search.setItems(phuongphap);
    }
    public static void showAlert(String title, String header, String content) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(header);
        alert.setContentText(content);
        alert.showAndWait();
    }

    private void updateDocGiaInDatabase(DocGia docGia) throws SQLException {
        String updateSQL = "UPDATE `danh sách độc giả` SET " +
                "ten_docgia = ?, " +
                "thong_tin = ?, " +
                "ngay_giahan = ?, " +
                "ngay_hethan = ?, " +
                "ghi_chu = ? " +
                "WHERE madocgia = ?";

        try (Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/library", "root", "1234");
             PreparedStatement statement = connection.prepareStatement(updateSQL)) {

            statement.setString(1, docGia.getTen_docgia());
            statement.setString(2, docGia.getThong_tin());
            statement.setString(3, docGia.getNgay_giahan());
            statement.setString(4, docGia.getNgay_hethan());
            statement.setString(5, docGia.getGhi_chu());
            statement.setString(6, docGia.getMadocgia());

            // Thực thi câu lệnh cập nhật
            int rowsUpdated = statement.executeUpdate();
            if (rowsUpdated > 0) {
                showAlert("Thông báo", "Cập nhật thành công", "Thông tin độc giả đã được cập nhật.");
            } else {
                showAlert("Lỗi", "Cập nhật thất bại", "Không có thay đổi nào được thực hiện.");
            }
        }
    }

    @FXML
    void click_in(MouseEvent event) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("danhsachkhithemdocgia.fxml"));
        Scene scene = new Scene(fxmlLoader.load());

        // Khởi tạo Stage mới
        Stage stage = new Stage();
        stage.setTitle("Hello!");
        stage.setScene(scene);
        stage.show();

        // Đóng cửa sổ hiện tại
        ((Node) (event.getSource())).getScene().getWindow().hide();
    }

}


