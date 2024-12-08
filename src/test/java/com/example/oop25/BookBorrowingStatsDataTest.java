package com.example.oop25;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class BookBorrowingStatsDataTest {

    @Test
    public void testConstructorAndGetters() {
        // Khởi tạo đối tượng
        BookBorrowingStatsData bookStats = new BookBorrowingStatsData(1, "123456", "Test Book", 5, 10);

        // Kiểm tra giá trị khởi tạo
        assertEquals(1, bookStats.getOrderNumber());
        assertEquals(15, bookStats.getTotalBooks());  // Tổng sách = borrowed + available
        assertEquals("123456", bookStats.getBookId());
        assertEquals("Test Book", bookStats.getBookTitle());
        assertEquals(10, bookStats.getAvailableQuantity());
        assertEquals(5, bookStats.getBorrowedQuantity());
    }

    @Test
    public void testSetBorrowedCopies() {
        // Khởi tạo đối tượng
        BookBorrowingStatsData bookStats = new BookBorrowingStatsData(1, "123456", "Test Book", 5, 10);

        // Thay đổi số sách mượn
        bookStats.borrowedCopiesProperty().set(7); // Đặt số sách mượn là 7

        // Kiểm tra giá trị sau khi thay đổi
        assertEquals(7, bookStats.getBorrowedQuantity());
        assertEquals(17, bookStats.getTotalBooks()); // Tổng sách cập nhật theo giá trị mới
    }

    @Test
    public void testSetAvailableCopies() {
        // Khởi tạo đối tượng
        BookBorrowingStatsData bookStats = new BookBorrowingStatsData(1, "123456", "Test Book", 5, 10);

        // Thay đổi số sách còn lại
        bookStats.availableCopiesProperty().set(8); // Đặt số sách còn lại là 8

        // Kiểm tra giá trị sau khi thay đổi
        assertEquals(8, bookStats.getAvailableQuantity());
        assertEquals(13, bookStats.getTotalBooks()); // Tổng sách cập nhật theo giá trị mới
    }

    @Test
    public void testUpdateProperties() {
        // Khởi tạo đối tượng
        BookBorrowingStatsData bookStats = new BookBorrowingStatsData(1, "123456", "Test Book", 5, 10);

        // Thay đổi cả hai giá trị
        bookStats.borrowedCopiesProperty().set(6);
        bookStats.availableCopiesProperty().set(9);

        // Kiểm tra giá trị cập nhật
        assertEquals(6, bookStats.getBorrowedQuantity());
        assertEquals(9, bookStats.getAvailableQuantity());
        assertEquals(15, bookStats.getTotalBooks());
    }

    @Test
    public void testOrderNumberProperty() {
        // Kiểm tra xem thứ tự có được lưu trữ đúng không
        BookBorrowingStatsData bookStats = new BookBorrowingStatsData(1, "123456", "Test Book", 5, 10);

        assertEquals(1, bookStats.orderNumberProperty().get());

        // Thay đổi thứ tự
        bookStats.orderNumberProperty().set(2);
        assertEquals(2, bookStats.getOrderNumber());
    }
}
