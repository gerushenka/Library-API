package com.example.booktrackerservice.service;

import com.example.booktrackerservice.dto.TrackerDto;
import com.example.booktrackerservice.entity.Tracker;
import com.example.booktrackerservice.mapper.TrackerMapper;
import com.example.booktrackerservice.repository.TrackerRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class TrackerServiceTest {

    @Mock
    private TrackerRepository trackerRepository;

    @Mock
    private TrackerMapper trackerMapper;

    @InjectMocks
    private TrackerService trackerService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testCreateTracker() {
        TrackerDto trackerDto = new TrackerDto(null, 1L, "available", LocalDateTime.now(), null);
        Tracker tracker = new Tracker();
        tracker.setBookId(trackerDto.getBookId());
        tracker.setStatus(trackerDto.getStatus());
        tracker.setCheckoutTime(trackerDto.getCheckoutTime());
        tracker.setReturnTime(trackerDto.getReturnTime());

        trackerService.createTracker(trackerDto);

        verify(trackerRepository, times(1)).save(any(Tracker.class));
    }

    @Test
    public void testGetAvailableBooks() {
        List<Tracker> availableBooks = Arrays.asList(
                new Tracker(1L, 1L, "available", LocalDateTime.now(), null),
                new Tracker(2L, 2L, "available", LocalDateTime.now(), null)
        );
        when(trackerRepository.findAvailableBooks()).thenReturn(availableBooks);

        List<TrackerDto> result = trackerService.getAvailableBooks();

        assertEquals(2, result.size());
        verify(trackerRepository, times(1)).findAvailableBooks();
    }

    @Test
    public void testUpdateBookStatus_FromAvailableToUnavailable() {
        Tracker tracker = new Tracker(1L, 1L, "available", LocalDateTime.now(), null);
        when(trackerRepository.findById(1L)).thenReturn(Optional.of(tracker));

        trackerService.updateBookStatus(1L);

        assertEquals("unavailable", tracker.getStatus());
        assertNotNull(tracker.getCheckoutTime());
        assertNotNull(tracker.getReturnTime());
        verify(trackerRepository, times(1)).save(any(Tracker.class));
    }

    @Test
    public void testUpdateBookStatus_FromUnavailableToAvailable() {
        Tracker tracker = new Tracker(1L, 1L, "unavailable", LocalDateTime.now(), LocalDateTime.now().plusDays(7));
        when(trackerRepository.findById(1L)).thenReturn(Optional.of(tracker));

        trackerService.updateBookStatus(1L);

        assertEquals("available", tracker.getStatus());
        assertNull(tracker.getCheckoutTime());
        assertNull(tracker.getReturnTime());
        verify(trackerRepository, times(1)).save(any(Tracker.class));
    }

    @Test
    public void testUpdateBookStatus_NotFound() {
        when(trackerRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(IllegalArgumentException.class, () -> {
            trackerService.updateBookStatus(1L);
        });
    }

    @Test
    public void testGetAll() {
        List<Tracker> allBooks = Arrays.asList(
                new Tracker(1L, 1L, "available", LocalDateTime.now(), null),
                new Tracker(2L, 2L, "checked out", LocalDateTime.now().plusDays(1), null)
        );
        when(trackerRepository.findAll()).thenReturn(allBooks);

        Iterable<Tracker> result = trackerService.getAll();

        assertEquals(2, ((List<Tracker>) result).size());
        verify(trackerRepository, times(1)).findAll();
    }

    @Test
    public void testDeleteTracker() {
        trackerService.deleteTracker(1L);

        verify(trackerRepository, times(1)).deleteByBookId(1L);
    }
}
