package com.example.booktrackerservice.mapper;

import com.example.booktrackerservice.dto.TrackerDto;
import com.example.booktrackerservice.entity.Tracker;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface TrackerMapper {

    TrackerDto toDto(Tracker bookTracker);

    Tracker toEntity(TrackerDto bookTrackerDto);
}
