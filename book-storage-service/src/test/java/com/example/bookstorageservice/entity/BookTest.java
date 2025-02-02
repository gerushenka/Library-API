package com.example.bookstorageservice.entity;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class BookTest {

    @Test
    void testBook() {
        Book book = new Book(1L, "1234567890", "Title", "Genre", "Description", "Author", false);
        assertEquals(1L, book.getId());
        assertEquals("1234567890", book.getIsbn());
        assertEquals("Title", book.getTitle());
        assertEquals("Genre", book.getGenre());
        assertEquals("Description", book.getDescription());
        assertEquals("Author", book.getAuthor());
        assertEquals(false, book.isDeleted());
    }
}
