package com.example.bookstorageservice.controller;

import com.example.bookstorageservice.dto.BookDto;
import com.example.bookstorageservice.service.BookService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.servlet.http.HttpServletRequest; // Обновленный импорт
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/library")
public class BookController {

    @Autowired
    private BookService bookService;

    @Operation(summary = "Get a book by ID")
    @GetMapping("/book/{id}")
    public ResponseEntity<BookDto> getBookById(@PathVariable Long id) {
        try {
            BookDto bookDto = bookService.getBookById(id);
            return new ResponseEntity<>(bookDto, HttpStatus.OK);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @Operation(summary = "Get all books")
    @GetMapping("/book/all")
    public ResponseEntity<List<BookDto>> getAllBooks() {
        List<BookDto> books = bookService.getAllBooks();
        return ResponseEntity.ok(books);
    }

    @Operation(summary = "Get all books except deleted ones")
    @GetMapping("/book/active")
    public ResponseEntity<List<BookDto>> getAllActiveBooks() {
        List<BookDto> books = bookService.getAllActiveBooks();
        return ResponseEntity.ok(books);
    }

    @Operation(summary = "Get a book by isbn")
    @GetMapping("/isbn/{isbn}")
    public ResponseEntity<BookDto> getBookByIsbn(@PathVariable String isbn) {
        try {
            BookDto bookDto = bookService.getBookByIsbn(isbn);
            return new ResponseEntity<>(bookDto, HttpStatus.OK);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @Operation(summary = "Delete a book by ID")
    @DeleteMapping("/book/{id}")
    public ResponseEntity<Void> deleteBookById(@PathVariable Long id) {
        try {
            bookService.deleteBookById(id);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @Operation(summary = "Add a book")
    @PostMapping("/book")
    public ResponseEntity<BookDto> addBook(@RequestBody BookDto bookDto) {
        BookDto savedBookDto = bookService.addBook(bookDto);
        return new ResponseEntity<>(savedBookDto, HttpStatus.CREATED);
    }

    @Operation(summary = "Change book information")
    @PutMapping("/book/{id}")
    public ResponseEntity<BookDto> updateBook(@PathVariable Long id, @RequestBody BookDto bookDto) {
        try {
            BookDto updatedBookDto = bookService.updateBook(id, bookDto);
            return new ResponseEntity<>(updatedBookDto, HttpStatus.OK);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @Operation(summary = "Get all the books available in the library")
    @GetMapping("/available")
    public List<BookDto> getAvailableBooks() {
        return bookService.getAvailableBooks();
    }
}
