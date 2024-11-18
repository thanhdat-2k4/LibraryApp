package com.example.oop25;

// Ngoại lệ cho mật khẩu không hợp lệ
class InvalidOTPException extends Exception {
    public InvalidOTPException(String message) {
        super(message);
    }
}
