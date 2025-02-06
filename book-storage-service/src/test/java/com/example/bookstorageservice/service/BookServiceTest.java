package com.example.bookstorageservice.service;

import com.example.bookstorageservice.dto.BookDto;
import com.example.bookstorageservice.entity.Book;
import com.example.bookstorageservice.mapper.BookMapper;
import com.example.bookstorageservice.repository.BookRepository;
import com.example.bookstorageservice.client.TrackerClient;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Testcontainers
public class BookServiceTest {
    @Container
    private static final PostgreSQLContainer<?> postgreSQLContainer = new PostgreSQLContainer<>("postgres:14")
            .withDatabaseName("testdb")
            .withUsername("testuser")
            .withPassword("testpass");

    @DynamicPropertySource
    static void setDataSourceProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", postgreSQLContainer::getJdbcUrl);
        registry.add("spring.datasource.username", postgreSQLContainer::getUsername);
        registry.add("spring.datasource.password", postgreSQLContainer::getPassword);
        registry.add("spring.datasource.driver-class-name", () -> "org.postgresql.Driver");
    }

    @Autowired
    private BookRepository bookRepository;

    @Autowired
    private BookService bookService;

    @MockBean
    private TrackerClient trackerClient;

    private Book book;
    private BookDto bookDto;


    @BeforeEach
    void setUp() {
        book = new Book(null, "1234567890", "Book One", "Fiction", "Description One", "Author One", false);
        bookRepository.save(book);
        bookDto = BookMapper.INSTANCE.toDto(book);
    }

    @AfterEach
    void tearDown() {
        bookRepository.deleteAll();
    }


    @Test
    void testGetBookById() {
        BookDto result = bookService.getBookById(book.getId());
        assertThat(result).isNotNull();
        assertThat(result.getIsbn()).isEqualTo("1234567890");
        assertThat(result.getTitle()).isEqualTo("Book One");
    }

    @Test
    void testGetBookById_NotFound() {
        assertThrows(IllegalArgumentException.class, () -> bookService.getBookById(99L));
    }

    @Test
    void testGetBookByIsbn() {
        BookDto result = bookService.getBookByIsbn("1234567890");

        assertThat(result).isNotNull();
        assertThat(result.getIsbn()).isEqualTo("1234567890");
    }

    @Test
    void testAddBook() {
        BookDto newBookDto = new BookDto(null, "9876543210", "New Book", "Non-Fiction", "New Description", "New Author", false);
        BookDto result = bookService.addBook(newBookDto);

        assertThat(result).isNotNull();
        assertThat(result.getIsbn()).isEqualTo("9876543210");
    }

    @Test
    void testUpdateBook_NotFound() {
        BookDto updatedDto = new BookDto(99L, "1234567890", "Updated Book", "Fiction", "Updated Description", "Updated Author", false);
        assertThrows(IllegalArgumentException.class, () -> bookService.updateBook(99L, updatedDto));
    }

    @Test
    void testGetAllBooks() {
        List<BookDto> result = bookService.getAllBooks();

        assertThat(result).isNotEmpty();
        assertThat(result.get(0).getIsbn()).isEqualTo("1234567890");
    }

    @Test
    void testGetAllActiveBooks() {
        List<BookDto> result = bookService.getAllActiveBooks();

        assertThat(result).isNotEmpty();
        assertThat(result.get(0).getIsbn()).isEqualTo("1234567890");
    }

    @Test
    void testDeleteBook_NotFound() {
        assertThrows(IllegalArgumentException.class, () -> bookService.deleteBookById(99L));
    }

    @Test
    void testDeleteBook_Success() {
        Long bookId = book.getId();
        assertNotNull(bookId);
        bookService.deleteBookById(bookId);
        Optional<Book> deletedBook = bookRepository.findById(bookId);
        assertTrue(deletedBook.isPresent());
        assertTrue(deletedBook.get().isDeleted());
    }

    @Test
    void testDeleteBook_AlreadyDeleted() {
        book.setDeleted(true);
        bookRepository.save(book);

        assertThrows(IllegalArgumentException.class, () -> bookService.deleteBookById(1L));
    }

    @Test
    void testUpdateBook_BookNotFound() {
        BookDto updatedDto = new BookDto(99L, "1234567890", "Updated Book", "Fiction", "Updated Description", "Updated Author", false);
        assertThrows(IllegalArgumentException.class, () -> bookService.updateBook(99L, updatedDto));
    }

    @Test
    void testAddBook_BookAlreadyExists() {
        assertThrows(IllegalArgumentException.class, () -> bookService.addBook(bookDto));
    }

    @Test
    void testAddBook_RestoreDeletedBook() {
        book.setDeleted(true);
        bookRepository.save(book);

        BookDto result = bookService.addBook(bookDto);

        assertThat(result).isNotNull();
        assertThat(result.getIsbn()).isEqualTo("1234567890");
        assertThat(result.isDeleted()).isFalse();
    }

    @Test
    void testAddBook_NewBook() {
        BookDto newBookDto = new BookDto(null, "9876543210", "New Book", "Non-Fiction", "New Description", "New Author", false);
        BookDto result = bookService.addBook(newBookDto);

        assertThat(result).isNotNull();
        assertThat(result.getIsbn()).isEqualTo("9876543210");
    }

    @Test
    void testGetBookByIsbn_NotFound() {
        assertThrows(IllegalArgumentException.class, () -> bookService.getBookByIsbn("0000000000"));
    }

    @Test
    void testUpdateBook_AlreadyDeleted() {
        book.setDeleted(true);
        bookRepository.save(book);

        BookDto updatedDto = new BookDto(1L, "1234567890", "Updated Book", "Fiction", "Updated Description", "Updated Author", false);
        assertThrows(IllegalArgumentException.class, () -> bookService.updateBook(1L, updatedDto));
    }

    @Test
    void testGetBookById_Deleted() {
        book.setDeleted(true);
        bookRepository.save(book);

        assertThrows(IllegalArgumentException.class, () -> bookService.getBookById(1L));
    }

    @Test
    void testUpdateBook_Success() {
        Long bookId = book.getId();
        assertNotNull(bookId);
        BookDto updatedDto = new BookDto(bookId, "1234567890", "Updated Title", "Fiction", "Updated Description", "Updated Author", false);
        BookDto result = bookService.updateBook(bookId, updatedDto);
        assertNotNull(result);
        assertEquals("Updated Title", result.getTitle());
        assertEquals("Updated Description", result.getDescription());
        assertEquals("Updated Author", result.getAuthor());
    }
}