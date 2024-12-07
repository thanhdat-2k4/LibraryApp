//// tim kiem doc gia

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

public class ReaderSearch implements Initializable {

    @FXML
    private TextField searchField;

    @FXML
    private ComboBox<String> searchTypeComboBox;

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
    void handleBackClick(MouseEvent event) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("danhsachkhithemdocgia.fxml"));
        Scene scene = new Scene(fxmlLoader.load());

        Stage stage = new Stage();
        stage.setTitle("Trang chủ!");
        stage.setScene(scene);
        stage.show();

        // Đóng cửa sổ hiện tại
        ((Node) (event.getSource())).getScene().getWindow().hide();
    }

    @FXML
    void handleSearchInput(KeyEvent event) throws SQLException {
        if (event.getCode() == KeyCode.ENTER) {
            int selectedSearchType = searchTypeComboBox.getSelectionModel().getSelectedIndex();

            ObservableList<Reader> readerList = FXCollections.observableArrayList();

            String query = "";
            if (selectedSearchType == 0) {
                // Tìm kiếm theo mã độc giả
                query = "SELECT * FROM `danh sách độc giả` WHERE madocgia = ?;";
            } else if (selectedSearchType == 1) {
                // Tìm kiếm theo tên độc giả
                query = "SELECT * FROM `danh sách độc giả` WHERE ten_docgia = ?;";
            } else if (selectedSearchType == 2) {
                // Tìm kiếm theo ngày gia hạn
                query = "SELECT * FROM `danh sách độc giả` WHERE ngay_giahan = ?;";
            } else if (selectedSearchType == 3) {
                // Tìm kiếm theo ngày hết hạn
                query = "SELECT * FROM `danh sách độc giả` WHERE ngay_hethan = ?;";
            } else {
                showAlert("Chưa chọn loại tìm kiếm", "Lỗi", "Lỗi");
                return;
            }

            try (Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/library", "root", "1234");
                 PreparedStatement searchStatement = connection.prepareStatement(query)) {
                searchStatement.setString(1, searchField.getText());
                ResultSet resultSet = searchStatement.executeQuery();
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

            // Cập nhật TableView với kết quả tìm kiếm
            readerTableView.setItems(readerList);

            if (!readerList.isEmpty()) {
                showAlert("Thông báo", "Tìm kiếm thành công", "Đã tìm thấy kết quả.");
            } else {
                showAlert("Thông báo", "Không có kết quả", "Không tìm thấy kết quả phù hợp.");
            }
        }
    }

    @FXML
    void handleEditClick(MouseEvent event) throws IOException, SQLException {
        Reader selectedReader = readerTableView.getSelectionModel().getSelectedItem();

        if (selectedReader == null) {
            showAlert("Lỗi", "Chưa chọn độc giả", "Vui lòng chọn một độc giả để sửa.");
            return;
        }

        Dialog<Reader> dialog = new Dialog<>();
        dialog.setTitle("Sửa thông tin độc giả");
        dialog.setHeaderText("Chỉnh sửa thông tin độc giả");

        ButtonType saveButtonType = new ButtonType("Lưu", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(saveButtonType, ButtonType.CANCEL);

        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);

        TextField readerNameField = new TextField(selectedReader.getReaderName());
        TextField contactInfoField = new TextField(selectedReader.getInformation());
        TextField renewalDateField = new TextField(selectedReader.getRenewalDate());
        TextField expirationDateField = new TextField(selectedReader.getExpirationDate());
        TextField noteField = new TextField(selectedReader.getNotes());

        grid.add(new Label("Tên độc giả:"), 0, 0);
        grid.add(readerNameField, 1, 0);
        grid.add(new Label("Thông tin:"), 0, 1);
        grid.add(contactInfoField, 1, 1);
        grid.add(new Label("Ngày gia hạn:"), 0, 2);
        grid.add(renewalDateField, 1, 2);
        grid.add(new Label("Ngày hết hạn:"), 0, 3);
        grid.add(expirationDateField, 1, 3);
        grid.add(new Label("Ghi chú:"), 0, 4);
        grid.add(noteField, 1, 4);

        dialog.getDialogPane().setContent(grid);

        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == saveButtonType) {
                selectedReader.setReaderName(readerNameField.getText());
                selectedReader.setInformation(contactInfoField.getText());
                selectedReader.setRenewalDate(renewalDateField.getText());
                selectedReader.setExpirationDate(expirationDateField.getText());
                selectedReader.setNotes(noteField.getText());
                return selectedReader;
            }
            return null;
        });

        dialog.showAndWait().ifPresent(updatedReader -> {
            readerTableView.refresh();
            try {
                updateReaderInDatabase(updatedReader);
            } catch (SQLException e) {
                e.printStackTrace();
                showAlert("Lỗi", "Cập nhật thất bại", "Có lỗi xảy ra khi cập nhật cơ sở dữ liệu.");
            }
        });
    }

    @FXML
    void handleExitClick(MouseEvent event) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("trangchuquanlidocgia.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        Stage stage = new Stage();
        stage.setTitle("Trang chủ quản lý độc giả!");
        stage.setScene(scene);
        stage.show();

        ((Node) (event.getSource())).getScene().getWindow().hide();
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        ObservableList<String> searchMethods = FXCollections.observableArrayList(
                "Tìm kiếm theo mã thẻ",
                "Tìm kiếm theo tên",
                "Tìm kiếm theo ngày gia hạn",
                "Tìm kiếm theo ngày hết hạn");

        columnReaderId.setCellValueFactory(new PropertyValueFactory<>("readerId"));
        columnReaderName.setCellValueFactory(new PropertyValueFactory<>("readerName"));
        columnContactInfo.setCellValueFactory(new PropertyValueFactory<>("contactInfo"));
        columnRenewalDate.setCellValueFactory(new PropertyValueFactory<>("renewalDate"));
        columnExpirationDate.setCellValueFactory(new PropertyValueFactory<>("expirationDate"));
        columnNote.setCellValueFactory(new PropertyValueFactory<>("note"));

        try {
            loadData();
        } catch (SQLException e) {
            e.printStackTrace();
            showAlert("Lỗi", "Lỗi khi tải dữ liệu", "Có lỗi khi tải dữ liệu độc giả.");
        }

        searchTypeComboBox.setItems(searchMethods);
    }

    public static void showAlert(String title, String header, String content) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(header);
        alert.setContentText(content);
        alert.showAndWait();
    }

    private void updateReaderInDatabase(Reader reader) throws SQLException {
        String query = "UPDATE `danh sách độc giả` SET ten_docgia = ?, thong_tin = ?, ngay_giahan = ?, ngay_hethan = ?, ghi_chu = ? WHERE madocgia = ?";

        try (Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/library", "root", "1234");
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {

            preparedStatement.setString(1, reader.getReaderName());
            preparedStatement.setString(2, reader.getInformation());
            preparedStatement.setString(3, reader.getRenewalDate());
            preparedStatement.setString(4, reader.getExpirationDate());
            preparedStatement.setString(5, reader.getNotes());
            preparedStatement.setString(6, reader.getReaderId());

            preparedStatement.executeUpdate();
        }
    }

    private void loadData() throws SQLException {
        ObservableList<Reader> readerList = FXCollections.observableArrayList();
        String query = "SELECT * FROM `danh sách độc giả`";

        try (Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/library", "root", "1234");
             PreparedStatement statement = connection.prepareStatement(query);
             ResultSet resultSet = statement.executeQuery()) {

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

            readerTableView.setItems(readerList);
        }
    }
}
