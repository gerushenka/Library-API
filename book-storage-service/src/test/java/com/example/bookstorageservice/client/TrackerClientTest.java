package com.example.bookstorageservice.client;

import com.example.bookstorageservice.dto.TrackerDto;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
public class TrackerClientTest {

    @Mock
    private TrackerClient trackerClient;

    @Test
    void testCreateTracker() {
        TrackerDto trackerDto = new TrackerDto(1L, "available", "2023-10-01T10:00:00", "2023-10-02T10:00:00");
        trackerClient.createTracker(trackerDto);
        verify(trackerClient).createTracker(trackerDto);
    }

    @Test
    void testDeleteTracker() {
        Long bookId = 1L;
        trackerClient.deleteTracker(bookId);
        verify(trackerClient).deleteTracker(bookId);
    }
}
