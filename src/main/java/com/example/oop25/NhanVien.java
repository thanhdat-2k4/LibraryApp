//package com.example.oop25;
//public class NhanVien {
//    private String maNV;
//    private String hoTen;
//    private String ngaySinh;
//    private String diaChi;
//    private String sdt;
//    private String cccd;
//    // Constructor để khởi tạo các giá trị cho các trường dữ liệu
//    public NhanVien(String maNV, String hoTen, String ngaySinh, String diaChi, String sdt, String cccd) {
//        this.maNV = maNV;
//        this.hoTen = hoTen;
//        this.ngaySinh = ngaySinh;
//        this.diaChi = diaChi;
//        this.sdt = sdt;
//        this.cccd = cccd;
//    }
//    // Getter để lấy giá trị của các trường dữ liệu
//    public String getMaNV() {
//        return maNV;
//    }
//    public String getHoTen() {
//        return hoTen;
//    }
//    public String getNgaySinh() {
//        return ngaySinh;
//    }
//    public String getDiaChi() {
//        return diaChi;
//    }
//    public String getSdt() {
//        return sdt;
//    }
//    public String getCccd() {
//        return cccd;
//    }
//    // Setter để thay đổi giá trị của các trường dữ liệu
//    public void setMaNV(String maNV) {
//        this.maNV = maNV;
//    }
//    public void setHoTen(String hoTen) {
//        this.hoTen = hoTen;
//    }
//    public void setNgaySinh(String ngaySinh) {
//        this.ngaySinh = ngaySinh;
//    }
//    public void setDiaChi(String diaChi) {
//        this.diaChi = diaChi;
//    }
//    public void setSdt(String sdt) {
//        this.sdt = sdt;
//    }
//    public void setCccd(String cccd) {
//        this.cccd = cccd;
//    }
//}
package com.example.oop25;

import javafx.beans.InvalidationListener;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;

public class NhanVien extends Person {
    private StringProperty cccd;

    // Constructor
    public NhanVien(String maNV, String hoTen, String ngaySinh, String diaChi, String sdt, String cccd) {
        super(maNV, hoTen, ngaySinh, diaChi, sdt); // Gọi constructor của Person
        this.cccd = new StringProperty() {
            @Override
            public void addListener(InvalidationListener invalidationListener) {

            }

            @Override
            public void removeListener(InvalidationListener invalidationListener) {

            }

            @Override
            public void addListener(ChangeListener<? super String> changeListener) {

            }

            @Override
            public void removeListener(ChangeListener<? super String> changeListener) {

            }

            @Override
            public String get() {
                return "";
            }

            @Override
            public void set(String s) {

            }

            @Override
            public Object getBean() {
                return null;
            }

            @Override
            public String getName() {
                return "";
            }

            @Override
            public void bind(ObservableValue<? extends String> observableValue) {

            }

            @Override
            public void unbind() {

            }

            @Override
            public boolean isBound() {
                return false;
            }
        };
    }

    // Getter and Setter for cccd
    public String getCccd() {
        return cccd.get();
    }

    public void setCccd(String cccd) {
        this.cccd.set(cccd);
    }
}
//ke thua