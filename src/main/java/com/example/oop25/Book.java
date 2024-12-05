
package com.example.oop25;

import javafx.beans.property.*;

public abstract class Book {
    private final StringProperty bookId;
    private final StringProperty bookTitle;
    private final StringProperty authorName;
    private final StringProperty publisher;
    private final IntegerProperty availableCopies;
    private final IntegerProperty borrowedCopies;
    private BooleanProperty chon;  // BooleanProperty for CheckBox

    // Constructor
    public Book(String bookId, String bookTitle, String authorName, String publisher, int availableCopies, int borrowedCopies) {
        this.bookId = new SimpleStringProperty(bookId); // ISBN is a string
        this.bookTitle = new SimpleStringProperty(bookTitle);
        this.authorName = new SimpleStringProperty(authorName);
        this.publisher = new SimpleStringProperty(publisher);
        this.availableCopies = new SimpleIntegerProperty(availableCopies);
        this.borrowedCopies = new SimpleIntegerProperty(borrowedCopies);
        this.chon = new SimpleBooleanProperty(false);  // Default is false
    }

    // Getters
    public String getBookId() {
        return bookId.get();
    }

    public String getBookTitle() {
        return bookTitle.get();
    }

    public String getAuthorName() {
        return authorName.get();
    }

    public String getPublisher() {
        return publisher.get();
    }

    public int getAvailableCopies() {
        return availableCopies.get();
    }

    public int getBorrowedCopies() {
        return borrowedCopies.get();
    }

    // Properties
    public StringProperty bookIdProperty() {
        return bookId;
    }

    public StringProperty bookTitleProperty() {
        return bookTitle;
    }

    public StringProperty authorNameProperty() {
        return authorName;
    }

    public StringProperty publisherProperty() {
        return publisher;
    }

    public IntegerProperty availableCopiesProperty() {
        return availableCopies;
    }

    public IntegerProperty borrowedCopiesProperty() {
        return borrowedCopies;
    }

    // Setters (Optional, if you need to change these properties)
    public void setBookId(String bookId) {
        this.bookId.set(bookId);
    }

    public void setBookTitle(String bookTitle) {
        this.bookTitle.set(bookTitle);
    }

    public void setAuthorName(String authorName) {
        this.authorName.set(authorName);
    }

    public void setPublisher(String publisher) {
        this.publisher.set(publisher);
    }

    public void setAvailableCopies(int availableCopies) {
        this.availableCopies.set(availableCopies);
    }

    public void setBorrowedCopies(int borrowedCopies) {
        this.borrowedCopies.set(borrowedCopies);
    }

    // BooleanProperty getter and setter
    public BooleanProperty chonProperty() {
        return chon;
    }

    public boolean isChon() {
        return chon.get();
    }

    public void setChon(boolean chon) {
        this.chon.set(chon);
    }

    // Override các phương thức từ lớp Book nếu cần
    public abstract int getAvailableQuantity();

    public abstract int getBorrowedQuantity();

    // Override setters in Book to update totalQuantity automatically
    public abstract void setAvailableQuantity(int availableQuantity);
}
