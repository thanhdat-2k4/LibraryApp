package com.example.oop25;

// Ngoại lệ cho OTP không hợp lệ
class InvalidPasswordException extends Exception {
    public InvalidPasswordException(String message) {
        super(message);
    }
}
