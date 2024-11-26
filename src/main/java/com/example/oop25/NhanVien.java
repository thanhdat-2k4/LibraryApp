package com.example.oop25;

public class NhanVien {
    private String maNV;
    private String hoTen;
    private String ngaySinh;
    private String diaChi;
    private String sdt;
    private String cccd;

    // Constructor để khởi tạo các giá trị cho các trường dữ liệu
    public NhanVien(String maNV, String hoTen, String ngaySinh, String diaChi, String sdt, String cccd) {
        this.maNV = maNV;
        this.hoTen = hoTen;
        this.ngaySinh = ngaySinh;
        this.diaChi = diaChi;
        this.sdt = sdt;
        this.cccd = cccd;
    }

    // Getter để lấy giá trị của các trường dữ liệu
    public String getMaNV() {
        return maNV;
    }

    public String getHoTen() {
        return hoTen;
    }

    public String getNgaySinh() {
        return ngaySinh;
    }

    public String getDiaChi() {
        return diaChi;
    }

    public String getSdt() {
        return sdt;
    }

    public String getCccd() {
        return cccd;
    }

    // Setter để thay đổi giá trị của các trường dữ liệu
    public void setMaNV(String maNV) {
        this.maNV = maNV;
    }

    public void setHoTen(String hoTen) {
        this.hoTen = hoTen;
    }

    public void setNgaySinh(String ngaySinh) {
        this.ngaySinh = ngaySinh;
    }

    public void setDiaChi(String diaChi) {
        this.diaChi = diaChi;
    }

    public void setSdt(String sdt) {
        this.sdt = sdt;
    }

    public void setCccd(String cccd) {
        this.cccd = cccd;
    }
}