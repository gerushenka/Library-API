package com.example.booktrackerservice.repository;

import com.example.booktrackerservice.entity.Tracker;
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

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Testcontainers
public class TrackerRepositoryTest {

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

    private Tracker tracker1;
    private Tracker tracker2;

    @BeforeEach
    public void setUp() {
        trackerRepository.deleteAll();

        tracker1 = new Tracker(null, 1L, "available", LocalDateTime.now(), null);
        tracker2 = new Tracker(null, 2L, "unavailable", LocalDateTime.now(), LocalDateTime.now().plusDays(1));

        trackerRepository.save(tracker1);
        trackerRepository.save(tracker2);
    }

    @AfterEach
    public void tearDown() {
        trackerRepository.deleteAll();
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
        assertEquals(1, availableBooks.size());
        assertEquals("available", availableBooks.get(0).getStatus());
    }

    @Test
    public void testDeleteByBookId() {
        trackerRepository.deleteByBookId(1L);

        Tracker deletedTracker = trackerRepository.findByBookId(1L);
        assertNull(deletedTracker);
    }
}