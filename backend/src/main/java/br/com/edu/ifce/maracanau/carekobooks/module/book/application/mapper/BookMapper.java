package br.com.edu.ifce.maracanau.carekobooks.module.book.application.mapper;

import br.com.edu.ifce.maracanau.carekobooks.module.book.application.request.BookRequest;
import br.com.edu.ifce.maracanau.carekobooks.module.book.application.dto.BookDTO;
import br.com.edu.ifce.maracanau.carekobooks.module.book.infrastructure.repository.BookRepository;
import br.com.edu.ifce.maracanau.carekobooks.shared.module.application.mapper.BaseUpdateMapper;
import br.com.edu.ifce.maracanau.carekobooks.module.book.infrastructure.model.Book;
import org.mapstruct.Mapper;
import org.mapstruct.Named;
import org.springframework.beans.factory.annotation.Autowired;

@Mapper(componentModel = "spring")
public abstract class BookMapper implements BaseUpdateMapper<Book, BookRequest> {

    @Autowired
    protected BookRepository bookRepository;

    public abstract Book toModel(BookRequest bookRequest);
    public abstract BookDTO toDTO(Book book);

    @Named("toBookModelFromId")
    public Book toModel(Long id) {
        return bookRepository.findById(id).orElse(null);
    }

}
