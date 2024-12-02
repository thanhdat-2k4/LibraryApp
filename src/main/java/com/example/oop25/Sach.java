//package com.example.oop25;
//
//import com.mysql.cj.conf.IntegerProperty;
//import javafx.beans.property.BooleanProperty;
//import javafx.beans.property.SimpleBooleanProperty;
//import javafx.beans.property.StringProperty;
//
//public class Sach {
//    private String isbn;  // Mã ISBN
//    private String tenSach;
//    private String tenTacGia;
//    private String nxb;
//    private int soLuongHienCon;
//    private int soLuongMuon;
//    private BooleanProperty chon;  // Sử dụng BooleanProperty cho CheckBox
//
//
//    // Constructor
//    public Sach(String isbn, String tenSach, String tenTacGia, String nxb, int soLuongHienCon, int soLuongMuon) {
//        this.isbn = isbn;
//        this.tenSach = tenSach;
//        this.tenTacGia = tenTacGia;
//        this.nxb = nxb;
//        this.soLuongHienCon = soLuongHienCon;
//        this.soLuongMuon = soLuongMuon;
//        this.chon = new SimpleBooleanProperty(false);  // Mặc định là false
//    }
//
//    // Getters và Setters cho các thuộc tính cơ bản
//    public String getIsbn() {
//        return isbn;
//    }
//
//    public void setIsbn(String isbn) {
//        this.isbn = isbn;
//    }
//
//    public String getTenSach() {
//        return tenSach;
//    }
//
//    public void setTenSach(String tenSach) {
//        this.tenSach = tenSach;
//    }
//
//    public String getTenTacGia() {
//        return tenTacGia;
//    }
//
//    public void setTenTacGia(String tenTacGia) {
//        this.tenTacGia = tenTacGia;
//    }
//
//    public String getNxb() {
//        return nxb;
//    }
//
//    public void setNxb(String nxb) {
//        this.nxb = nxb;
//    }
//
//    public int getSoLuongHienCon() {
//        return soLuongHienCon;
//    }
//
//    public void setSoLuongHienCon(int soLuongHienCon) {
//        this.soLuongHienCon = soLuongHienCon;
//    }
//
//    public int getSoLuongMuon() {
//        return soLuongMuon;
//    }
//
//    public void setSoLuongMuon(int soLuongMuon) {
//        this.soLuongMuon = soLuongMuon;
//    }
//
//    // BooleanProperty getter và setter
//    public BooleanProperty chonProperty() {
//        return chon;
//    }
//
//    public boolean isChon() {
//        return chon.get();
//    }
//
//    public void setChon(boolean chon) {
//        this.chon.set(chon);
//    }
//
//    @Override
//    public String toString() {
//        return "Sach{" +
//                "isbn='" + isbn + '\'' +
//                ", tenSach='" + tenSach + '\'' +
//                ", tenTacGia='" + tenTacGia + '\'' +
//                ", nxb='" + nxb + '\'' +
//                ", soLuongHienCon=" + soLuongHienCon +
//                ", soLuongMuon=" + soLuongMuon +
//                '}';
//    }
//}
package com.example.oop25;

import javafx.beans.property.*;

public class Sach {
    private final StringProperty isbn; // ISBN được chuyển thành chuỗi
    private final StringProperty tenSach;
    private final StringProperty tenTacGia;
    private final StringProperty nxb;
    private final IntegerProperty soLuongHienCon;
    private final IntegerProperty soLuongMuon;
    private BooleanProperty chon;  // Sử dụng BooleanProperty cho CheckBox
    // Constructor
    public Sach(String isbn, String tenSach, String tenTacGia, String nxb, int soLuongHienCon, int soLuongMuon) {
        this.isbn = new SimpleStringProperty(isbn); // ISBN là chuỗi
        this.tenSach = new SimpleStringProperty(tenSach);
        this.tenTacGia = new SimpleStringProperty(tenTacGia);
        this.nxb = new SimpleStringProperty(nxb);
        this.soLuongHienCon = new SimpleIntegerProperty(soLuongHienCon);
        this.soLuongMuon = new SimpleIntegerProperty(soLuongMuon);
        this.chon = new SimpleBooleanProperty(false);  // Mặc định là false
    }

    // Getters
    public String getIsbn() {
        return isbn.get();
    }

    public String getTenSach() {
        return tenSach.get();
    }

    public String getTenTacGia() {
        return tenTacGia.get();
    }

    public String getNxb() {
        return nxb.get();
    }

    public int getSoLuongHienCon() {
        return soLuongHienCon.get();
    }

    public int getSoLuongMuon() {
        return soLuongMuon.get();
    }

    // Properties
    public StringProperty isbnProperty() {
        return isbn;
    }

    public StringProperty tenSachProperty() {
        return tenSach;
    }

    public StringProperty tenTacGiaProperty() {
        return tenTacGia;
    }

    public StringProperty nxbProperty() {
        return nxb;
    }

    public IntegerProperty soLuongHienConProperty() {
        return soLuongHienCon;
    }

    public IntegerProperty soLuongMuonProperty() {
        return soLuongMuon;
    }

    // Setters (Optional, nếu bạn cần thay đổi giá trị của các thuộc tính này)
    public void setIsbn(String isbn) {
        this.isbn.set(isbn);
    }

    public void setTenSach(String tenSach) {
        this.tenSach.set(tenSach);
    }

    public void setTenTacGia(String tenTacGia) {
        this.tenTacGia.set(tenTacGia);
    }

    public void setNxb(String nxb) {
        this.nxb.set(nxb);
    }

    public void setSoLuongHienCon(int soLuongHienCon) {
        this.soLuongHienCon.set(soLuongHienCon);
    }

    public void setSoLuongMuon(int soLuongMuon) {
        this.soLuongMuon.set(soLuongMuon);
    }

    // BooleanProperty getter và setter
    public BooleanProperty chonProperty() {
        return chon;
    }

    public boolean isChon() {
        return chon.get();
    }

    public void setChon(boolean chon) {
        this.chon.set(chon);
    }
}