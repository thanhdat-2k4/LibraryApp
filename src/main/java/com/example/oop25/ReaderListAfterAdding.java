// danh sach sau khi them doc gia

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

public class ReaderListAfterAdding implements Initializable {

    @FXML
    private TableColumn<Reader, String> columnIndex;

    @FXML
    private TableView<Reader> readerTableView;

    @FXML
    private TableColumn<Reader, String> columnReaderId;

    @FXML
    private TableColumn<Reader, String> columnReaderName;

    @FXML
    private TableColumn<Reader, String> columnContactInfo;

    @FXML
    private TableColumn<Reader, String> columnRenewalDate;

    @FXML
    private TableColumn<Reader, String> columnExpirationDate;

    @FXML
    private TableColumn<Reader, String> columnNote;

    @FXML
    void clickBack(MouseEvent event) throws IOException {
        // Quay lại trang chủ quản lý độc giả
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("trangchuquanlidocgia.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 1000, 600);
        Stage stage = new Stage();
        stage.setTitle("Trang chủ quản lý độc giả");
        stage.setScene(scene);
        stage.show();

        // Đóng cửa sổ hiện tại
        ((Node) (event.getSource())).getScene().getWindow().hide();
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        // Tạo ObservableList chứa dữ liệu Reader
        ObservableList<Reader> readerList = FXCollections.observableArrayList();

        // Lấy dữ liệu từ cơ sở dữ liệu
        try (Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/library", "root", "1234")) {
            String query = "SELECT * FROM `danh sách độc giả`";
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(query);

            // Thêm dữ liệu vào ObservableList
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

        // Gán danh sách vào TableView
        readerTableView.setItems(readerList);

        // Cài đặt các cột của TableView
        columnReaderId.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getReaderId()));
        columnReaderName.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getReaderName()));
        columnContactInfo.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getInformation()));
        columnRenewalDate.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getRenewalDate()));
        columnExpirationDate.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getExpirationDate()));
        columnNote.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getNotes()));

        // Thiết lập cột STT (Số thứ tự)
        columnIndex.setCellValueFactory((TableColumn.CellDataFeatures<Reader, String> param) ->
                new SimpleStringProperty(String.valueOf(readerList.indexOf(param.getValue()) + 1))
        );
    }
}
