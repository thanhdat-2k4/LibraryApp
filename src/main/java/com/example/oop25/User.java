package com.example.oop25;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class User {
    private String cardId;                // Mã thẻ
    private InfoUser info;                // Thông tin người dùng (từ lớp InfoUser)
    private LocalDate renewalDate;        // Ngày gia hạn thẻ
    private LocalDate expiryDate;         // Ngày hết hạn thẻ
    private List<Book> borrowedBooks;     // Danh sách sách mượn

    // Constructor
    public User(String cardId, InfoUser info, LocalDate renewalDate, LocalDate expiryDate) {
        this.cardId = cardId;
        this.info = info;
        this.renewalDate = renewalDate;
        this.expiryDate = expiryDate;
        this.borrowedBooks = new ArrayList<>();
    }

    // Getter và Setter
    public String getCardId() {
        return cardId;
    }

    public void setCardId(String cardId) {
        this.cardId = cardId;
    }

    public InfoUser getInfo() {
        return info;
    }

    public void setInfo(InfoUser info) {
        this.info = info;
    }

    public LocalDate getRenewalDate() {
        return renewalDate;
    }

    public void setRenewalDate(LocalDate renewalDate) {
        this.renewalDate = renewalDate;
    }

    public LocalDate getExpiryDate() {
        return expiryDate;
    }

    public void setExpiryDate(LocalDate expiryDate) {
        this.expiryDate = expiryDate;
    }

    public List<Book> getBorrowedBooks() {
        return borrowedBooks;
    }

    // Thêm sách vào danh sách mượn
    public void borrowBook(Book book) {
        borrowedBooks.add(book);
        System.out.println("Book borrowed: " + book.getName());
    }

    // Xóa sách khỏi danh sách mượn
    public void returnBook(Book book) {
        if (borrowedBooks.remove(book)) {
            System.out.println("Book returned: " + book.getName());
        } else {
            System.out.println("Book not found in borrowed list: " + book.getName());
        }
    }

    // Sửa thông tin người dùng
    public void updateUserInfo(InfoUser newInfo, LocalDate newRenewalDate, LocalDate newExpiryDate) {
        this.info = newInfo;
        this.renewalDate = newRenewalDate;
        this.expiryDate = newExpiryDate;
        System.out.println("User information updated!");
    }

    // Phương thức in thông tin người dùng
    public void printUserInfo() {
        System.out.println("Card ID: " + cardId);
        System.out.println("Renewal Date: " + renewalDate);
        System.out.println("Expiry Date: " + expiryDate);
        System.out.println("User Information:");
        info.printInfo();
        System.out.println("Borrowed Books:");
        if (borrowedBooks.isEmpty()) {
            System.out.println("No borrowed books.");
        } else {
            for (Book book : borrowedBooks) {
                System.out.println("- " + book.getName());
            }
        }
    }
}
