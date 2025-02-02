package com.example.bookstorageservice.controller;

import com.example.bookstorageservice.dto.BookDto;
import com.example.bookstorageservice.service.BookService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import static org.springframework.http.HttpStatus.*;

@ExtendWith(MockitoExtension.class)
class BookControllerTest {

    @Mock
    private BookService bookService;

    @InjectMocks
    private BookController bookController;

    private BookDto sampleBook;

    @BeforeEach
    void setUp() {
        sampleBook = new BookDto(1L, "1234567890", "Test Book", "Fiction", "A test book description", "Test Author", false);
    }

    @Test
    void getBookById_Success() {
        when(bookService.getBookById(1L)).thenReturn(sampleBook);
        ResponseEntity<BookDto> response = bookController.getBookById(1L);
        assertEquals(OK, response.getStatusCode());
        assertEquals(sampleBook, response.getBody());
    }

    @Test
    void getBookById_NotFound() {
        when(bookService.getBookById(2L)).thenThrow(new IllegalArgumentException());
        ResponseEntity<BookDto> response = bookController.getBookById(2L);
        assertEquals(NOT_FOUND, response.getStatusCode());
    }

    @Test
    void getAllBooks() {
        List<BookDto> books = Arrays.asList(sampleBook);
        when(bookService.getAllBooks()).thenReturn(books);
        ResponseEntity<List<BookDto>> response = bookController.getAllBooks();
        assertEquals(OK, response.getStatusCode());
        assertEquals(books, response.getBody());
    }

    @Test
    void getAllActiveBooks() {
        List<BookDto> books = Arrays.asList(sampleBook);
        when(bookService.getAllActiveBooks()).thenReturn(books);
        ResponseEntity<List<BookDto>> response = bookController.getAllActiveBooks();
        assertEquals(OK, response.getStatusCode());
        assertEquals(books, response.getBody());
    }

    @Test
    void getBookByIsbn_Success() {
        when(bookService.getBookByIsbn("1234567890")).thenReturn(sampleBook);
        ResponseEntity<BookDto> response = bookController.getBookByIsbn("1234567890");
        assertEquals(OK, response.getStatusCode());
        assertEquals(sampleBook, response.getBody());
    }

    @Test
    void getBookByIsbn_NotFound() {
        when(bookService.getBookByIsbn("0000000000")).thenThrow(new IllegalArgumentException());
        ResponseEntity<BookDto> response = bookController.getBookByIsbn("0000000000");
        assertEquals(NOT_FOUND, response.getStatusCode());
    }

    @Test
    void deleteBookById_Success() {
        doNothing().when(bookService).deleteBookById(1L);
        ResponseEntity<Void> response = bookController.deleteBookById(1L);
        assertEquals(NO_CONTENT, response.getStatusCode());
    }

    @Test
    void deleteBookById_BadRequest() {
        doThrow(new IllegalArgumentException()).when(bookService).deleteBookById(2L);
        ResponseEntity<Void> response = bookController.deleteBookById(2L);
        assertEquals(BAD_REQUEST, response.getStatusCode());
    }

    @Test
    void addBook() {
        when(bookService.addBook(sampleBook)).thenReturn(sampleBook);
        ResponseEntity<BookDto> response = bookController.addBook(sampleBook);
        assertEquals(CREATED, response.getStatusCode());
        assertEquals(sampleBook, response.getBody());
    }

    @Test
    void updateBook_Success() {
        when(bookService.updateBook(eq(1L), any(BookDto.class))).thenReturn(sampleBook);
        ResponseEntity<BookDto> response = bookController.updateBook(1L, sampleBook);
        assertEquals(OK, response.getStatusCode());
        assertEquals(sampleBook, response.getBody());
    }

    @Test
    void updateBook_BadRequest() {
        when(bookService.updateBook(eq(2L), any(BookDto.class))).thenThrow(new IllegalArgumentException());
        ResponseEntity<BookDto> response = bookController.updateBook(2L, sampleBook);
        assertEquals(BAD_REQUEST, response.getStatusCode());
    }

    @Test
    void getAvailableBooks() {
        List<BookDto> books = Arrays.asList(sampleBook);
        when(bookService.getAvailableBooks()).thenReturn(books);
        List<BookDto> response = bookController.getAvailableBooks();
        assertEquals(books, response);
    }
}