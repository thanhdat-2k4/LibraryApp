package com.example.oop25;

public class PasswordStorage {
    private static String currentPassword = "12345678";

    public static String getCurrentPassword() {
        return currentPassword;
    }

    public static void setCurrentPassword(String newPassword) {
        currentPassword = newPassword;
    }
}
// hoàng lam
