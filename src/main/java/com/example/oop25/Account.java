package com.example.oop25;

public class Account {
    private String password;       // Mật khẩu
    private String phoneNumber;    // Số điện thoại của admin

    // Constructor
    public Account(String password, String phoneNumber) throws InvalidPasswordException {
        setPassword(password); // Kiểm tra tính hợp lệ khi khởi tạo
        this.phoneNumber = phoneNumber;
    }

    // Kiểm tra mật khẩu hợp lệ
    private boolean isPasswordValid(String password) {
        return password != null && password.length() >= 6 && password.length() <= 12;
    }

    // Đặt mật khẩu
    public void setPassword(String password) throws InvalidPasswordException {
        if (!isPasswordValid(password)) {
            throw new InvalidPasswordException("Password must be 6-12 characters long.");
        }
        this.password = password;
    }

    // Kiểm tra mật khẩu khi đăng nhập
    public boolean checkPassword(String inputPassword) {
        return password.equals(inputPassword);
    }

    // Getter số điện thoại
    public String getPhoneNumber() {
        return phoneNumber;
    }
}

