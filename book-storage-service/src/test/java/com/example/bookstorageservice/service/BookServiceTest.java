package com.example.bookstorageservice.service;

import com.example.bookstorageservice.dto.BookDto;
import com.example.bookstorageservice.entity.Book;
import com.example.bookstorageservice.repository.BookRepository;
import com.example.bookstorageservice.client.TrackerClient;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.transaction.annotation.Transactional;
import org.testcontainers.containers.MySQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Testcontainers
@ActiveProfiles("test")
@Transactional
public class BookServiceTest {

    @Container
    private static final MySQLContainer<?> mysqlContainer = new MySQLContainer<>("mysql:8")
            .withDatabaseName("testdb")
            .withUsername("testuser")
            .withPassword("testpass");

    @DynamicPropertySource
    static void setDataSourceProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", mysqlContainer::getJdbcUrl);
        registry.add("spring.datasource.username", mysqlContainer::getUsername);
        registry.add("spring.datasource.password", mysqlContainer::getPassword);
        registry.add("spring.datasource.driver-class-name", () -> "com.mysql.cj.jdbc.Driver");
    }

    @Autowired
    private BookRepository bookRepository;

    @Autowired
    private BookService bookService;

    @MockBean
    private TrackerClient trackerClient;

    private Long existingBookId1;
    private Long existingBookId2;

    @BeforeEach
    void setUp() {
        List<Book> books = StreamSupport.stream(bookRepository.findAll().spliterator(), false)
                .collect(Collectors.toList());

        assertNotNull(books);
        assertFalse(books.isEmpty());
        existingBookId1 = books.get(0).getId();
        existingBookId2 = books.get(1).getId();
    }

    @AfterEach
    void tearDown() {
    }

    @Test
    void testGetBookById() {
        BookDto result = bookService.getBookById(existingBookId1);
        assertNotNull(result);
        assertEquals("978-3-16-148410-0", result.getIsbn());
        assertEquals("Book1", result.getTitle());
    }

    @Test
    void testGetBookById_NotFound() {
        assertThrows(IllegalArgumentException.class, () -> bookService.getBookById(99L));
    }

    @Test
    void testGetBookByIsbn() {
        BookDto result = bookService.getBookByIsbn("978-3-16-148410-0");
        assertNotNull(result);
        assertEquals("978-3-16-148410-0", result.getIsbn());
        assertEquals("Book1", result.getTitle());
    }

    @Test
    void testAddBook_BookAlreadyExists() {
        BookDto newBookDto = new BookDto(null, "978-3-16-148410-0", "Book1", "Fiction", "Description1", "Author1", false);
        assertThrows(IllegalArgumentException.class, () -> bookService.addBook(newBookDto));
    }

    @Test
    void testUpdateBook_Success() {
        BookDto updatedDto = new BookDto(existingBookId1, "978-3-16-148410-0", "Updated Title", "Fiction", "Updated Description", "Updated Author", false);
        BookDto result = bookService.updateBook(existingBookId1, updatedDto);
        assertNotNull(result);
        assertEquals("Updated Title", result.getTitle());
        assertEquals("Updated Description", result.getDescription());
        assertEquals("Updated Author", result.getAuthor());
    }

    @Test
    void testUpdateBook_BookNotFound() {
        BookDto updatedDto = new BookDto(99L, "978-3-16-148410-0", "Updated Title", "Fiction", "Updated Description", "Updated Author", false);
        assertThrows(IllegalArgumentException.class, () -> bookService.updateBook(99L, updatedDto));
    }

    @Test
    void testGetAllBooks() {
        List<BookDto> result = bookService.getAllBooks();
        assertNotNull(result);
        assertFalse(result.isEmpty());
        assertEquals(2, result.size());
    }

    @Test
    void testGetAllActiveBooks() {
        List<BookDto> result = bookService.getAllActiveBooks();
        assertNotNull(result);
        assertFalse(result.isEmpty());
        assertEquals(2, result.size());
    }

    @Test
    void testDeleteBook_Success() {
        bookService.deleteBookById(existingBookId1);
        Optional<Book> deletedBook = bookRepository.findById(existingBookId1);
        assertTrue(deletedBook.isPresent());
        assertTrue(deletedBook.get().isDeleted());
    }

    @Test
    void testDeleteBook_AlreadyDeleted() {
        bookService.deleteBookById(existingBookId1);
        assertThrows(IllegalArgumentException.class, () -> bookService.deleteBookById(existingBookId1));
    }

    @Test
    void testGetBookByIsbn_NotFound() {
        assertThrows(IllegalArgumentException.class, () -> bookService.getBookByIsbn("0000000000"));
    }
}