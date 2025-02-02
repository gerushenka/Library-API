package com.example.bookstorageservice.mapper;

import com.example.bookstorageservice.dto.BookDto;
import com.example.bookstorageservice.entity.Book;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class BookMapperTest {

    private final BookMapper bookMapper = BookMapper.INSTANCE;

    @Test
    void testToDto() {
        Book book = new Book(1L, "1234567890", "Title", "Genre", "Description", "Author", false);
        BookDto bookDto = bookMapper.toDto(book);

        assertEquals(1L, bookDto.getId());
        assertEquals("1234567890", bookDto.getIsbn());
        assertEquals("Title", bookDto.getTitle());
        assertEquals("Genre", bookDto.getGenre());
        assertEquals("Description", bookDto.getDescription());
        assertEquals("Author", bookDto.getAuthor());
        assertEquals(false, bookDto.isDeleted());
    }

    @Test
    void testToDto_NullBook() {
        BookDto bookDto = bookMapper.toDto(null);
        assertNull(bookDto);
    }

    @Test
    void testToEntity() {
        BookDto bookDto = new BookDto(1L, "1234567890", "Title", "Genre", "Description", "Author", false);
        Book book = bookMapper.toEntity(bookDto);

        assertEquals(1L, book.getId());
        assertEquals("1234567890", book.getIsbn());
        assertEquals("Title", book.getTitle());
        assertEquals("Genre", book.getGenre());
        assertEquals("Description", book.getDescription());
        assertEquals("Author", book.getAuthor());
        assertEquals(false, book.isDeleted());
    }

    @Test
    void testToEntity_NullBookDto() {
        Book book = bookMapper.toEntity(null);
        assertNull(book);
    }
    @Test
    void testUpdateEntityFromDto() {
        BookDto bookDto = new BookDto(1L, "1234567890", "Title", "Genre", "Description", "Author", false);
        Book book = new Book();
        bookMapper.updateEntityFromDto(bookDto, book);

        assertEquals("1234567890", book.getIsbn());
        assertEquals("Title", book.getTitle());
        assertEquals("Genre", book.getGenre());
        assertEquals("Description", book.getDescription());
        assertEquals("Author", book.getAuthor());
        assertEquals(false, book.isDeleted());
    }

    @Test
    void testUpdateEntityFromDto_NullBookDto() {
        Book book = new Book();
        bookMapper.updateEntityFromDto(null, book);

        assertNull(book.getIsbn());
        assertNull(book.getTitle());
        assertNull(book.getGenre());
        assertNull(book.getDescription());
        assertNull(book.getAuthor());
        assertFalse(book.isDeleted()); // Предполагаем, что по умолчанию флаг deleted равен false
    }

}
