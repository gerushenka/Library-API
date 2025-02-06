package com.example.bookstorageservice.repository;

import com.example.bookstorageservice.entity.Book;
import com.example.bookstorageservice.entity.Tracker;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.utility.DockerImageName;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Testcontainers
public class BookRepositoryTest {

    @Container
    private static final PostgreSQLContainer<?> postgreSQLContainer = new PostgreSQLContainer<>(DockerImageName.parse("postgres:14"))
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
    private TrackerRepository trackerRepository;

    private Book book1;
    private Book book2;

    @BeforeEach
    void setUp() {
        book1 = new Book(null, "1234567890", "Book One", "Fiction", "Description One", "Author One", false);
        book2 = new Book(null, "0987654321", "Book Two", "Science", "Description Two", "Author Two", false);

        book1 = bookRepository.save(book1);
        book2 = bookRepository.save(book2);

        Tracker tracker1 = new Tracker(null, book1.getId(), "available", LocalDateTime.now().minusDays(10), null);
        Tracker tracker2 = new Tracker(null, book2.getId(), "borrowed", LocalDateTime.now().minusDays(5), LocalDateTime.now().plusDays(5));

        trackerRepository.save(tracker1);
        trackerRepository.save(tracker2);
    }

    @AfterEach
    void tearDown() {
        bookRepository.deleteAll();
        trackerRepository.deleteAll(); // Очистите трекеры после каждого теста
    }

    @Test
    void testFindByIsbn() {
        Book foundBook = bookRepository.findByIsbn("1234567890");

        assertThat(foundBook).isNotNull();
        assertThat(foundBook.getTitle()).isEqualTo("Book One");
    }

    @Test
    void testFindAllActiveBooks() {
        List<Book> books = bookRepository.findAllActiveBooks();

        assertThat(books).hasSize(2);
    }

    @Test
    void testSoftDeleteBook() {
        bookRepository.softDeleteBook(book1.getId());

        List<Book> books = bookRepository.findAllActiveBooks();

        assertThat(books).hasSize(1);
        assertThat(books.get(0).getId()).isEqualTo(book2.getId());
    }

    @Test
    void testFindAvailableBooks() {
        List<Book> books = bookRepository.findAvailableBooks();

        assertThat(books).hasSize(1);
        assertThat(books.get(0).getId()).isEqualTo(book1.getId());
    }
}