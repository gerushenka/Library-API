package com.example.bookstorageservice.repositories;

import com.example.bookstorageservice.entity.Book;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public interface BookRepository extends CrudRepository<Book, Long> {
    Book findByIsbn(String isbn);

    @Query("SELECT b FROM Book b JOIN Tracker t ON b.id = t.bookId WHERE t.status = 'available'")
    List<Book> findAvailableBooks();

    @Modifying
    @Transactional
    @Query("UPDATE Book b SET b.deleted = true WHERE b.id = :id")
    void softDeleteBook(@Param("id") Long id);

    @Query("SELECT b FROM Book b WHERE b.deleted = false")
    List<Book> findAllActiveBooks();
}
