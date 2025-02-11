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
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.transaction.annotation.Transactional;
import org.testcontainers.containers.MySQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Testcontainers
@ActiveProfiles("test")
@Transactional
public class TrackerServiceTest {

    @Container
    private static final MySQLContainer<?> mysqlContainer = new MySQLContainer<>("mysql:8")
            .withDatabaseName("testdb")
            .withUsername("testuser")
            .withPassword("testpass");

    @DynamicPropertySource
    static void configureDataSourceProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", mysqlContainer::getJdbcUrl);
        registry.add("spring.datasource.username", mysqlContainer::getUsername);
        registry.add("spring.datasource.password", mysqlContainer::getPassword);
        registry.add("spring.datasource.driver-class-name", () -> "com.mysql.cj.jdbc.Driver");
        registry.add("spring.jpa.database-platform", () -> "org.hibernate.dialect.MySQLDialect");
    }

    @Autowired
    private TrackerRepository trackerRepository;

    @Autowired
    private TrackerMapper trackerMapper;

    @Autowired
    private TrackerService trackerService;

    private Long existingTrackerId1;
    private Long existingTrackerId2;

    @BeforeEach
    public void setUp() {
        List<Tracker> trackers = StreamSupport.stream(trackerRepository.findAll().spliterator(), false)
                .collect(Collectors.toList());
        assertNotNull(trackers);
        assertFalse(trackers.isEmpty());

        existingTrackerId1 = trackers.get(0).getId();
        existingTrackerId2 = trackers.get(1).getId();
    }

    @AfterEach
    void tearDown() {
    }

    @Test
    public void testCreateTracker() {
        TrackerDto newTrackerDto = new TrackerDto(null, 2L, "available", LocalDateTime.now(), null);
        trackerService.createTracker(newTrackerDto);

        List<Tracker> trackers = StreamSupport.stream(trackerRepository.findAll().spliterator(), false).toList();
        assertEquals(3, trackers.size());
        assertEquals("available", trackers.get(2).getStatus());
    }

    @Test
    public void testGetAvailableBooks() {
        List<TrackerDto> result = trackerService.getAvailableBooks();
        assertEquals(2, result.size());
    }

    @Test
    public void testUpdateBookStatus() {
        trackerService.updateBookStatus(existingTrackerId1);

        Tracker updatedTracker = trackerRepository.findById(existingTrackerId1).orElseThrow();
        assertEquals("unavailable", updatedTracker.getStatus());
        assertNotNull(updatedTracker.getCheckoutTime());
        assertNotNull(updatedTracker.getReturnTime());
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
        trackerService.deleteTracker(existingTrackerId1);

        List<Tracker> remainingTrackers = StreamSupport.stream(trackerRepository.findAll().spliterator(), false).toList();
        assertEquals(1, remainingTrackers.size());
    }
}