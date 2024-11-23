package com.example.oop25;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.scene.control.Cell;

public class Sach {
    private String isbn;  // Mã ISBN
    private String tenSach;
    private String tenTacGia;
    private String nxb;
    private int soLuongHienCon;
    private int soLuongMuon;
    private BooleanProperty chon;  // Sử dụng BooleanProperty thay vì CheckBox

    // Constructor
    public Sach(int isbn, String tenSach, String tenTacGia, String nxb, int soLuongHienCon, int soLuongMuon) {
        this.isbn = String.valueOf(isbn);
        this.tenSach = tenSach;
        this.tenTacGia = tenTacGia;
        this.nxb = nxb;
        this.soLuongHienCon = soLuongHienCon;
        this.soLuongMuon = soLuongMuon;
        this.chon = new SimpleBooleanProperty(false);  // Giá trị mặc định là false (unchecked)
    }

    // Getters and Setters
    public String getIsbn() {
        return isbn;
    }

    public void setIsbn(String isbn) {
        this.isbn = isbn;
    }

    public String getTenSach() {
        return tenSach;
    }

    public void setTenSach(String tenSach) {
        this.tenSach = tenSach;
    }

    public String getTenTacGia() {
        return tenTacGia;
    }

    public void setTenTacGia(String tenTacGia) {
        this.tenTacGia = tenTacGia;
    }

    public String getNxb() {
        return nxb;
    }

    public void setNxb(String nxb) {
        this.nxb = nxb;
    }

    public int getSoLuongHienCon() {
        return soLuongHienCon;
    }

    public void setSoLuongHienCon(int soLuongHienCon) {
        this.soLuongHienCon = soLuongHienCon;
    }

    public int getSoLuongMuon() {
        return soLuongMuon;
    }

    public void setSoLuongMuon(int soLuongMuon) {
        this.soLuongMuon = soLuongMuon;
    }

    // Getter cho chon (BooleanProperty)
    public BooleanProperty chonProperty() {
        return chon;
    }

    public boolean isChon() {
        return chon.get();
    }

    public void setChon(boolean chon) {
        this.chon.set(chon);
    }

    // Method to display object info (for debugging)
    @Override
    public String toString() {
        return "Sach{" +
                "isbn='" + isbn + '\'' +
                ", tenSach='" + tenSach + '\'' +
                ", tenTacGia='" + tenTacGia + '\'' +
                ", nxb='" + nxb + '\'' +
                ", soLuongHienCon=" + soLuongHienCon +
                ", soLuongMuon=" + soLuongMuon +
                ", chon=" + chon.get() +
                '}';
    }

    public Cell<Object> getChon() {
        return null;
    }
}
