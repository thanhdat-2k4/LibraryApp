
package com.example.oop25;

import javafx.beans.property.*;

public class Sach {
    private final StringProperty isbn;
    private final StringProperty tenSach;
    private final StringProperty tenTacGia;
    private final StringProperty nxb;
    private final IntegerProperty soLuongHienCon;
    private final IntegerProperty soLuongMuon;
    private BooleanProperty chon;  // Sử dụng BooleanProperty cho CheckBox
    // Constructor
    public Sach(String isbn, String tenSach, String tenTacGia, String nxb, int soLuongHienCon, int soLuongMuon) {
        this.isbn = new SimpleStringProperty(isbn); // ISBN là chuỗi
        this.tenSach = new SimpleStringProperty(tenSach);
        this.tenTacGia = new SimpleStringProperty(tenTacGia);
        this.nxb = new SimpleStringProperty(nxb);
        this.soLuongHienCon = new SimpleIntegerProperty(soLuongHienCon);
        this.soLuongMuon = new SimpleIntegerProperty(soLuongMuon);
        this.chon = new SimpleBooleanProperty(false);  // Mặc định là false
    }

    // Getters
    public String getIsbn() {
        return isbn.get();
    }

    public String getTenSach() {
        return tenSach.get();
    }

    public String getTenTacGia() {
        return tenTacGia.get();
    }

    public String getNxb() {
        return nxb.get();
    }

    public int getSoLuongHienCon() {
        return soLuongHienCon.get();
    }

    public int getSoLuongMuon() {
        return soLuongMuon.get();
    }

    // Properties
    public StringProperty isbnProperty() {
        return isbn;
    }

    public StringProperty tenSachProperty() {
        return tenSach;
    }

    public StringProperty tenTacGiaProperty() {
        return tenTacGia;
    }

    public StringProperty nxbProperty() {
        return nxb;
    }

    public IntegerProperty soLuongHienConProperty() {
        return soLuongHienCon;
    }

    public IntegerProperty soLuongMuonProperty() {
        return soLuongMuon;
    }

    // Setters (Optional, nếu bạn cần thay đổi giá trị của các thuộc tính này)
    public void setIsbn(String isbn) {
        this.isbn.set(isbn);
    }

    public void setTenSach(String tenSach) {
        this.tenSach.set(tenSach);
    }

    public void setTenTacGia(String tenTacGia) {
        this.tenTacGia.set(tenTacGia);
    }

    public void setNxb(String nxb) {
        this.nxb.set(nxb);
    }

    public void setSoLuongHienCon(int soLuongHienCon) {
        this.soLuongHienCon.set(soLuongHienCon);
    }

    public void setSoLuongMuon(int soLuongMuon) {
        this.soLuongMuon.set(soLuongMuon);
    }

    // BooleanProperty getter và setter
    public BooleanProperty chonProperty() {
        return chon;
    }

    public boolean isChon() {
        return chon.get();
    }

    public void setChon(boolean chon) {
        this.chon.set(chon);
    }
}

//package com.example.oop25;
//
//import javafx.beans.property.*;
//
//public class Book {
//    private final StringProperty bookId;
//    private final StringProperty bookTitle;
//    private final StringProperty authorName;
//    private final StringProperty publisher;
//    private final IntegerProperty availableCopies;
//    private final IntegerProperty borrowedCopies;
//    private BooleanProperty chon;  // BooleanProperty for CheckBox
//
//    // Constructor
//    public Book(String bookId, String bookTitle, String authorName, String publisher, int availableCopies, int borrowedCopies) {
//        this.bookId = new SimpleStringProperty(bookId); // ISBN is a string
//        this.bookTitle = new SimpleStringProperty(bookTitle);
//        this.authorName = new SimpleStringProperty(authorName);
//        this.publisher = new SimpleStringProperty(publisher);
//        this.availableCopies = new SimpleIntegerProperty(availableCopies);
//        this.borrowedCopies = new SimpleIntegerProperty(borrowedCopies);
//        this.chon = new SimpleBooleanProperty(false);  // Default is false
//    }
//
//    // Getters
//    public String getBookId() {
//        return bookId.get();
//    }
//
//    public String getBookTitle() {
//        return bookTitle.get();
//    }
//
//    public String getAuthorName() {
//        return authorName.get();
//    }
//
//    public String getPublisher() {
//        return publisher.get();
//    }
//
//    public int getAvailableCopies() {
//        return availableCopies.get();
//    }
//
//    public int getBorrowedCopies() {
//        return borrowedCopies.get();
//    }
//
//    // Properties
//    public StringProperty bookIdProperty() {
//        return bookId;
//    }
//
//    public StringProperty bookTitleProperty() {
//        return bookTitle;
//    }
//
//    public StringProperty authorNameProperty() {
//        return authorName;
//    }
//
//    public StringProperty publisherProperty() {
//        return publisher;
//    }
//
//    public IntegerProperty availableCopiesProperty() {
//        return availableCopies;
//    }
//
//    public IntegerProperty borrowedCopiesProperty() {
//        return borrowedCopies;
//    }
//
//    // Setters (Optional, if you need to change these properties)
//    public void setBookId(String bookId) {
//        this.bookId.set(bookId);
//    }
//
//    public void setBookTitle(String bookTitle) {
//        this.bookTitle.set(bookTitle);
//    }
//
//    public void setAuthorName(String authorName) {
//        this.authorName.set(authorName);
//    }
//
//    public void setPublisher(String publisher) {
//        this.publisher.set(publisher);
//    }
//
//    public void setAvailableCopies(int availableCopies) {
//        this.availableCopies.set(availableCopies);
//    }
//
//    public void setBorrowedCopies(int borrowedCopies) {
//        this.borrowedCopies.set(borrowedCopies);
//    }
//
//    // BooleanProperty getter and setter
//    public BooleanProperty chonProperty() {
//        return chon;
//    }
//
//    public boolean isChon() {
//        return chon.get();
//    }
//
//    public void setChon(boolean chon) {
//        this.chon.set(chon);
//    }
//}
