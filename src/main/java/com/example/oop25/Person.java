package com.example.oop25;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class Person {
    private StringProperty maNV;
    private StringProperty hoTen;
    private StringProperty ngaySinh;
    private StringProperty diaChi;
    private StringProperty sdt;

    // Constructor
    public Person(String maNV, String hoTen, String ngaySinh, String diaChi, String sdt) {
        this.maNV = new SimpleStringProperty(maNV);
        this.hoTen = new SimpleStringProperty(hoTen);
        this.ngaySinh = new SimpleStringProperty(ngaySinh);
        this.diaChi = new SimpleStringProperty(diaChi);
        this.sdt = new SimpleStringProperty(sdt);
    }

    // Getter and Setter methods
    public String getMaNV() {
        return maNV.get();
    }

    public void setMaNV(String maNV) {
        this.maNV.set(maNV);
    }

    public String getHoTen() {
        return hoTen.get();
    }

    public void setHoTen(String hoTen) {
        this.hoTen.set(hoTen);
    }

    public String getNgaySinh() {
        return ngaySinh.get();
    }

    public void setNgaySinh(String ngaySinh) {
        this.ngaySinh.set(ngaySinh);
    }

    public String getDiaChi() {
        return diaChi.get();
    }

    public void setDiaChi(String diaChi) {
        this.diaChi.set(diaChi);
    }

    public String getSdt() {
        return sdt.get();
    }

    public void setSdt(String sdt) {
        this.sdt.set(sdt);
    }
}
