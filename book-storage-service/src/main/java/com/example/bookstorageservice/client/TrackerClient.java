package com.example.bookstorageservice.client;

import com.example.bookstorageservice.dto.TrackerDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "book-tracker-service", url = "http://localhost:8082")
public interface TrackerClient {

    @PostMapping("/tracker")
    void createTracker(@RequestBody TrackerDto trackerDto);

    @DeleteMapping("/tracker/{bookId}")
    void deleteTracker(@PathVariable Long bookId);
}
