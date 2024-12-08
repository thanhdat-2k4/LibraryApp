package com.example.oop25;

import com.example.oop25.Reader;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class ReaderTest {

    @Test
    public void testConstructorAndGetters() {
        // Tạo đối tượng Reader
        Reader reader = new Reader("R001", "John Doe", "Student", "2024-12-01", "2025-12-01", "No notes");

        // Kiểm tra các giá trị khởi tạo
        assertEquals("R001", reader.getReaderId());
        assertEquals("John Doe", reader.getReaderName());
        assertEquals("Student", reader.getInformation());
        assertEquals("2024-12-01", reader.getRenewalDate());
        assertEquals("2025-12-01", reader.getExpirationDate());
        assertEquals("No notes", reader.getNotes());
    }

    @Test
    public void testSettersAndGetters() {
        // Tạo đối tượng Reader
        Reader reader = new Reader("R001", "John Doe", "Student", "2024-12-01", "2025-12-01", "No notes");

        // Thay đổi các giá trị sử dụng setter
        reader.setReaderId("R002");
        reader.setReaderName("Jane Smith");
        reader.setInformation("Teacher");
        reader.setRenewalDate("2025-12-01");
        reader.setExpirationDate("2026-12-01");
        reader.setNotes("Some notes");

        // Kiểm tra lại các giá trị sau khi thay đổi
        assertEquals("R002", reader.getReaderId());
        assertEquals("Jane Smith", reader.getReaderName());
        assertEquals("Teacher", reader.getInformation());
        assertEquals("2025-12-01", reader.getRenewalDate());
        assertEquals("2026-12-01", reader.getExpirationDate());
        assertEquals("Some notes", reader.getNotes());
    }

    @Test
    public void testStringProperty() {
        // Tạo đối tượng Reader
        Reader reader = new Reader("R001", "John Doe", "Student", "2024-12-01", "2025-12-01", "No notes");

        // Kiểm tra giá trị StringProperty
        assertEquals("R001", reader.readerIdProperty().get());
        assertEquals("John Doe", reader.readerNameProperty().get());
        assertEquals("Student", reader.informationProperty().get());
        assertEquals("2024-12-01", reader.renewalDateProperty().get());
        assertEquals("2025-12-01", reader.expirationDateProperty().get());
        assertEquals("No notes", reader.notesProperty().get());
    }

    @Test
    public void testStringPropertySetters() {
        // Tạo đối tượng Reader
        Reader reader = new Reader("R001", "John Doe", "Student", "2024-12-01", "2025-12-01", "No notes");

        // Thay đổi giá trị qua StringProperty
        reader.readerIdProperty().set("R002");
        reader.readerNameProperty().set("Jane Smith");
        reader.informationProperty().set("Teacher");
        reader.renewalDateProperty().set("2025-12-01");
        reader.expirationDateProperty().set("2026-12-01");
        reader.notesProperty().set("Updated notes");

        // Kiểm tra lại các giá trị đã thay đổi
        assertEquals("R002", reader.getReaderId());
        assertEquals("Jane Smith", reader.getReaderName());
        assertEquals("Teacher", reader.getInformation());
        assertEquals("2025-12-01", reader.getRenewalDate());
        assertEquals("2026-12-01", reader.getExpirationDate());
        assertEquals("Updated notes", reader.getNotes());
    }
}
