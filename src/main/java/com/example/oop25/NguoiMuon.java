package com.example.oop25;

public class NguoiMuon {
    private int stt;
    private String maTheDocGia;
    private String tenDocGia;
    private String tenSachMuon;
    private String tinhTrangMuon;

    public NguoiMuon(int stt, String maTheDocGia, String tenDocGia, String tenSachMuon, String tinhTrangMuon) {
        this.stt = stt;
        this.maTheDocGia = maTheDocGia;
        this.tenDocGia = tenDocGia;
        this.tenSachMuon = tenSachMuon;
        this.tinhTrangMuon = tinhTrangMuon;
    }

    // Getter và Setter
    public int getStt() {
        return stt;
    }

    public String getMaTheDocGia() {
        return maTheDocGia;
    }

    public String getTenDocGia() {
        return tenDocGia;
    }

    public String getTenSachMuon() {
        return tenSachMuon;
    }

    public String getTinhTrangMuon() {
        return tinhTrangMuon;
    }
}