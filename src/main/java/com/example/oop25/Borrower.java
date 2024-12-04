// nguoi muon
package com.example.oop25;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class Borrower {

    private final StringProperty readerID;
    private final StringProperty readerName;
    private final StringProperty borrowedBookTitle;
    private final StringProperty borrowStatus;

    public Borrower(String readerID, String readerName, String borrowedBookTitle, String borrowStatus) {
        this.readerID = new SimpleStringProperty(readerID);
        this.readerName = new SimpleStringProperty(readerName);
        this.borrowedBookTitle = new SimpleStringProperty(borrowedBookTitle);
        this.borrowStatus = new SimpleStringProperty(borrowStatus);
    }

    // Getter and Setter for readerID
    public String getReaderID() {
        return readerID.get();
    }

    public void setReaderID(String readerID) {
        this.readerID.set(readerID);
    }

    public StringProperty readerIDProperty() {
        return readerID;
    }

    // Getter and Setter for readerName
    public String getReaderName() {
        return readerName.get();
    }

    public void setReaderName(String readerName) {
        this.readerName.set(readerName);
    }

    public StringProperty readerNameProperty() {
        return readerName;
    }

    // Getter and Setter for borrowedBookTitle
    public String getBorrowedBookTitle() {
        return borrowedBookTitle.get();
    }

    public void setBorrowedBookTitle(String borrowedBookTitle) {
        this.borrowedBookTitle.set(borrowedBookTitle);
    }

    public StringProperty borrowedBookTitleProperty() {
        return borrowedBookTitle;
    }

    // Getter and Setter for borrowStatus
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
