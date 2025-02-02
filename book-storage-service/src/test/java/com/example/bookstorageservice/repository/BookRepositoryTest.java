package com.example.bookstorageservice.repository;

import com.example.bookstorageservice.entity.Book;
import com.example.bookstorageservice.entity.Tracker;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(SpringExtension.class)
@DataJpaTest
@ActiveProfiles("test")
@Transactional
class BookRepositoryTest {

    @Autowired
    private BookRepository bookRepository;

    @Autowired
    private TestEntityManager entityManager;

    private Book book1;
    private Book book2;

    @BeforeEach
    void setUp() {
        book1 = new Book(null, "1234567890", "Book One", "Fiction", "Description One", "Author One", false);
        book2 = new Book(null, "0987654321", "Book Two", "Science", "Description Two", "Author Two", false);

        entityManager.persist(book1);
        entityManager.persist(book2);

        // Добавляем трекеры
        Tracker tracker1 = new Tracker(null, book1.getId(), "available", LocalDateTime.now().minusDays(10), null);
        Tracker tracker2 = new Tracker(null, book2.getId(), "borrowed", LocalDateTime.now().minusDays(5), LocalDateTime.now().plusDays(5));

        entityManager.persist(tracker1);
        entityManager.persist(tracker2);

        entityManager.flush();
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
        entityManager.flush();
        entityManager.clear(); // Очистка контекста Hibernate

        List<Book> books = bookRepository.findAllActiveBooks();

        System.out.println("Active books after deletion: " + books); // Проверяем, что в БД после soft-delete
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
