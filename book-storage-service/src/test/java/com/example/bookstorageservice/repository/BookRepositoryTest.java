package com.example.bookstorageservice.repository;

import com.example.bookstorageservice.entity.Book;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.MySQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.utility.DockerImageName;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Testcontainers
@ActiveProfiles("test")
@Transactional
public class BookRepositoryTest {

    @Container
    private static final MySQLContainer<?> mysqlContainer = new MySQLContainer<>(DockerImageName.parse("mysql:8.0"))
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

    private Long existingBookId1;
    private Long existingBookId2;

    @BeforeEach
    void setUp() {
        List<Book> books = StreamSupport.stream(bookRepository.findAll().spliterator(), false)
                .collect(Collectors.toList());
        assertThat(books).isNotEmpty();

        existingBookId1 = books.get(0).getId();
        existingBookId2 = books.get(1).getId();
    }

    @Test
    void testFindByIsbn() {
        Book foundBook = bookRepository.findByIsbn("978-3-16-148410-0");
        assertThat(foundBook).isNotNull();
        assertThat(foundBook.getTitle()).isEqualTo("Book1");
    }

    @Test
    void testFindAllActiveBooks() {
        List<Book> books = bookRepository.findAllActiveBooks();
        assertThat(books).hasSize(2);
    }

    @Test
    void testSoftDeleteBook() {
        bookRepository.softDeleteBook(existingBookId1);

        List<Book> activeBooks = bookRepository.findAllActiveBooks();
        assertThat(activeBooks).hasSize(1); // Осталась только вторая книга
        assertThat(activeBooks.get(0).getId()).isEqualTo(existingBookId2);
    }
}