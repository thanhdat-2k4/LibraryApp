package com.example.oop25;

import java.util.ArrayList;
import java.util.List;

public class Library {
    private List<Book> books;

    // Constructor
    public Library() {
        books = new ArrayList<>();
    }

    // Thêm sách vào thư viện
    public void addBook(Book book) {
        books.add(book);
        System.out.println("Book added: " + book.getName());
    }

    // Hiển thị danh sách sách trong thư viện
    public void displayBooks() {
        System.out.println("Books in Library:");
        if (books.isEmpty()) {
            System.out.println("No books in the library.");
        } else {
            for (Book book : books) {
                book.printBookInfo();
                System.out.println("-------------------");
            }
        }
    }

    // Tìm sách theo ID
    public Book findBookById(String id) {
        for (Book book : books) {
            if (book.getId().equalsIgnoreCase(id)) {
                return book;
            }
        }
        System.out.println("Book with ID " + id + " not found.");
        return null;
    }

    // Mượn sách
    public void borrowBook(String id) {
        Book book = findBookById(id);
        if (book != null) {
            if (book.getNum() > 0) {
                book.setNum(book.getNum() - 1);
                System.out.println("Book borrowed successfully: " + book.getName());
            } else {
                System.out.println("Book is out of stock: " + book.getName());
            }
        }
    }

    // Trả sách
    public void returnBook(String id) {
        Book book = findBookById(id);
        if (book != null) {
            book.setNum(book.getNum() + 1);
            System.out.println("Book returned successfully: " + book.getName());
        }
    }
}

