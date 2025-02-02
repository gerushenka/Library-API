package com.example.booktrackerservice.repository;

import com.example.booktrackerservice.entity.Tracker;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@DataJpaTest
@ActiveProfiles("test")
@Transactional
public class TrackerRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private TrackerRepository trackerRepository;

    private Tracker tracker1;
    private Tracker tracker2;

    @BeforeEach
    public void setUp() {
        tracker1 = new Tracker(null, 1L, "available", LocalDateTime.now(), null);
        tracker2 = new Tracker(null, 2L, "checked out", LocalDateTime.now(), LocalDateTime.now().plusDays(1));

        entityManager.persist(tracker1);
        entityManager.persist(tracker2);
        entityManager.flush();
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
