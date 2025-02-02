package com.example.bookstorageservice.service;

import com.example.bookstorageservice.dto.BookDto;
import com.example.bookstorageservice.entity.Book;
import com.example.bookstorageservice.mapper.BookMapper;
import com.example.bookstorageservice.repository.BookRepository;
import com.example.bookstorageservice.client.TrackerClient;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;


import static org.mockito.Mockito.*;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

import java.util.List;
import java.util.Optional;

@ExtendWith(MockitoExtension.class)
class BookServiceTest {

    @Mock
    private BookRepository bookRepository;

    @Mock
    private TrackerClient trackerClient;

    @Mock
    private BookMapper bookMapper;

    @InjectMocks
    private BookService bookService;

    private Book book;

    private BookDto bookDto;

    @BeforeEach
    void setUp() {
        book = new Book(1L, "1234567890", "Book One", "Fiction", "Description One", "Author One", false);
        bookDto = new BookDto(1L, "1234567890", "Book One", "Fiction", "Description One", "Author One", false);
    }

    @Test
    void testGetBookById() {
        when(bookRepository.findById(1L)).thenReturn(Optional.of(book));

        BookDto result = bookService.getBookById(1L);

        assertThat(result).isNotNull();
        assertThat(result.getIsbn()).isEqualTo("1234567890");
        assertThat(result.getTitle()).isEqualTo("Book One");
    }

    @Test
    void testGetBookById_NotFound() {
        when(bookRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(IllegalArgumentException.class, () -> bookService.getBookById(1L));
    }

    @Test
    void testGetBookByIsbn() {
        when(bookRepository.findByIsbn("1234567890")).thenReturn(book);

        BookDto result = bookService.getBookByIsbn("1234567890");

        assertThat(result).isNotNull();
        assertThat(result.getIsbn()).isEqualTo("1234567890");
    }

    @Test
    void testAddBook() {
        when(bookRepository.findByIsbn("1234567890")).thenReturn(null);
        when(bookRepository.save(any(Book.class))).thenReturn(book);

        BookDto result = bookService.addBook(bookDto);

        assertThat(result).isNotNull();
        assertThat(result.getIsbn()).isEqualTo("1234567890");
        verify(trackerClient, times(1)).createTracker(any());
    }

    @Test
    void testUpdateBook_NotFound() {
        when(bookRepository.findById(1L)).thenReturn(Optional.empty());

        BookDto updatedDto = new BookDto(1L, "1234567890", "Updated Book", "Fiction", "Updated Description", "Updated Author", false);

        assertThrows(IllegalArgumentException.class, () -> bookService.updateBook(1L, updatedDto));
    }

    @Test
    void testGetAllBooks() {
        List<Book> books = List.of(book);
        when(bookRepository.findAll()).thenReturn(books);

        List<BookDto> result = bookService.getAllBooks();

        assertThat(result).isNotEmpty();
        assertThat(result.get(0).getIsbn()).isEqualTo("1234567890");
    }

    @Test
    void testGetAllActiveBooks() {
        List<Book> activeBooks = List.of(book);
        when(bookRepository.findAllActiveBooks()).thenReturn(activeBooks);

        List<BookDto> result = bookService.getAllActiveBooks();

        assertThat(result).isNotEmpty();
        assertThat(result.get(0).getIsbn()).isEqualTo("1234567890");
    }


    @Test
    void testDeleteBook_NotFound() {
        when(bookRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(IllegalArgumentException.class, () -> bookService.deleteBookById(1L));
    }

    @Test
    void testDeleteBook_Success() {
        when(bookRepository.findById(1L)).thenReturn(Optional.of(book));
        doNothing().when(bookRepository).softDeleteBook(1L);  // Используйте doNothing() для void методов

        bookService.deleteBookById(1L);

        verify(bookRepository, times(1)).softDeleteBook(1L);
        verify(trackerClient, times(1)).deleteTracker(1L);
    }


    @Test
    void testDeleteBook_AlreadyDeleted() {
        book.setDeleted(true);
        when(bookRepository.findById(1L)).thenReturn(Optional.of(book));

        assertThrows(IllegalArgumentException.class, () -> bookService.deleteBookById(1L));
    }

    @Test
    void testUpdateBook_BookNotFound() {
        BookDto updatedDto = new BookDto(1L, "1234567890", "Updated Book", "Fiction", "Updated Description", "Updated Author", false);
        when(bookRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(IllegalArgumentException.class, () -> bookService.updateBook(1L, updatedDto));
    }

    @Test
    void testAddBook_BookAlreadyExists() {
        when(bookRepository.findByIsbn("1234567890")).thenReturn(book);

        assertThrows(IllegalArgumentException.class, () -> bookService.addBook(bookDto));
    }

    @Test
    void testAddBook_RestoreDeletedBook() {
        book.setDeleted(true);
        when(bookRepository.findByIsbn("1234567890")).thenReturn(book);
        when(bookRepository.save(any(Book.class))).thenReturn(book);

        BookDto result = bookService.addBook(bookDto);

        assertThat(result).isNotNull();
        assertThat(result.getIsbn()).isEqualTo("1234567890");
        assertThat(result.isDeleted()).isFalse();
        verify(trackerClient, times(1)).createTracker(any());
    }

    @Test
    void testAddBook_NewBook() {
        when(bookRepository.findByIsbn("1234567890")).thenReturn(null);
        when(bookRepository.save(any(Book.class))).thenReturn(book);

        BookDto result = bookService.addBook(bookDto);

        assertThat(result).isNotNull();
        assertThat(result.getIsbn()).isEqualTo("1234567890");
        verify(trackerClient, times(1)).createTracker(any());
    }

    @Test
    void testGetBookByIsbn_NotFound() {
        when(bookRepository.findByIsbn("1234567890")).thenReturn(null);

        assertThrows(IllegalArgumentException.class, () -> bookService.getBookByIsbn("1234567890"));
    }

    @Test
    void testUpdateBook_AlreadyDeleted() {
        Book deletedBook = new Book(1L, "1234567890", "Book One", "Fiction", "Description One", "Author One", true);
        when(bookRepository.findById(1L)).thenReturn(Optional.of(deletedBook));

        BookDto updatedDto = new BookDto(1L, "1234567890", "Updated Book", "Fiction", "Updated Description", "Updated Author", false);

        assertThrows(IllegalArgumentException.class, () -> bookService.updateBook(1L, updatedDto));
    }

    @Test
    void testGetBookById_Deleted() {
        Book deletedBook = new Book(1L, "1234567890", "Book One", "Fiction", "Description One", "Author One", true);
        when(bookRepository.findById(1L)).thenReturn(Optional.of(deletedBook));

        assertThrows(IllegalArgumentException.class, () -> bookService.getBookById(1L));
    }

    @Test
    void testUpdateBook_Success() {
        Book existingBook = new Book(1L, "1234567890", "Book One", "Fiction", "Description One", "Author One", false);
        when(bookRepository.findById(1L)).thenReturn(Optional.of(existingBook));

        BookDto updatedDto = new BookDto(1L, "1234567890", "Updated Book", "Fiction", "Updated Description", "Updated Author", false);

        when(bookRepository.save(any(Book.class))).thenReturn(existingBook);

        BookDto result = bookService.updateBook(1L, updatedDto);

        assertThat(result).isNull();
    }


}
