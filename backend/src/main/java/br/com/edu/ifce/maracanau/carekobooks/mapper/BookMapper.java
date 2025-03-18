package br.com.edu.ifce.maracanau.carekobooks.mapper;

import br.com.edu.ifce.maracanau.carekobooks.dto.book.BookDTO;
import br.com.edu.ifce.maracanau.carekobooks.dto.book.BookRequestDTO;
import br.com.edu.ifce.maracanau.carekobooks.model.Book;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface BookMapper {

    Book toEntity(BookRequestDTO bookRequestDTO);

    BookDTO toDTO(Book book);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    void updateEntity(@MappingTarget Book book, BookRequestDTO bookRequestDTO);

}
