package com.example.oop25;

import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.sql.*;
import java.util.ResourceBundle;

public class danhsachkhithemdocgia implements Initializable {

    @FXML
    private TableColumn<DocGia, String> STT;  // Chỉ cần khai báo một lần

    @FXML
    private TableView<DocGia> danh_sach_doc_gia;

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
    void click_quaylai(MouseEvent event) throws IOException {
        // về trang chủ qua lí ng dùng.
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("trangchuquanlidocgia.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 1000, 600);
        Stage stage = new Stage();
        stage.setTitle("Trang chủ quản lí độc giả!");
        stage.setScene(scene);
        stage.show();
        // xóa khung

        ((Node)(event.getSource())).getScene().getWindow().hide();
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        // ObservableList chứa dữ liệu DocGia
        ObservableList<DocGia> docGiaList = FXCollections.observableArrayList();

        try (Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/library", "root", "1234")) {
            String query = "SELECT * FROM `danh sách độc giả`";
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(query);

            // Duyệt qua các dòng dữ liệu và thêm vào ObservableList
            while (resultSet.next()) {
                docGiaList.add(new DocGia(
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

        // Gán danh sách vào TableView
        danh_sach_doc_gia.setItems(docGiaList);

        // Thiết lập các cột trong TableView
        madocgia.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getMadocgia()));
        ten_docgia.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getTen_docgia()));
        thong_tin.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getThong_tin()));
        ngay_giahan.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getNgay_giahan()));
        ngay_hethan.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getNgay_hethan()));
        ghi_chu.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getGhi_chu()));

        // Cài đặt cột STT để hiển thị số thứ tự
        STT.setCellValueFactory((TableColumn.CellDataFeatures<DocGia, String> param) ->
                new SimpleStringProperty(String.valueOf(docGiaList.indexOf(param.getValue()) + 1))
        );
    }

}
