package com.example.oop25;

public class InfoUser {
    private String name;         // Tên người dùng
    private String courseClass;  // Lớp khóa học
    private String department;   // Khoa
    private int enrollmentYear;  // Năm nhập học
    private String dob;          // Ngày tháng năm sinh

    // Constructor
    public InfoUser(String name, String courseClass, String department, int enrollmentYear, String dob) {
        this.name = name;
        this.courseClass = courseClass;
        this.department = department;
        this.enrollmentYear = enrollmentYear;
        this.dob = dob;
    }

    // Getter và Setter
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCourseClass() {
        return courseClass;
    }

    public void setCourseClass(String courseClass) {
        this.courseClass = courseClass;
    }

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    public int getEnrollmentYear() {
        return enrollmentYear;
    }

    public void setEnrollmentYear(int enrollmentYear) {
        this.enrollmentYear = enrollmentYear;
    }

    public String getDob() {
        return dob;
    }

    public void setDob(String dob) {
        this.dob = dob;
    }

    // Phương thức in thông tin người dùng
    public void printInfo() {
        System.out.println("Name: " + name);
        System.out.println("Class: " + courseClass);
        System.out.println("Department: " + department);
        System.out.println("Enrollment Year: " + enrollmentYear);
        System.out.println("Date of Birth: " + dob);
    }
}

