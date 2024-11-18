package com.example.oop25;

import java.util.Random;

public class AccountService {
    private Account account;

    // Constructor
    public AccountService(Account account) {
        this.account = account;
    }

    // Đổi mật khẩu nếu nhớ mật khẩu cũ
    public void changePassword(String oldPassword, String newPassword) throws InvalidPasswordException {
        if (!account.checkPassword(oldPassword)) {
            System.out.println("Incorrect old password.");
            return;
        }
        account.setPassword(newPassword);
        System.out.println("Password changed successfully.");
    }

    // Quên mật khẩu - đặt lại bằng OTP
    public void resetPassword(String phoneNumber, String otp, String newPassword)
            throws InvalidOTPException, PhoneNumberNotFoundException, InvalidPasswordException {
        // Kiểm tra số điện thoại
        if (!account.getPhoneNumber().equals(phoneNumber)) {
            throw new PhoneNumberNotFoundException("Phone number not found in the system.");
        }

        // Kiểm tra OTP
        if (!validateOTP(otp)) {
            throw new InvalidOTPException("Invalid OTP provided.");
        }

        // Đặt mật khẩu mới
        account.setPassword(newPassword);
        System.out.println("Password reset successfully.");
    }

    // Sinh và gửi OTP
    public String generateOTP() {
        Random random = new Random();
        int otp = 100000 + random.nextInt(900000); // Sinh số ngẫu nhiên 6 chữ số
        System.out.println("OTP sent: " + otp);
        return String.valueOf(otp);
    }

    // Kiểm tra OTP
    private boolean validateOTP(String otp) {
        // Ví dụ: kiểm tra OTP phải là số 6 chữ số
        return otp.matches("\\d{6}");
    }
}

