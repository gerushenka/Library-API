package com.example.booktrackerservice.repository;

import com.example.booktrackerservice.entity.Tracker;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public interface TrackerRepository extends CrudRepository<Tracker, Long> {
    Tracker findByBookId(Long bookId);

    @Query("SELECT b FROM Tracker b WHERE b.status = 'available'")
    List<Tracker> findAvailableBooks();

    @Modifying
    @Transactional
    @Query("DELETE FROM Tracker b WHERE b.bookId = :bookId")
    void deleteByBookId(@Param("bookId") Long bookId);
}

