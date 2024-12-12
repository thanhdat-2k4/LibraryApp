package com.example.oop25;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class BookBorrowingStatsDataTest {
    private BookBorrowingStatsData bookStats;

    @BeforeEach
    void setUp() {
        // Khởi tạo đối tượng BookBorrowingStatsData với các tham số
        bookStats = new BookBorrowingStatsData(1, "123-456-789", "Sample Book", 5, 10);
    }

    @Test
    void testConstructorAndGetters() {
        // Kiểm tra constructor và các phương thức getter
        assertEquals(1, bookStats.getOrderNumber());
        assertEquals("Sample Book", bookStats.getBookTitle());
        assertEquals("123-456-789", bookStats.getBookId());  // Sử dụng getBookId thay vì getIsbn
        assertEquals(5, bookStats.getBorrowedQuantity());
        assertEquals(10, bookStats.getAvailableQuantity());
        assertEquals(15, bookStats.getTotalBooks()); // 5 mượn + 10 còn lại = 15 tổng
    }

    @Test
    void testUpdateTotalBooks() {
        // Kiểm tra phương thức updateTotalBooks
        bookStats.setAvailableQuantity(8);
        bookStats.setBorrowedCopies(7);
        bookStats.updateTotalBooks();

        // Kiểm tra lại tổng số sách
        assertEquals(15, bookStats.getTotalBooks()); // 7 mượn + 8 còn lại = 15 tổng
    }

    @Test
    void testSetAvailableQuantity() {
        // Kiểm tra việc thay đổi số lượng sách có sẵn
        bookStats.setAvailableQuantity(20);
        assertEquals(20, bookStats.getAvailableQuantity());
    }

    @Test
    void testSetBorrowedQuantity() {
        // Kiểm tra việc thay đổi số lượng sách đã mượn
        bookStats.setBorrowedCopies(12);
        assertEquals(12, bookStats.getBorrowedQuantity());
    }
}
