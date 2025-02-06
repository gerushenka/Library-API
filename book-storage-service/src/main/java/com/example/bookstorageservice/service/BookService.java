package com.example.bookstorageservice.service;

import com.example.bookstorageservice.dto.BookDto;
import com.example.bookstorageservice.dto.TrackerDto;
import com.example.bookstorageservice.entity.Book;
import com.example.bookstorageservice.mapper.BookMapper;
import com.example.bookstorageservice.repository.BookRepository;
import com.example.bookstorageservice.client.TrackerClient;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Service
public class BookService {

    @Autowired
    private BookRepository bookRepository;

    @Autowired
    private BookMapper bookMapper;

    @Autowired
    private TrackerClient trackerClient;

    public BookDto getBookById(Long id) {
        Book book = bookRepository.findById(id).orElse(null);
        if (book != null && !book.isDeleted()) {
            return BookMapper.INSTANCE.toDto(book);
        } else {
            throw new IllegalArgumentException("Book not found or deleted");
        }
    }

    public List<BookDto> getAllBooks() {
        Iterable<Book> booksIterable = bookRepository.findAll();
        List<Book> books = StreamSupport.stream(booksIterable.spliterator(), false)
                .collect(Collectors.toList());
        return books.stream()
                .map(book -> {
                    BookDto bookDto = BookMapper.INSTANCE.toDto(book);
                    bookDto.setDeleted(book.isDeleted());
                    return bookDto;
                })
                .collect(Collectors.toList());
    }

    public List<BookDto> getAllActiveBooks() {
        List<Book> books = bookRepository.findAllActiveBooks();
        return books.stream()
                .map(BookMapper.INSTANCE::toDto)
                .collect(Collectors.toList());
    }

    public BookDto getBookByIsbn(String isbn) {
        Book book = bookRepository.findByIsbn(isbn);
        if (book != null && !book.isDeleted()) {
            return BookMapper.INSTANCE.toDto(book);
        } else {
            throw new IllegalArgumentException("Book not found or deleted");
        }
    }

    @Transactional
    public void deleteBookById(Long id) {
        Optional<Book> optionalBook = bookRepository.findById(id);
        if (optionalBook.isPresent()) {
            Book book = optionalBook.get();
            if (book.isDeleted()) {
                throw new IllegalArgumentException("Book is already deleted");
            }
            bookRepository.softDeleteBook(id);
            trackerClient.deleteTracker(id);
        } else {
            throw new IllegalArgumentException("Book not found");
        }
    }

    @Transactional
    public BookDto addBook(BookDto bookDto) {
        Book existingBook = bookRepository.findByIsbn(bookDto.getIsbn());
        if (existingBook != null) {
            if (existingBook.isDeleted()) {
                existingBook.setDeleted(false);
                Book savedBook = bookRepository.save(existingBook);

                TrackerDto trackerDto = new TrackerDto(savedBook.getId(), "available", null, null);
                trackerClient.createTracker(trackerDto);

                return BookMapper.INSTANCE.toDto(savedBook);
            } else {
                throw new IllegalArgumentException("Book with the same ISBN already exists");
            }
        } else {
            Book book = BookMapper.INSTANCE.toEntity(bookDto);
            Book savedBook = bookRepository.save(book);

            TrackerDto trackerDto = new TrackerDto(savedBook.getId(), "available", null, null);
            trackerClient.createTracker(trackerDto);

            return BookMapper.INSTANCE.toDto(savedBook);
        }
    }

    @Transactional
    public BookDto updateBook(Long id, BookDto bookDto) {
        Optional<Book> optionalBook = bookRepository.findById(id);
        if (optionalBook.isPresent()) {
            Book existingBook = optionalBook.get();
            if (existingBook.isDeleted()) {
                throw new IllegalArgumentException("Book is already deleted");
            }
            bookMapper.updateEntityFromDto(bookDto, existingBook);
            Book savedBook = bookRepository.save(existingBook);
            return bookMapper.toDto(savedBook);
        } else {
            throw new IllegalArgumentException("Book not found");
        }
    }

    public List<BookDto> getAvailableBooks() {
        List<Book> availableBooks = bookRepository.findAvailableBooks();
        return availableBooks.stream()
                .map(bookMapper::toDto)
                .collect(Collectors.toList());
    }
}
