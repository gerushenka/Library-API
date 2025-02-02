package com.example.bookstorageservice.dto;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class BookDtoTest {

    @Test
    void testBookDto() {
        BookDto bookDto = new BookDto(1L, "1234567890", "Title", "Genre", "Description", "Author", false);
        assertEquals(1L, bookDto.getId());
        assertEquals("1234567890", bookDto.getIsbn());
        assertEquals("Title", bookDto.getTitle());
        assertEquals("Genre", bookDto.getGenre());
        assertEquals("Description", bookDto.getDescription());
        assertEquals("Author", bookDto.getAuthor());
        assertEquals(false, bookDto.isDeleted());
    }
}
