//// nguoi muon

package com.example.oop25;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.beans.value.ObservableValue;

public class Borrower extends Reader {
    private final StringProperty borrowedBookTitle; // Tiêu đề sách mượn
    private final StringProperty borrowStatus;     // Trạng thái mượn sách

    // Constructor
    public Borrower(String readerId, String readerName, String borrowedBookTitle, String borrowStatus) {
        super(readerId, readerName, null, null, null, null); // Gọi constructor của lớp Reader với các giá trị không sử dụng
        this.borrowedBookTitle = new SimpleStringProperty(borrowedBookTitle);
        this.borrowStatus = new SimpleStringProperty(borrowStatus);
    }

    // Getter và Setter cho borrowedBookTitle
    public String getBorrowedBookTitle() {
        return borrowedBookTitle.get();
    }

    public void setBorrowedBookTitle(String borrowedBookTitle) {
        this.borrowedBookTitle.set(borrowedBookTitle);
    }

    public StringProperty borrowedBookTitleProperty() {
        return borrowedBookTitle;
    }

    // Getter và Setter cho borrowStatus
    public String getBorrowStatus() {
        return borrowStatus.get();
    }

    public void setBorrowStatus(String borrowStatus) {
        this.borrowStatus.set(borrowStatus);
    }

    public StringProperty borrowStatusProperty() {
        return borrowStatus;
    }

}
