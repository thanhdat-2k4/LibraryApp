package com.example.oop25;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class BorrowerTest {

    @Test
    public void testConstructorAndGetters() {
        // Khởi tạo đối tượng
        Borrower borrower = new Borrower("R001", "Nguyen Van A", "Java Programming", "Đang mượn");

        // Kiểm tra giá trị khởi tạo
        assertEquals("R001", borrower.getReaderID());
        assertEquals("Nguyen Van A", borrower.getReaderName());
        assertEquals("Java Programming", borrower.getBorrowedBookTitle());
        assertEquals("Đang mượn", borrower.getBorrowStatus());
    }

    @Test
    public void testSetReaderID() {
        // Khởi tạo đối tượng
        Borrower borrower = new Borrower("R001", "Nguyen Van A", "Java Programming", "Đang mượn");

        // Thay đổi giá trị readerID
        borrower.setReaderID("R002");

        // Kiểm tra giá trị mới
        assertEquals("R002", borrower.getReaderID());
    }

    @Test
    public void testSetReaderName() {
        // Khởi tạo đối tượng
        Borrower borrower = new Borrower("R001", "Nguyen Van A", "Java Programming", "Đang mượn");

        // Thay đổi giá trị readerName
        borrower.setReaderName("Tran Van B");

        // Kiểm tra giá trị mới
        assertEquals("Tran Van B", borrower.getReaderName());
    }

    @Test
    public void testSetBorrowedBookTitle() {
        // Khởi tạo đối tượng
        Borrower borrower = new Borrower("R001", "Nguyen Van A", "Java Programming", "Đang mượn");

        // Thay đổi giá trị borrowedBookTitle
        borrower.setBorrowedBookTitle("System Design");

        // Kiểm tra giá trị mới
        assertEquals("System Design", borrower.getBorrowedBookTitle());
    }

    @Test
    public void testSetBorrowStatus() {
        // Khởi tạo đối tượng
        Borrower borrower = new Borrower("R001", "Nguyen Van A", "Java Programming", "Đang mượn");

        // Thay đổi giá trị borrowStatus
        borrower.setBorrowStatus("Đã trả");

        // Kiểm tra giá trị mới
        assertEquals("Đã trả", borrower.getBorrowStatus());
    }

    @Test
    public void testProperties() {
        // Khởi tạo đối tượng
        Borrower borrower = new Borrower("R001", "Nguyen Van A", "Java Programming", "Đang mượn");

        // Kiểm tra StringProperty
        assertEquals("R001", borrower.readerIDProperty().get());
        assertEquals("Nguyen Van A", borrower.readerNameProperty().get());
        assertEquals("Java Programming", borrower.borrowedBookTitleProperty().get());
        assertEquals("Đang mượn", borrower.borrowStatusProperty().get());
    }
}
