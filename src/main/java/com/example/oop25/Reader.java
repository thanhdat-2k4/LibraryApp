
package com.example.oop25;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class Reader {
    private StringProperty readerId;       // ID of the reader
    private StringProperty readerName;    // Name of the reader
    private StringProperty information;   // Additional information
    private StringProperty renewalDate;   // Date of renewal
    private StringProperty expirationDate; // Expiration date
    private StringProperty notes;         // Notes

    // Constructor
    public Reader(String readerId, String readerName, String information, String renewalDate, String expirationDate, String notes) {
        this.readerId = new SimpleStringProperty(readerId);
        this.readerName = new SimpleStringProperty(readerName);
        this.information = new SimpleStringProperty(information);
        this.renewalDate = new SimpleStringProperty(renewalDate);
        this.expirationDate = new SimpleStringProperty(expirationDate);
        this.notes = new SimpleStringProperty(notes);
    }

    // Getters and setters
    public String getReaderId() {
        return readerId.get();
    }

    public void setReaderId(String readerId) {
        this.readerId.set(readerId);
    }

    public StringProperty readerIdProperty() {
        return readerId;
    }

    public String getReaderName() {
        return readerName.get();
    }

    public void setReaderName(String readerName) {
        this.readerName.set(readerName);
    }

    public StringProperty readerNameProperty() {
        return readerName;
    }

    public String getInformation() {
        return information.get();
    }

    public void setInformation(String information) {
        this.information.set(information);
    }

    public StringProperty informationProperty() {
        return information;
    }

    public String getRenewalDate() {
        return renewalDate.get();
    }

    public void setRenewalDate(String renewalDate) {
        this.renewalDate.set(renewalDate);
    }

    public StringProperty renewalDateProperty() {
        return renewalDate;
    }

    public String getExpirationDate() {
        return expirationDate.get();
    }

    public void setExpirationDate(String expirationDate) {
        this.expirationDate.set(expirationDate);
    }

    public StringProperty expirationDateProperty() {
        return expirationDate;
    }

    public String getNotes() {
        return notes.get();
    }

    public void setNotes(String notes) {
        this.notes.set(notes);
    }

    public StringProperty notesProperty() {
        return notes;
    }
}
