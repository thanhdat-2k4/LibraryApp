package com.example.oop25;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class BorrowerTest {

    @Test
    public void testConstructorAndGetters() {
        // Khởi tạo đối tượng
        Borrower borrower = new Borrower("R001", "Nguyen Van A", "Java Programming", "Đang mượn");

        // Kiểm tra giá trị khởi tạo
        assertEquals("R001", borrower.getReaderId());
        assertEquals("Nguyen Van A", borrower.getReaderName());
        assertEquals("Java Programming", borrower.getBorrowedBookTitle());
        assertEquals("Đang mượn", borrower.getBorrowStatus());
    }

    @Test
    public void testSetReaderID() {
        Borrower borrower = new Borrower("R001", "Nguyen Van A", "Java Programming", "Đang mượn");

        borrower.setReaderId("R002"); // Thay đổi giá trị readerId
        assertEquals("R002", borrower.getReaderId());
    }

    @Test
    public void testSetReaderName() {
        Borrower borrower = new Borrower("R001", "Nguyen Van A", "Java Programming", "Đang mượn");

        borrower.setReaderName("Tran Van B"); // Thay đổi giá trị readerName
        assertEquals("Tran Van B", borrower.getReaderName());
    }

    @Test
    public void testSetBorrowedBookTitle() {
        Borrower borrower = new Borrower("R001", "Nguyen Van A", "Java Programming", "Đang mượn");

        borrower.setBorrowedBookTitle("System Design"); // Thay đổi giá trị borrowedBookTitle
        assertEquals("System Design", borrower.getBorrowedBookTitle());
    }

    @Test
    public void testSetBorrowStatus() {
        Borrower borrower = new Borrower("R001", "Nguyen Van A", "Java Programming", "Đang mượn");

        borrower.setBorrowStatus("Đã trả"); // Thay đổi giá trị borrowStatus
        assertEquals("Đã trả", borrower.getBorrowStatus());
    }

    @Test
    public void testProperties() {
        Borrower borrower = new Borrower("R001", "Nguyen Van A", "Java Programming", "Đang mượn");

        assertEquals("R001", borrower.readerIdProperty().get());
        assertEquals("Nguyen Van A", borrower.readerNameProperty().get());
        assertEquals("Java Programming", borrower.borrowedBookTitleProperty().get());
        assertEquals("Đang mượn", borrower.borrowStatusProperty().get());
    }
}
