package com.example.bookstorageservice.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BookDto {
    private Long id;
    private String isbn;
    private String title;
    private String genre;
    private String description;
    private String author;
    private boolean deleted;
}
