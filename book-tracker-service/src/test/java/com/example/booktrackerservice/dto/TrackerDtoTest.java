package com.example.booktrackerservice.dto;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import java.time.LocalDateTime;

public class TrackerDtoTest {

    @Test
    public void testNoArgsConstructor() {
        TrackerDto dto = new TrackerDto();
        assertNotNull(dto);
    }

    @Test
    public void testAllArgsConstructor() {
        Long id = 1L;
        Long bookId = 2L;
        String status = "Checked Out";
        LocalDateTime checkoutTime = LocalDateTime.now();
        LocalDateTime returnTime = LocalDateTime.now().plusDays(1);

        TrackerDto dto = new TrackerDto(id, bookId, status, checkoutTime, returnTime);

        assertEquals(id, dto.getId());
        assertEquals(bookId, dto.getBookId());
        assertEquals(status, dto.getStatus());
        assertEquals(checkoutTime, dto.getCheckoutTime());
        assertEquals(returnTime, dto.getReturnTime());
    }

    @Test
    public void testGettersAndSetters() {
        TrackerDto dto = new TrackerDto();

        Long id = 1L;
        Long bookId = 2L;
        String status = "Checked Out";
        LocalDateTime checkoutTime = LocalDateTime.now();
        LocalDateTime returnTime = LocalDateTime.now().plusDays(1);

        dto.setId(id);
        dto.setBookId(bookId);
        dto.setStatus(status);
        dto.setCheckoutTime(checkoutTime);
        dto.setReturnTime(returnTime);

        assertEquals(id, dto.getId());
        assertEquals(bookId, dto.getBookId());
        assertEquals(status, dto.getStatus());
        assertEquals(checkoutTime, dto.getCheckoutTime());
        assertEquals(returnTime, dto.getReturnTime());
    }
}
