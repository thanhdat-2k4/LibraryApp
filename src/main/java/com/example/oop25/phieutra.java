package com.example.oop25;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.*;

public class phieutra {

    @FXML
    private TextField madocgia;

    @FXML
    private DatePicker maphieu;


    public static void showAlert(String thôngBáo, String s, String s1) {
    }


    @FXML
    void click_quaylai(MouseEvent event) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("trangchuquanlidocgia.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 600, 580);
        Stage stage = new Stage();
        stage.setScene(scene);
        stage.setTitle("Trang chủ quản lí độc giả  !");
        stage.show();
    }


    @FXML
    void click_ktra(MouseEvent event) {
        String madocgiaValue = madocgia.getText(); // Get the text from the TextField
        if (madocgiaValue == null || madocgiaValue.trim().isEmpty()) {
            showAlert("Error", "Invalid Input", "Mã đọc giả không được để trống.");
            return;
        }

        // MySQL connection details
        String jdbcURL = "jdbc:mysql://localhost:3306/your_database_name"; // Replace with your DB details
        String username = "your_username"; // Replace with your DB username
        String password = "your_password"; // Replace with your DB password

        String query = "SELECT COUNT(*) FROM your_table_name WHERE madocgia = ?"; // Replace with your table and column names

        try (Connection connection = DriverManager.getConnection(jdbcURL, username, password);
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {

            // Set the parameter for the query
            preparedStatement.setString(1, madocgiaValue);

            // Execute the query
            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                int count = resultSet.getInt(1);
                if (count > 0) {
                    showAlert("Success", "Valid Reader", "Mã đọc giả hợp lệ.");
                } else {
                    showAlert("Error", "Invalid Reader", "Mã đọc giả không tồn tại.");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            showAlert("Error", "Database Error", "Không thể kết nối đến cơ sở dữ liệu.");
        }
    }


}
