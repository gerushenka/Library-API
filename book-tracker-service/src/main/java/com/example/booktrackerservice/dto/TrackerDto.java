package com.example.booktrackerservice.dto;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TrackerDto {
    private Long id;
    private Long bookId;
    private String status;
    private LocalDateTime checkoutTime;
    private LocalDateTime returnTime;
}
