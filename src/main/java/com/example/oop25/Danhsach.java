//package com.example.oop25;
//
//import com.google.zxing.BarcodeFormat;
//import javafx.fxml.FXML;
//import javafx.fxml.FXMLLoader;
//import javafx.scene.Node;
//import javafx.scene.Parent;
//import javafx.scene.Scene;
//import javafx.scene.control.*;
//import javafx.scene.control.cell.PropertyValueFactory;
//import javafx.scene.input.MouseEvent;
//import javafx.collections.FXCollections;
//import javafx.collections.ObservableList;
//import javafx.stage.Stage;
//import javafx.scene.image.Image;
//import javafx.scene.image.ImageView;
//import javafx.scene.layout.StackPane;
//
//import com.google.zxing.qrcode.QRCodeWriter;
//import com.google.zxing.common.BitMatrix;
//import java.awt.image.BufferedImage;
//import javafx.embed.swing.SwingFXUtils;
//
//import java.io.IOException;
//import java.sql.*;
//import java.util.logging.Level;
//import java.util.logging.Logger;
//
//public class Danhsach {
//
//    @FXML
//    private TableColumn<Sach, Integer> colISBN;
//
//    @FXML
//    private TableColumn<Sach, String> colTenSach;
//
//    @FXML
//    private TableColumn<Sach, String> colTenTacGia;
//
//    @FXML
//    private TableColumn<Sach, String> colNXB;
//
//    @FXML
//    private TableColumn<Sach, Integer> colSoLuongHienCon;
//
//    @FXML
//    private TableColumn<Sach, Integer> colSoLuongMuon;
//
//    @FXML
//    private TableColumn<Sach, Void> colChiTiet;
//
//    @FXML
//    private TableView<Sach> tableViewBooks;
//
//    private ObservableList<Sach> bookList;
//
//    private final String DB_URL = "jdbc:mysql://localhost:3306/library";
//    private final String DB_USER = "root";
//    private final String DB_PASSWORD = "1234";
//
//    @FXML
//    void click_in(MouseEvent event) {
//        // Chức năng in danh sách sách
//        System.out.println("In danh sách sách!");
//    }
//
//    @FXML
//    void click_quaylaiquanlysach(MouseEvent event) {
//        try {
//            FXMLLoader loader = new FXMLLoader(getClass().getResource("QuanLySach.fxml"));
//            Parent root = loader.load();
//            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
//            stage.setScene(new Scene(root));
//            stage.setTitle("Quản lý sách");
//            stage.show();
//        } catch (IOException e) {
//            e.printStackTrace();
//            showAlert(Alert.AlertType.ERROR, "Lỗi", "Không thể quay lại giao diện Quản lý sách!");
//        }
//    }
//
//    @FXML
//    public void initialize() {
//        // Đặt dữ liệu cho từng cột
//        colISBN.setCellValueFactory(new PropertyValueFactory<>("isbn"));
//        colTenSach.setCellValueFactory(new PropertyValueFactory<>("tenSach"));
//        colTenTacGia.setCellValueFactory(new PropertyValueFactory<>("tenTacGia"));
//        colNXB.setCellValueFactory(new PropertyValueFactory<>("NXB"));
//        colSoLuongHienCon.setCellValueFactory(new PropertyValueFactory<>("soLuongHienCon"));
//        colSoLuongMuon.setCellValueFactory(new PropertyValueFactory<>("soLuongMuon"));
//
//        // Thêm nút vào cột Chi Tiết
//        addButtonToTable();
//
//        // Tải dữ liệu sách
//        loadBooksFromDatabase();
//    }
//
//    private void addButtonToTable() {
//        colChiTiet.setCellFactory(param -> new TableCell<>() {
//            private final Button btn = new Button("Chi tiết");
//
//            {
//                btn.setOnAction(event -> {
//                    // Lấy thông tin sách từ hàng hiện tại
//                    Sach sach = getTableView().getItems().get(getIndex());
//                    try {
//                        // Tạo mã QR từ thông tin sách
//                        BufferedImage bufferedQrImage = generateQRCode(
//                                "ISBN: " + sach.getIsbn() + "\nTên sách: " + sach.getTenSach(),
//                                200, 200
//                        );
//
//                        // Chuyển đổi BufferedImage thành JavaFX Image
//                        Object SwingFXUtils = null;
//                        Image qrImage = Image.fromPlatformImage(SwingFXUtils.toString());
//
//                        // Hiển thị mã QR trong cửa sổ mới
//                        showQRCode(qrImage);
//
//                    } catch (Exception e) {
//                        e.printStackTrace();
//                        showAlert(Alert.AlertType.ERROR, "Lỗi", "Không thể tạo mã QR cho sách!");
//                    }
//                });
//            }
//
//            @Override
//            protected void updateItem(Void item, boolean empty) {
//                super.updateItem(item, empty);
//                if (empty) {
//                    setGraphic(null);
//                } else {
//                    setGraphic(btn);
//                }
//            }
//        });
//    }
//
//    private void showQRCode(Image qrImage) {
//        // Tạo một Stage mới để hiển thị mã QR
//        Stage qrStage = new Stage();
//        qrStage.setTitle("Mã QR của sách");
//
//        // Hiển thị mã QR trong ImageView (JavaFX)
//        ImageView imageView = new ImageView(qrImage);
//        imageView.setFitWidth(200);
//        imageView.setFitHeight(200);
//
//        // Đặt Scene và hiển thị
//        Scene scene = new Scene(new StackPane(imageView), 250, 250);
//        qrStage.setScene(scene);
//        qrStage.show();
//    }
//
//    private BufferedImage generateQRCode(String text, int width, int height) throws Exception {
//        QRCodeWriter qrCodeWriter = new QRCodeWriter();
//        BitMatrix bitMatrix = qrCodeWriter.encode(text, BarcodeFormat.QR_CODE, width, height);
//
//        BufferedImage bufferedImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
//        for (int x = 0; x < width; x++) {
//            for (int y = 0; y < height; y++) {
//                bufferedImage.setRGB(x, y, bitMatrix.get(x, y) ? 0x000000 : 0xFFFFFF); // Black and white pixels
//            }
//        }
//        return bufferedImage;
//    }
//
//    private void loadBooksFromDatabase() {
//        bookList = FXCollections.observableArrayList();
//        String query = "SELECT * FROM sach";
//
//        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
//             Statement stmt = conn.createStatement();
//             ResultSet rs = stmt.executeQuery(query)) {
//
//            while (rs.next()) {
//                bookList.add(new Sach(
//                        rs.getString("ISBN"),
//                        rs.getString("ten_sach"),
//                        rs.getString("ten_tac_gia"),
//                        rs.getString("NXB"),
//                        rs.getInt("so_luong_hien_con"),
//                        rs.getInt("so_luong_muon")
//                ));
//            }
//
//            tableViewBooks.setItems(bookList);
//
//        } catch (SQLException e) {
//            e.printStackTrace();
//            showAlert(Alert.AlertType.ERROR, "Lỗi", "Không thể tải dữ liệu từ cơ sở dữ liệu!");
//        }
//    }
//
//    private void showAlert(Alert.AlertType alertType, String title, String message) {
//        Alert alert = new Alert(alertType);
//        alert.setTitle(title);
//        alert.setContentText(message);
//        alert.showAndWait();
//    }
//}
