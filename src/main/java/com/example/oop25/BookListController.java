// Danh Sach - của quan li sach

package com.example.oop25;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.stage.Stage;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.qrcode.QRCodeWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import javafx.embed.swing.SwingFXUtils;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.sql.*;
import java.util.Hashtable;

public class BookListController {

    @FXML
    private TableColumn<Book, String> colIsbn;

    @FXML
    private TableColumn<Book, String> colBookTitle;

    @FXML
    private TableColumn<Book, String> colAuthor;

    @FXML
    private TableColumn<Book, String> colPublisher;

    @FXML
    private TableColumn<Book, Integer> colAvailableCopies;

    @FXML
    private TableColumn<Book, Integer> colBorrowedCopies;

    @FXML
    private TableColumn<Book, Void> colShowQrCode;

    @FXML
    private TableView<Book> bookTableView;

    private final ObservableList<Book> bookList = FXCollections.observableArrayList();

    private final String DB_URL = "jdbc:mysql://localhost:3306/library";
    private final String DB_USER = "root";
    private final String DB_PASSWORD = "1234";

    @FXML
    public void initialize() {
        setupTableColumns();
        loadBooksFromDatabase();
    }

    private void setupTableColumns() {
        colIsbn.setCellValueFactory(new PropertyValueFactory<>("bookId"));
        colBookTitle.setCellValueFactory(new PropertyValueFactory<>("bookTitle"));
        colAuthor.setCellValueFactory(new PropertyValueFactory<>("authorName"));
        colPublisher.setCellValueFactory(new PropertyValueFactory<>("publisher"));
        colAvailableCopies.setCellValueFactory(new PropertyValueFactory<>("availableCopies"));
        colBorrowedCopies.setCellValueFactory(new PropertyValueFactory<>("borrowedCopies"));

        colShowQrCode.setCellFactory(param -> new TableCell<>() {
            private final Button qrButton = new Button("Mã QR");

            {
                qrButton.setOnAction(event -> {
                    Book book = getTableView().getItems().get(getIndex());
                    showQrCode(book);
                });
            }

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setGraphic(null);
                } else {
                    setGraphic(qrButton);
                }
            }
        });

        bookTableView.setItems(bookList);
    }

    private void loadBooksFromDatabase() {
        try (Connection connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
             Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery("SELECT * FROM `thông tin sách`")) {

            while (resultSet.next()) {
                String bookId = resultSet.getString("ISBN");
                String bookTitle = resultSet.getString("ten_sach");
                String authorName = resultSet.getString("ten_tac_gia");
                String publisher = resultSet.getString("NXB");
                int availableCopies = resultSet.getInt("so_luong_hien_con");
                int borrowedCopies = resultSet.getInt("so_luong_muon");

                bookList.add(new Book(bookId, bookTitle, authorName, publisher, availableCopies, borrowedCopies) {
                    @Override
                    public int getAvailableQuantity() {
                        return 0;
                    }

                    @Override
                    public int getBorrowedQuantity() {
                        return 0;
                    }

                    @Override
                    public void setAvailableQuantity(int availableQuantity) {

                    }
                });
            }
        } catch (SQLException e) {
            showAlert(Alert.AlertType.ERROR, "Lỗi kết nối cơ sở dữ liệu", "Không thể tải dữ liệu từ cơ sở dữ liệu. Vui lòng kiểm tra kết nối!");
        }
    }

    private void showQrCode(Book book) {
        Alert qrAlert = new Alert(Alert.AlertType.INFORMATION);
        qrAlert.setTitle("Mã QR");
        qrAlert.setHeaderText("Mã QR cho sách: " + book.getBookTitle());

        // Sử dụng ZXing để tạo mã QR đẹp
        Image qrImage = generateQrCodeWithZxing(book.getBookId());
        ImageView imageView = new ImageView(qrImage);
        qrAlert.setGraphic(imageView);

        qrAlert.showAndWait();
    }

    private Image generateQrCodeWithZxing(String data) {
        try {
            // Cấu hình cho mã QR
            Hashtable<EncodeHintType, Object> hints = new Hashtable<>();
            hints.put(EncodeHintType.MARGIN, 1); // Set margin to 1

            // Tạo QR Code
            QRCodeWriter qrCodeWriter = new QRCodeWriter();
            BitMatrix bitMatrix = qrCodeWriter.encode(data, BarcodeFormat.QR_CODE, 200, 200, hints);

            // Chuyển đổi BitMatrix thành BufferedImage
            BufferedImage bufferedImage = new BufferedImage(200, 200, BufferedImage.TYPE_INT_RGB);
            for (int x = 0; x < 200; x++) {
                for (int y = 0; y < 200; y++) {
                    bufferedImage.setRGB(x, y, bitMatrix.get(x, y) ? Color.BLACK.getRGB() : Color.WHITE.getRGB());
                }
            }

            return SwingFXUtils.toFXImage(bufferedImage, null);
        } catch (WriterException e) {
            e.printStackTrace();
            return null;
        }
    }

    @FXML
    void onBackToBookManagementClick(MouseEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("Quanlysach.fxml"));
            Parent root = loader.load();
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.setTitle("Quản lý sách");
            stage.show();
        } catch (IOException e) {
            showAlert(Alert.AlertType.ERROR, "Lỗi", "Không thể quay lại màn hình quản lý sách!");
        }
    }

    private void showAlert(Alert.AlertType type, String title, String message) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
