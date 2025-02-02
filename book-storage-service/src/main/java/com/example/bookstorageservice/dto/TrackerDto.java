package com.example.bookstorageservice.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TrackerDto {
    private Long bookId;
    private String status;
    private String checkoutTime;
    private String returnTime;
}
