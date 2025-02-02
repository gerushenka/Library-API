package com.example.bookstorageservice.entity;

import jakarta.persistence.*;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Book {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String isbn;
    private String title;
    private String genre;
    private String description;
    private String author;

    @Column(nullable = false, columnDefinition = "boolean default false")
    private boolean deleted = false;
}
