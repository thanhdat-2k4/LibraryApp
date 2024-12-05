// sachMuon ke thua tu sach
package com.example.oop25;

import javafx.beans.property.*;

public class BookBorrowingStatsData extends Book {
    private final IntegerProperty orderNumber;  // Số thứ tự (Order Number)
    private final IntegerProperty totalBooks;  // Tổng số sách (Mượn + Còn lại)

    public BookBorrowingStatsData(int orderNumber, String isbn, String bookTitle, int borrowedQuantity, int availableBooks) {
        super(isbn, bookTitle, "", "", availableBooks, borrowedQuantity);  // Gọi constructor của lớp Book
        this.orderNumber = new SimpleIntegerProperty(orderNumber);
        this.totalBooks = new SimpleIntegerProperty(borrowedQuantity + availableBooks);  // Tính tổng số lượng sách
    }

    // Getter và Setter cho orderNumber
    public int getOrderNumber() {
        return orderNumber.get();
    }

    public IntegerProperty orderNumberProperty() {
        return orderNumber;
    }

    // Getter và Setter cho tổng số sách
    public int getTotalBooks() {
        return totalBooks.get();
    }

    public IntegerProperty totalBooksProperty() {
        return totalBooks;
    }

    // Sử dụng phương thức getter từ lớp Book để lấy giá trị
    @Override
    public int getAvailableQuantity() {
        return getAvailableCopies();  // Gọi phương thức getter của lớp Book
    }

    @Override
    public int getBorrowedQuantity() {
        return getBorrowedCopies();  // Gọi phương thức getter của lớp Book
    }

    // Cập nhật tổng số lượng sách khi thay đổi số sách mượn hoặc còn lại
    public void updateTotalBooks() {
        this.totalBooks.set(getBorrowedQuantity() + getAvailableQuantity());  // Tính lại tổng số sách
    }

    @Override
    public void setAvailableQuantity(int availableQuantity) {
        // Nếu cần thiết, bạn có thể thêm logic để thay đổi availableQuantity.
        // Nhưng nếu không, bạn có thể bỏ qua việc thay đổi ở đây vì đã có trong lớp cha.
    }
}