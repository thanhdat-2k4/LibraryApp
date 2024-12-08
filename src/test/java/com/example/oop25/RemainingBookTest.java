package com.example.oop25;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class RemainingBookTest {

    @Test
    void testConstructorAndTotalQuantity() {
        // Arrange
        RemainingBook book = new RemainingBook("B001", "Java Programming", "John Doe", "TechPress", 10, 5);

        // Act & Assert
        assertEquals(10, book.getAvailableCopies(), "Available copies should be initialized to 10");
        assertEquals(5, book.getBorrowedCopies(), "Borrowed copies should be initialized to 5");
        assertEquals(15, book.getTotalQuantity(), "Total quantity should be the sum of available and borrowed copies");
    }

    @Test
    void testSetAvailableCopies() {
        // Arrange
        RemainingBook book = new RemainingBook("B001", "Java Programming", "John Doe", "TechPress", 10, 5);

        // Act
        book.setAvailableCopies(15);

        // Assert
        assertEquals(15, book.getAvailableCopies(), "Available copies should be updated to 15");
        assertEquals(20, book.getTotalQuantity(), "Total quantity should be updated to reflect new available copies");
    }

    @Test
    void testSetBorrowedCopies() {
        // Arrange
        RemainingBook book = new RemainingBook("B001", "Java Programming", "John Doe", "TechPress", 10, 5);

        // Act
        book.setBorrowedCopies(8);

        // Assert
        assertEquals(8, book.getBorrowedCopies(), "Borrowed copies should be updated to 8");
        assertEquals(18, book.getTotalQuantity(), "Total quantity should be updated to reflect new borrowed copies");
    }

    @Test
    void testOverriddenMethods() {
        // Arrange
        RemainingBook book = new RemainingBook("B001", "Java Programming", "John Doe", "TechPress", 10, 5);

        // Act & Assert
        assertEquals(0, book.getAvailableQuantity(), "getAvailableQuantity should return 0 due to overridden behavior");
        assertEquals(0, book.getBorrowedQuantity(), "getBorrowedQuantity should return 0 due to overridden behavior");
    }

    @Test
    void testSetAvailableQuantityOverride() {
        // Arrange
        RemainingBook book = new RemainingBook("B001", "Java Programming", "John Doe", "TechPress", 10, 5);

        // Act
        book.setAvailableQuantity(20); // Overridden method does nothing

        // Assert
        assertEquals(10, book.getAvailableCopies(), "Available copies should remain unchanged due to overridden setAvailableQuantity");
        assertEquals(15, book.getTotalQuantity(), "Total quantity should remain unchanged due to overridden setAvailableQuantity");
    }
}
