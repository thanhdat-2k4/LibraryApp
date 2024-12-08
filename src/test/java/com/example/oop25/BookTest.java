package com.example.oop25;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

public class BookTest {

    @Test
    public void testConcreteBook() {
        // Tạo đối tượng ConcreteBook
        ConcreteBook concreteBook = new ConcreteBook(
                "B001",                     // bookId
                "Java Programming",         // bookTitle
                "Author Name",              // authorName
                "Tech Publisher",           // publisher
                10,                         // availableCopies
                2                           // borrowedCopies
        );

        // Kiểm tra tên sách
        assertEquals("Java Programming", concreteBook.getBookTitle());

        // Kiểm tra số lượng sách có sẵn
        assertEquals(10, concreteBook.getAvailableQuantity());

        // Kiểm tra số lượng sách đã mượn
        assertEquals(2, concreteBook.getBorrowedQuantity());

        // Cập nhật số lượng sách có sẵn
        concreteBook.setAvailableQuantity(8);

        // Kiểm tra lại số lượng sách có sẵn sau khi cập nhật
        assertEquals(8, concreteBook.getAvailableQuantity());
    }
}
