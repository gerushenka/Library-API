package com.example.booktrackerservice.controller;

import com.example.booktrackerservice.dto.TrackerDto;
import com.example.booktrackerservice.entity.Tracker;
import com.example.booktrackerservice.service.TrackerService;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/tracker")
public class TrackerController {

    @Autowired
    private TrackerService trackerService;

    @Operation(summary = "Creating a book when it adds into a storage.")
    @PostMapping
    public void createBookTracker(@RequestBody TrackerDto trackerDto) {
        trackerService.createTracker(trackerDto);
    }

    @Operation(summary = "Get books with status 'Available' ")
    @GetMapping("/available")
    public List<TrackerDto> getAvailableBooks() {
        return trackerService.getAvailableBooks();
    }

    @Operation(summary = "Change status of a book")
    @PatchMapping("/status/{id}")
    public void updateBookStatus(@PathVariable Long id) {
        trackerService.updateBookStatus(id);
    }

    @Operation(summary = "Get all books statuses")
    @GetMapping("/all")
    public Iterable<Tracker> getAll() {
        return trackerService.getAll();
    }

    @Operation(summary = "Delete a book status")
    @DeleteMapping("/{bookId}")
    public ResponseEntity<Void> deleteBookTracker(@PathVariable Long bookId) {
        trackerService.deleteTracker(bookId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}