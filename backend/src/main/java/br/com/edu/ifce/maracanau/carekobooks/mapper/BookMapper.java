package br.com.edu.ifce.maracanau.carekobooks.mapper;

import br.com.edu.ifce.maracanau.carekobooks.dto.book.BookDTO;
import br.com.edu.ifce.maracanau.carekobooks.dto.book.BookRequestDTO;
import br.com.edu.ifce.maracanau.carekobooks.model.Book;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface BookMapper extends BaseMapper<Book, BookRequestDTO> {

    Book toEntity(BookRequestDTO bookRequestDTO);

    BookDTO toDTO(Book book);

}
