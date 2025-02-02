package com.example.bookstorageservice.entity;

import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class TrackerTest {

    @Test
    void testTracker() {
        LocalDateTime checkoutTime = LocalDateTime.of(2023, 10, 1, 10, 0);
        LocalDateTime returnTime = LocalDateTime.of(2023, 10, 2, 10, 0);
        Tracker tracker = new Tracker(1L, 1L, "available", checkoutTime, returnTime);
        assertEquals(1L, tracker.getId());
        assertEquals(1L, tracker.getBookId());
        assertEquals("available", tracker.getStatus());
        assertEquals(checkoutTime, tracker.getCheckoutTime());
        assertEquals(returnTime, tracker.getReturnTime());
    }
}