package com.example.bookstorageservice.mapper;

import com.example.bookstorageservice.dto.BookDto;
import com.example.bookstorageservice.entity.Book;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface BookMapper {
    BookMapper INSTANCE = Mappers.getMapper(BookMapper.class);


    @Mapping(source = "id", target = "id")
    @Mapping(source = "isbn", target = "isbn")
    @Mapping(source = "title", target = "title")
    @Mapping(source = "genre", target = "genre")
    @Mapping(source = "description", target = "description")
    @Mapping(source = "author", target = "author")
    @Mapping(source = "deleted", target = "deleted")
    BookDto toDto(Book book);

    @Mapping(source = "id", target = "id")
    @Mapping(source = "isbn", target = "isbn")
    @Mapping(source = "title", target = "title")
    @Mapping(source = "genre", target = "genre")
    @Mapping(source = "description", target = "description")
    @Mapping(source = "author", target = "author")
    Book toEntity(BookDto bookDto);

    @Mapping(target = "id", ignore = true)
    void updateEntityFromDto(BookDto bookDto, @MappingTarget Book book);
}
