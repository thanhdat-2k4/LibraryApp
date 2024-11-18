package com.example.oop25;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class Librarian {
    private List<User> users; // Danh sách người dùng

    // Constructor
    public Librarian() {
        users = new ArrayList<>();
    }

    // Thêm người dùng mới
    public void addUser(User user) {
        users.add(user);
        System.out.println("User added: " + user.getInfo().getName());
    }

    // Xóa người dùng theo mã thẻ
    public void removeUser(String cardId) {
        User userToRemove = findUserByCardId(cardId);
        if (userToRemove != null) {
            users.remove(userToRemove);
            System.out.println("User removed: " + userToRemove.getInfo().getName());
        } else {
            System.out.println("User with card ID " + cardId + " not found.");
        }
    }

    // Sửa thông tin người dùng
    public void updateUser(String cardId, InfoUser newInfo, LocalDate newRenewalDate, LocalDate newExpiryDate) {
        User userToUpdate = findUserByCardId(cardId);
        if (userToUpdate != null) {
            userToUpdate.updateUserInfo(newInfo, newRenewalDate, newExpiryDate);
        } else {
            System.out.println("User with card ID " + cardId + " not found.");
        }
    }

    // Tìm người dùng theo mã thẻ
    public User findUserByCardId(String cardId) {
        for (User user : users) {
            if (user.getCardId().equalsIgnoreCase(cardId)) {
                return user;
            }
        }
        return null;
    }

    // Hiển thị danh sách tất cả người dùng
    public void displayAllUsers() {
        if (users.isEmpty()) {
            System.out.println("No users in the system.");
        } else {
            System.out.println("List of users:");
            for (User user : users) {
                user.printUserInfo();
                System.out.println("-------------------");
            }
        }
    }

    public static void main(String[] args) {
        // Tạo lớp quản lý
        Librarian librarian = new Librarian();

        // Tạo thông tin người dùng
        InfoUser info1 = new InfoUser("Nguyen Van A", "KTPM01", "CNTT", 2020, "01/01/2002");
        InfoUser info2 = new InfoUser("Tran Van B", "KTPM02", "KHMT", 2021, "15/03/2001");

        // Tạo người dùng
        User user1 = new User("C001", info1, LocalDate.of(2024, 1, 1), LocalDate.of(2025, 1, 1));
        User user2 = new User("C002", info2, LocalDate.of(2024, 2, 1), LocalDate.of(2025, 2, 1));

        // Thêm người dùng
        librarian.addUser(user1);
        librarian.addUser(user2);

        // Hiển thị danh sách người dùng
        librarian.displayAllUsers();

        // Sửa thông tin người dùng
        InfoUser updatedInfo = new InfoUser("Nguyen Van A Updated", "KTPM03", "KHMT", 2022, "01/01/2002");
        librarian.updateUser("C001", updatedInfo, LocalDate.of(2025, 1, 1), LocalDate.of(2026, 1, 1));

        // Hiển thị danh sách sau khi sửa
        librarian.displayAllUsers();

        // Xóa người dùng
        librarian.removeUser("C002");

        // Hiển thị danh sách sau khi xóa
        librarian.displayAllUsers();

        // Thử xóa người dùng không tồn tại
        librarian.removeUser("C999");
    }
}

