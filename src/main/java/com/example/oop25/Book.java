package com.example.oop25;

public class Book {
    // Các thuộc tính của lớp Book
    private String id;
    private String nxb;  // Nhà xuất bản
    private String name;  // Tên sách
    private int year;     // Năm xuất bản
    private String author; // Tác giả
    private int num;      // Số lượng

    // Constructor mặc định
    public Book() {}

    // Constructor có tham số
    public Book(String id, String nxb, String name, int year, String author, int num) {
        this.id = id;
        this.nxb = nxb;
        this.name = name;
        this.year = year;
        this.author = author;
        this.num = num;
    }

    // Getter và Setter cho các thuộc tính
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNxb() {
        return nxb;
    }

    public void setNxb(String nxb) {
        this.nxb = nxb;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public int getNum() {
        return num;
    }

    public void setNum(int num) {
        this.num = num;
    }

    // Phương thức cập nhật thông tin sách
    public void updateBookInfo(String newId, String newNxb, String newName, int newYear, String newAuthor, int newNum) {
        this.id = newId;
        this.nxb = newNxb;
        this.name = newName;
        this.year = newYear;
        this.author = newAuthor;
        this.num = newNum;
    }

    // Phương thức in thông tin sách
    public void printBookInfo() {
        System.out.println("ID: " + id);
        System.out.println("NXB: " + nxb);
        System.out.println("Tên sách: " + name);
        System.out.println("Năm xuất bản: " + year);
        System.out.println("Tác giả: " + author);
        System.out.println("Số lượng: " + num);
    }
}
