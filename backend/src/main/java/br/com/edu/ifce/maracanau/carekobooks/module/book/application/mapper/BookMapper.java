package br.com.edu.ifce.maracanau.carekobooks.module.book.application.mapper;

import br.com.edu.ifce.maracanau.carekobooks.module.book.application.dto.BookRequestDTO;
import br.com.edu.ifce.maracanau.carekobooks.module.book.application.dto.BookDTO;
import br.com.edu.ifce.maracanau.carekobooks.shared.application.mapper.BaseMapper;
import br.com.edu.ifce.maracanau.carekobooks.module.book.infra.model.Book;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface BookMapper extends BaseMapper<Book, BookRequestDTO> {

    Book toEntity(BookRequestDTO bookRequestDTO);

    BookDTO toDTO(Book book);

}
