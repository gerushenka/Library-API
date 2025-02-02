package com.example.booktrackerservice.controller;

import com.example.booktrackerservice.dto.TrackerDto;
import com.example.booktrackerservice.entity.Tracker;
import com.example.booktrackerservice.service.TrackerService;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class TrackerControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Mock
    private TrackerService trackerService;

    @InjectMocks
    private TrackerController trackerController;

    private ObjectMapper objectMapper;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(trackerController).build();
        objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule()); // Регистрация модуля для работы с java.time
    }

    @Test
    public void testCreateBookTracker() throws Exception {
        TrackerDto trackerDto = new TrackerDto(null, 1L, "available", LocalDateTime.now(), null);

        mockMvc.perform(post("/tracker")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(trackerDto)))
                .andExpect(status().isOk());

        verify(trackerService, times(1)).createTracker(any(TrackerDto.class));
    }

    @Test
    public void testGetAvailableBooks() throws Exception {
        List<TrackerDto> availableBooks = Arrays.asList(
                new TrackerDto(1L, 1L, "available", LocalDateTime.now(), null),
                new TrackerDto(2L, 2L, "available", LocalDateTime.now(), null)
        );

        when(trackerService.getAvailableBooks()).thenReturn(availableBooks);

        mockMvc.perform(get("/tracker/available"))
                .andExpect(status().isOk());

        verify(trackerService, times(1)).getAvailableBooks();
    }

    @Test
    public void testUpdateBookStatus() throws Exception {
        mockMvc.perform(patch("/tracker/status/1"))
                .andExpect(status().isOk());

        verify(trackerService, times(1)).updateBookStatus(1L);
    }

    @Test
    public void testGetAllBooks() throws Exception {
        List<Tracker> allBooks = Arrays.asList(
                new Tracker(1L, 1L, "available", LocalDateTime.now(), null),
                new Tracker(2L, 2L, "checked out", LocalDateTime.now().plusDays(1), null)
        );

        when(trackerService.getAll()).thenReturn(allBooks);

        mockMvc.perform(get("/tracker/all"))
                .andExpect(status().isOk());

        verify(trackerService, times(1)).getAll();
    }

    @Test
    public void testDeleteBookTracker() throws Exception {
        mockMvc.perform(delete("/tracker/1"))
                .andExpect(status().isNoContent());

        verify(trackerService, times(1)).deleteTracker(1L);
    }
}
