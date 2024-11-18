package com.example.oop25;

// Ngoại lệ cho số điện thoại không tồn tại
class PhoneNumberNotFoundException extends Exception {
    public PhoneNumberNotFoundException(String message) {
        super(message);
    }
}
