package com.example.booktrackerservice.service;

import com.example.booktrackerservice.dto.TrackerDto;
import com.example.booktrackerservice.entity.Tracker;
import com.example.booktrackerservice.mapper.TrackerMapper;
import com.example.booktrackerservice.repository.TrackerRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Testcontainers
public class TrackerServiceTest {

    @Container
    private static final PostgreSQLContainer<?> postgreSQLContainer = new PostgreSQLContainer<>("postgres:14")
            .withDatabaseName("testdb")
            .withUsername("testuser")
            .withPassword("testpass");

    @DynamicPropertySource
    static void configureDataSourceProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", postgreSQLContainer::getJdbcUrl);
        registry.add("spring.datasource.username", postgreSQLContainer::getUsername);
        registry.add("spring.datasource.password", postgreSQLContainer::getPassword);
        registry.add("spring.jpa.database-platform", () -> "org.hibernate.dialect.PostgreSQLDialect");
        registry.add("spring.datasource.driver-class-name", () -> "org.postgresql.Driver");
        registry.add("spring.flyway.enabled", () -> "false");
    }

    @Autowired
    private TrackerRepository trackerRepository;

    @Autowired
    private TrackerMapper trackerMapper;

    @Autowired
    private TrackerService trackerService;

    private Tracker tracker;

    @BeforeEach
    public void setUp() {
        tracker = new Tracker(null, 1L, "available", LocalDateTime.now(), null);
        trackerRepository.save(tracker);
        tracker = new Tracker(null, 2L, "unavailable", LocalDateTime.now(), LocalDateTime.now().plusDays(7));
        trackerRepository.save(tracker);

    }

    @AfterEach
    public void tearDown() {
        trackerRepository.deleteAll();
    }

    @Test
    public void testCreateTracker() {
        TrackerDto trackerDto = new TrackerDto(null, 3L, "available", LocalDateTime.now(), null);
        trackerService.createTracker(trackerDto);
        List<Tracker> trackers = StreamSupport.stream(trackerRepository.findAll().spliterator(), false).toList();
        assertEquals(3, trackers.size());
        assertEquals("available", trackers.get(2).getStatus());
    }

    @Test
    public void testGetAvailableBooks() {
        List<TrackerDto> result = trackerService.getAvailableBooks();
        assertEquals(1, result.size());
    }

    @Test
    public void testUpdateBookStatus_FromAvailableToUnavailable() {
        Long bookId = tracker.getId() - 1;
        trackerService.updateBookStatus(bookId);
        Tracker updatedTracker = trackerRepository.findById(bookId).orElseThrow();
        assertEquals("unavailable", updatedTracker.getStatus());
        assertNotNull(updatedTracker.getCheckoutTime());
        assertNotNull(updatedTracker.getReturnTime());
    }

    @Test
    public void testUpdateBookStatus_FromUnavailableToAvailable() {
        Long bookId = tracker.getId();

        trackerService.updateBookStatus(bookId);

        Tracker updatedTracker = trackerRepository.findById(bookId).orElseThrow();
        assertEquals("available", updatedTracker.getStatus());
        assertNull(updatedTracker.getCheckoutTime());
        assertNull(updatedTracker.getReturnTime());
    }

    @Test
    public void testUpdateBookStatus_NotFound() {
        assertThrows(IllegalArgumentException.class, () -> trackerService.updateBookStatus(999L));
    }

    @Test
    public void testGetAll() {
        Iterable<Tracker> iterableResult = trackerService.getAll();

        List<Tracker> resultList = StreamSupport.stream(iterableResult.spliterator(), false)
                .collect(Collectors.toList());

        assertEquals(2, resultList.size());
    }

    @Test
    public void testDeleteTracker() {
        trackerService.deleteTracker(1L);
        List<Tracker> remainingTrackers = StreamSupport.stream(trackerRepository.findAll().spliterator(), false).toList();
        assertEquals(1, remainingTrackers.size());
    }
}