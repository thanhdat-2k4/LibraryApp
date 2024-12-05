////SachConLai ke thua tu sach
//
//package com.example.oop25;
//
//import javafx.beans.property.IntegerProperty;
//import javafx.beans.property.SimpleIntegerProperty;
//
//public class RemainingBook extends Sach {
//
//    private final IntegerProperty totalQuantity; // Tổng số lượng (read-only)
//
//    // Constructor
//    public RemainingBook(String isbn, String title, String author, String publisher, int availableQuantity, int borrowedQuantity) {
//        super(isbn, title, author, publisher, availableQuantity, borrowedQuantity);
//        this.totalQuantity = new SimpleIntegerProperty(availableQuantity + borrowedQuantity); // Sử dụng SimpleIntegerProperty
//    }
//
//    // Getter for totalQuantity
//    public int getTotalQuantity() {
//        return totalQuantity.get();
//    }
//
//    // Property for totalQuantity (sử dụng cho TableView nếu cần)
//    public IntegerProperty totalQuantityProperty() {
//        return totalQuantity;
//    }
//
//    // Override setters in Sach to update totalQuantity automatically
//    @Override
//    public void setSoLuongHienCon(int availableQuantity) {
//        super.setSoLuongHienCon(availableQuantity);
//        updateTotalQuantity();
//    }
//
//    @Override
//    public void setSoLuongMuon(int borrowedQuantity) {
//        super.setSoLuongMuon(borrowedQuantity);
//        updateTotalQuantity();
//    }
//
//    // Private method to update totalQuantity
//    private void updateTotalQuantity() {
//        this.totalQuantity.set(getSoLuongHienCon() + getSoLuongMuon());
//    }
//}
package com.example.oop25;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;

public class RemainingBook extends Book {

    private final IntegerProperty totalQuantity; // Tổng số lượng (read-only)

    // Constructor
    public RemainingBook(String bookId, String bookTitle, String authorName, String publisher, int availableCopies, int borrowedCopies) {
        super(bookId, bookTitle, authorName, publisher, availableCopies, borrowedCopies);
        this.totalQuantity = new SimpleIntegerProperty(availableCopies + borrowedCopies); // Sử dụng SimpleIntegerProperty
    }

    // Getter for totalQuantity
    public int getTotalQuantity() {
        return totalQuantity.get();
    }

    // Property for totalQuantity (sử dụng cho TableView nếu cần)
    public IntegerProperty totalQuantityProperty() {
        return totalQuantity;
    }

    // Override setters in Book to update totalQuantity automatically
    @Override
    public void setAvailableCopies(int availableCopies) {
        super.setAvailableCopies(availableCopies);
        updateTotalQuantity();
    }

    @Override
    public void setBorrowedCopies(int borrowedCopies) {
        super.setBorrowedCopies(borrowedCopies);
        updateTotalQuantity();
    }

    @Override
    public int getAvailableQuantity() {
        return 0;
    }

    @Override
    public int getBorrowedQuantity() {
        return 0;
    }

    @Override
    public void setAvailableQuantity(int availableQuantity) {

    }

    // Private method to update totalQuantity
    private void updateTotalQuantity() {
        this.totalQuantity.set(getAvailableCopies() + getBorrowedCopies());
    }
}
