package com.example.oop25;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;

public class ThongTinCaNhan {

    @FXML
    private TextField textFieldMaNV;
    @FXML
    private TextField textFieldHoTen;
    @FXML
    private TextField textFieldNgaySinh;
    @FXML
    private TextField textFieldDiaChi;
    @FXML
    private TextField textFieldSDT;
    @FXML
    private TextField textFieldCCCD;

    // Phương thức để thiết lập thông tin nhân viên
    public void setThongTin(NhanVien nhanVien) {
        textFieldMaNV.setText(nhanVien.getMaNV());
        textFieldHoTen.setText(nhanVien.getHoTen());
        textFieldNgaySinh.setText(nhanVien.getNgaySinh());
        textFieldDiaChi.setText(nhanVien.getDiaChi());
        textFieldSDT.setText(nhanVien.getSdt());
        textFieldCCCD.setText(nhanVien.getCccd());
    }

    @FXML
    public void click_quaylai(ActionEvent actionEvent) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("danhsachnhanvien.fxml"));
            Scene scene = new Scene(fxmlLoader.load(), 1000, 600);
            Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
            stage.setTitle("Danh Sách Nhân Viên");
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            // Bạn có thể hiển thị thông báo lỗi cho người dùng ở đây
        }
    }
}