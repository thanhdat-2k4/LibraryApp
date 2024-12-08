//// sachMuon ke thua tu sach
//package com.example.oop25;
//
//import javafx.beans.property.*;
//
//public class BookBorrowingStatsData extends Book {
//    private final IntegerProperty orderNumber;  // Số thứ tự (Order Number)
//    private final IntegerProperty totalBooks;  // Tổng số sách (Mượn + Còn lại)
//
//    public BookBorrowingStatsData(int orderNumber, String isbn, String bookTitle, int borrowedQuantity, int availableBooks) {
//        super(isbn, bookTitle, "", "", availableBooks, borrowedQuantity);  // Gọi constructor của lớp Book
//        this.orderNumber = new SimpleIntegerProperty(orderNumber);
//        this.totalBooks = new SimpleIntegerProperty(borrowedQuantity + availableBooks);  // Tính tổng số lượng sách
//    }
//
//    // Getter và Setter cho orderNumber
//    public int getOrderNumber() {
//        return orderNumber.get();
//    }
//
//    public IntegerProperty orderNumberProperty() {
//        return orderNumber;
//    }
//
//    // Getter và Setter cho tổng số sách
//    public int getTotalBooks() {
//        return totalBooks.get();
//    }
//
//    public IntegerProperty totalBooksProperty() {
//        return totalBooks;
//    }
//
//    // Sử dụng phương thức getter từ lớp Book để lấy giá trị
//    @Override
//    public int getAvailableQuantity() {
//        return getAvailableCopies();  // Gọi phương thức getter của lớp Book
//    }
//
//    @Override
//    public int getBorrowedQuantity() {
//        return getBorrowedCopies();  // Gọi phương thức getter của lớp Book
//    }
//
//    // Cập nhật tổng số lượng sách khi thay đổi số sách mượn hoặc còn lại
//    public void updateTotalBooks() {
//        this.totalBooks.set(getBorrowedQuantity() + getAvailableQuantity());  // Tính lại tổng số sách
//    }
//
//    @Override
//    public void setAvailableQuantity(int availableQuantity) {
//        // Nếu cần thiết, bạn có thể thêm logic để thay đổi availableQuantity.
//        // Nhưng nếu không, bạn có thể bỏ qua việc thay đổi ở đây vì đã có trong lớp cha.
//    }
//
//}
package com.example.oop25;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.StringProperty;
import javafx.beans.property.SimpleStringProperty;

public class BookBorrowingStatsData {
    private final IntegerProperty orderNumber;
    private final StringProperty bookId;
    private final StringProperty bookTitle;
    private final IntegerProperty borrowedQuantity;
    private final IntegerProperty availableQuantity;

    // Constructor
    public BookBorrowingStatsData(int orderNumber, String bookId, String bookTitle, int borrowedQuantity, int availableQuantity) {
        this.orderNumber = new SimpleIntegerProperty(orderNumber);
        this.bookId = new SimpleStringProperty(bookId);
        this.bookTitle = new SimpleStringProperty(bookTitle);
        this.borrowedQuantity = new SimpleIntegerProperty(borrowedQuantity);
        this.availableQuantity = new SimpleIntegerProperty(availableQuantity);
    }

    // Getters
    public int getOrderNumber() {
        return orderNumber.get();
    }

    public String getBookId() {
        return bookId.get();
    }

    public String getBookTitle() {
        return bookTitle.get();
    }

    public int getBorrowedQuantity() {
        return borrowedQuantity.get();
    }

    public int getAvailableQuantity() {
        return availableQuantity.get();
    }

    public int getTotalBooks() {
        return getAvailableQuantity() + getBorrowedQuantity();
    }

    // Properties for JavaFX
    public IntegerProperty orderNumberProperty() {
        return orderNumber;
    }

    public StringProperty bookIdProperty() {
        return bookId;
    }

    public StringProperty bookTitleProperty() {
        return bookTitle;
    }

    public IntegerProperty borrowedCopiesProperty() {
        return borrowedQuantity;
    }

    public IntegerProperty availableCopiesProperty() {
        return availableQuantity;
    }
}
