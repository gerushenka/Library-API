package com.example.booktrackerservice.repository;

import com.example.booktrackerservice.entity.Tracker;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.transaction.annotation.Transactional;
import org.testcontainers.containers.MySQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Testcontainers
@ActiveProfiles("test")
@Transactional
public class TrackerRepositoryTest {

    private static final Logger logger = LoggerFactory.getLogger(TrackerRepositoryTest.class);

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
        registry.add("spring.flyway.enabled", () -> "true");
    }

    @Autowired
    private TrackerRepository trackerRepository;

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
    public void testFindByBookId() {
        Tracker foundTracker = trackerRepository.findByBookId(1L);
        assertNotNull(foundTracker);
        assertEquals(1L, foundTracker.getBookId());
        assertEquals("available", foundTracker.getStatus());
    }

    @Test
    public void testFindAvailableBooks() {
        List<Tracker> availableBooks = trackerRepository.findAvailableBooks();
        assertNotNull(availableBooks);

        assertEquals(2, availableBooks.size());
        assertEquals("available", availableBooks.get(0).getStatus());
    }

    @Test
    public void testDeleteByBookId() {
        trackerRepository.deleteByBookId(1L);

        Tracker deletedTracker = trackerRepository.findByBookId(1L);
        assertNull(deletedTracker);
    }
}