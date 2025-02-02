package com.example.booktrackerservice.entity;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import java.time.LocalDateTime;

public class TrackerTest {

    @Test
    public void testNoArgsConstructor() {
        Tracker tracker = new Tracker();
        assertNotNull(tracker);
    }

    @Test
    public void testAllArgsConstructor() {
        Long id = 1L;
        Long bookId = 2L;
        String status = "Checked Out";
        LocalDateTime checkoutTime = LocalDateTime.now();
        LocalDateTime returnTime = LocalDateTime.now().plusDays(1);

        Tracker tracker = new Tracker(id, bookId, status, checkoutTime, returnTime);

        assertEquals(id, tracker.getId());
        assertEquals(bookId, tracker.getBookId());
        assertEquals(status, tracker.getStatus());
        assertEquals(checkoutTime, tracker.getCheckoutTime());
        assertEquals(returnTime, tracker.getReturnTime());
    }

    @Test
    public void testGettersAndSetters() {
        Tracker tracker = new Tracker();

        Long id = 1L;
        Long bookId = 2L;
        String status = "Checked Out";
        LocalDateTime checkoutTime = LocalDateTime.now();
        LocalDateTime returnTime = LocalDateTime.now().plusDays(1);

        tracker.setId(id);
        tracker.setBookId(bookId);
        tracker.setStatus(status);
        tracker.setCheckoutTime(checkoutTime);
        tracker.setReturnTime(returnTime);

        assertEquals(id, tracker.getId());
        assertEquals(bookId, tracker.getBookId());
        assertEquals(status, tracker.getStatus());
        assertEquals(checkoutTime, tracker.getCheckoutTime());
        assertEquals(returnTime, tracker.getReturnTime());
    }
}
