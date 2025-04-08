package br.com.edu.ifce.maracanau.carekobooks.module.book.application.mapper;

import br.com.edu.ifce.maracanau.carekobooks.module.book.application.representation.request.BookRequest;
import br.com.edu.ifce.maracanau.carekobooks.module.book.application.representation.response.BookResponse;
import br.com.edu.ifce.maracanau.carekobooks.module.book.infrastructure.repository.BookRepository;
import br.com.edu.ifce.maracanau.carekobooks.module.image.application.mapper.ImageMapper;
import br.com.edu.ifce.maracanau.carekobooks.common.layer.application.mapper.BaseUpdateMapper;
import br.com.edu.ifce.maracanau.carekobooks.module.book.infrastructure.model.Book;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.beans.factory.annotation.Autowired;

@Mapper(componentModel = "spring", config = BaseUpdateMapper.class, uses = {BookGenreMapper.class})
public abstract class BookMapper implements BaseUpdateMapper<Book, BookRequest> {

    @Autowired
    protected BookRepository bookRepository;

    @Autowired
    protected ImageMapper imageMapper;

    public abstract Book toModel(BookRequest bookRequest);

    @Mapping(target = "image", expression = "java(imageMapper.toResponse(book.getImage()))")
    public abstract BookResponse toResponse(Book book);

    public Book toModel(Long id) {
        return bookRepository.findById(id).orElse(null);
    }

}
