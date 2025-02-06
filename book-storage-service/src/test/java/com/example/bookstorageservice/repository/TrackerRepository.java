package com.example.bookstorageservice.repository;

import com.example.bookstorageservice.entity.Tracker;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TrackerRepository extends JpaRepository<Tracker, Long> {
    List<Tracker> findByStatus(String status);
    List<Tracker> findByBookId(Long bookId);
}
