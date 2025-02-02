package com.example.booktrackerservice.service;

import com.example.booktrackerservice.dto.TrackerDto;
import com.example.booktrackerservice.entity.Tracker;
import com.example.booktrackerservice.repositories.TrackerRepository;
import com.example.booktrackerservice.mapper.TrackerMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class TrackerService {

    @Autowired
    private TrackerRepository trackerRepository;

    @Autowired
    private TrackerMapper trackerMapper;

    public void createTracker(TrackerDto trackerDto) {
        Tracker tracker = new Tracker();
        tracker.setBookId(trackerDto.getBookId());
        tracker.setStatus(trackerDto.getStatus());
        tracker.setCheckoutTime(trackerDto.getCheckoutTime());
        tracker.setReturnTime(trackerDto.getReturnTime());
        trackerRepository.save(tracker);
    }

    public List<TrackerDto> getAvailableBooks() {
        List<Tracker> availableBooks = trackerRepository.findAvailableBooks();
        return availableBooks.stream()
                .map(trackerMapper::toDto)
                .collect(Collectors.toList());
    }

    public void updateBookStatus(Long id) {
        Tracker tracker = trackerRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Book not found"));
        String newStatus = "unavailable".equals(tracker.getStatus()) ? "available" : "unavailable";
        tracker.setStatus(newStatus);
        if ("unavailable".equals(newStatus)) {
            tracker.setCheckoutTime(LocalDateTime.now());
            tracker.setReturnTime(LocalDateTime.now().plus(7, ChronoUnit.DAYS));
        } else {
            tracker.setCheckoutTime(null);
            tracker.setReturnTime(null);
        }
        trackerRepository.save(tracker);
    }

    public Iterable<Tracker> getAll() {
        return trackerRepository.findAll();
    }

    public void deleteTracker(Long bookId) {
        trackerRepository.deleteByBookId(bookId);
    }
}
