package com.example.oop25;

public class SachMuon {
    private int stt;
    private String maSach;
    private String tenSach;
    private int soLuongMuon;

    public SachMuon(int stt, String maSach, String tenSach, int soLuongMuon) {
        this.stt = stt;
        this.maSach = maSach;
        this.tenSach = tenSach;
        this.soLuongMuon = soLuongMuon;
    }

    // Getter và Setter
    public int getStt() {
        return stt;
    }

    public void setStt(int stt) {
        this.stt = stt;
    }

    public String getMaSach() {
        return maSach;
    }

    public void setMaSach(String maSach) {
        this.maSach = maSach;
    }

    public String getTenSach() {
        return tenSach;
    }

    public void setTenSach(String tenSach) {
        this.tenSach = tenSach;
    }

    public int getSoLuongMuon() {
        return soLuongMuon;
    }

    public void setSoLuongMuon(int soLuongMuon) {
        this.soLuongMuon = soLuongMuon;
    }
}