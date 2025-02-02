package com.example.booktrackerservice.mapper;

import com.example.booktrackerservice.dto.TrackerDto;
import com.example.booktrackerservice.entity.Tracker;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

public class TrackerMapperTest {

    private TrackerMapper trackerMapper;

    @BeforeEach
    public void setUp() {
        trackerMapper = Mappers.getMapper(TrackerMapper.class);
    }

    @Test
    public void testToDto() {
        Tracker tracker = new Tracker(1L, 1L, "available", LocalDateTime.now(), LocalDateTime.now().plusDays(1));
        TrackerDto trackerDto = trackerMapper.toDto(tracker);

        assertNotNull(trackerDto);
        assertEquals(tracker.getId(), trackerDto.getId());
        assertEquals(tracker.getBookId(), trackerDto.getBookId());
        assertEquals(tracker.getStatus(), trackerDto.getStatus());
        assertEquals(tracker.getCheckoutTime(), trackerDto.getCheckoutTime());
        assertEquals(tracker.getReturnTime(), trackerDto.getReturnTime());
    }

    @Test
    public void testToDtoWithNull() {
        TrackerDto trackerDto = trackerMapper.toDto(null);
        assertNull(trackerDto);
    }

    @Test
    public void testToEntity() {
        TrackerDto trackerDto = new TrackerDto(1L, 1L, "available", LocalDateTime.now(), LocalDateTime.now().plusDays(1));
        Tracker tracker = trackerMapper.toEntity(trackerDto);

        assertNotNull(tracker);
        assertEquals(trackerDto.getId(), tracker.getId());
        assertEquals(trackerDto.getBookId(), tracker.getBookId());
        assertEquals(trackerDto.getStatus(), tracker.getStatus());
        assertEquals(trackerDto.getCheckoutTime(), tracker.getCheckoutTime());
        assertEquals(trackerDto.getReturnTime(), tracker.getReturnTime());
    }

    @Test
    public void testToEntityWithNull() {
        Tracker tracker = trackerMapper.toEntity(null);
        assertNull(tracker);
    }
}
