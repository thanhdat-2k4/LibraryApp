package com.example.oop25;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class DocGia {
    private StringProperty madocgia;
    private StringProperty ten_docgia;
    private StringProperty thong_tin;
    private StringProperty ngay_giahan;
    private StringProperty ngay_hethan;
    private StringProperty ghi_chu;

    public DocGia(String madocgia, String ten_docgia, String thong_tin, String ngay_giahan, String ngay_hethan, String ghi_chu) {
        this.madocgia = new SimpleStringProperty(madocgia);
        this.ten_docgia = new SimpleStringProperty(ten_docgia);
        this.thong_tin = new SimpleStringProperty(thong_tin);
        this.ngay_giahan = new SimpleStringProperty(ngay_giahan);
        this.ngay_hethan = new SimpleStringProperty(ngay_hethan);
        this.ghi_chu = new SimpleStringProperty(ghi_chu);
    }

    public String getMadocgia() {
        return madocgia.get();
    }

    public void setMadocgia(String madocgia) {
        this.madocgia.set(madocgia);
    }

    public String getTen_docgia() {
        return ten_docgia.get();
    }

    public void setTen_docgia(String ten_docgia) {
        this.ten_docgia.set(ten_docgia);
    }

    public String getThong_tin() {
        return thong_tin.get();
    }

    public void setThong_tin(String thong_tin) {
        this.thong_tin.set(thong_tin);
    }

    public String getNgay_giahan() {
        return ngay_giahan.get();
    }

    public void setNgay_giahan(String ngay_giahan) {
        this.ngay_giahan.set(ngay_giahan);
    }

    public String getNgay_hethan() {
        return ngay_hethan.get();
    }

    public void setNgay_hethan(String ngay_hethan) {
        this.ngay_hethan.set(ngay_hethan);
    }

    public String getGhi_chu() {
        return ghi_chu.get();
    }

    public void setGhi_chu(String ghi_chu) {
        this.ghi_chu.set(ghi_chu);
    }
}
