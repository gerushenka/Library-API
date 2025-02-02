package com.example.bookstorageservice.dto;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class TrackerDtoTest {

    @Test
    void testTrackerDto() {
        TrackerDto trackerDto = new TrackerDto(1L, "available", "2023-10-01T10:00:00", "2023-10-02T10:00:00");
        assertEquals(1L, trackerDto.getBookId());
        assertEquals("available", trackerDto.getStatus());
        assertEquals("2023-10-01T10:00:00", trackerDto.getCheckoutTime());
        assertEquals("2023-10-02T10:00:00", trackerDto.getReturnTime());
    }
}