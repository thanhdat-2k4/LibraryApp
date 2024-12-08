

package com.example.oop25;

public class ConcreteBook extends Book {
    public ConcreteBook(String bookId, String bookTitle, String authorName, String publisher, int availableCopies, int borrowedCopies) {
        super(bookId, bookTitle, authorName, publisher, availableCopies, borrowedCopies);
    }

    @Override
    public int getAvailableQuantity() {
        return this.getAvailableCopies();
    }

    @Override
    public int getBorrowedQuantity() {
        return this.getBorrowedCopies();
    }

    @Override
    public void setAvailableQuantity(int availableQuantity) {
        this.setAvailableCopies(availableQuantity);
    }
}
