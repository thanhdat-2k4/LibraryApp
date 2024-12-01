package com.example.oop25;

public class SachConLai {
    private int stt;
    private String maSach;
    private String tenSach;
    private int soLuongConLai;

    public SachConLai(int stt, String maSach, String tenSach, int soLuongConLai) {
        this.stt = stt;
        this.maSach = maSach;
        this.tenSach = tenSach;
        this.soLuongConLai = soLuongConLai;
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

    public int getSoLuongConLai() {
        return soLuongConLai;
    }

    public void setSoLuongConLai(int soLuongConLai) {
        this.soLuongConLai = soLuongConLai;
    }
}