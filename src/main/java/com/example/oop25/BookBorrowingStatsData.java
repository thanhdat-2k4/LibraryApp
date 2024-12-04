// sach muon ke thua tu sach
package com.example.oop25;

import javafx.beans.property.*;

public class BookBorrowingStatsData extends Sach {  // Kế thừa từ Sach
    private final IntegerProperty orderNumber;  // Số thứ tự (Order Number)
    private final IntegerProperty totalBooks;  // Tổng số sách (Mượn + Còn lại)

    public BookBorrowingStatsData(int orderNumber, String isbn, String bookTitle, int borrowedQuantity, int availableBooks) {
        super(isbn, bookTitle, "", "", availableBooks, borrowedQuantity);  // Gọi constructor của lớp Sach
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

    // Override các phương thức từ lớp Sach nếu cần
    @Override
    public int getSoLuongHienCon() {
        return super.getSoLuongHienCon();  // Lấy giá trị từ lớp Sach
    }

    @Override
    public int getSoLuongMuon() {
        return super.getSoLuongMuon();  // Lấy giá trị từ lớp Sach
    }

    // Cập nhật tổng số lượng sách khi thay đổi số sách mượn hoặc còn lại
    public void updateTotalBooks() {
        this.totalBooks.set(getSoLuongMuon() + getSoLuongHienCon());  // Tính lại tổng số sách
    }
}
