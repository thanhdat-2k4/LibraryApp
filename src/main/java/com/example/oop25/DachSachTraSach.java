package com.example.oop25;

import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;

public class DachSachTraSach {

    @FXML
    private TableColumn<?, ?> ISBN;

    @FXML
    private TableColumn<?, ?> NXB;

    @FXML
    private TableView<?> danh_sach_tra;

    @FXML
    private ComboBox<?> loai_search;

    @FXML
    private TableColumn<?, ?> ma_phieu;

    @FXML
    private TextField search;

    @FXML
    private TableColumn<?, ?> so_luong_hien_con;

    @FXML
    private TableColumn<?, ?> so_luong_muon;

    @FXML
    private TableColumn<?, ?> ten_sach;

    @FXML
    private TableColumn<?, ?> ten_tac_gia;

    @FXML
    void click_in(MouseEvent event) {

    }

    @FXML
    void click_muon(MouseEvent event) {

    }

    @FXML
    void click_thoat(MouseEvent event) {

    }


    @FXML
    void nhap_search(KeyEvent event) {

    }

}
