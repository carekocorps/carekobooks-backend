package br.com.edu.ifce.maracanau.carekobooks.module.book.application.mapper;

import br.com.edu.ifce.maracanau.carekobooks.module.book.application.representation.dto.BookReviewDTO;
import br.com.edu.ifce.maracanau.carekobooks.module.book.application.representation.request.BookReviewRequest;
import br.com.edu.ifce.maracanau.carekobooks.module.book.infrastructure.model.BookReview;
import br.com.edu.ifce.maracanau.carekobooks.module.user.application.mapper.UserContextMapper;
import br.com.edu.ifce.maracanau.carekobooks.module.user.application.mapper.UserMapper;
import br.com.edu.ifce.maracanau.carekobooks.shared.application.mapper.BaseUpdateMapper;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.beans.factory.annotation.Autowired;

@Mapper(componentModel = "spring", uses = {UserMapper.class, BookMapper.class})
public abstract class BookReviewMapper implements BaseUpdateMapper<BookReview, BookReviewRequest> {

    @Autowired
    protected UserContextMapper userContextMapper;

    @Mapping(target = "user", expression = "java(userContextMapper.toModel())")
    @Mapping(target = "book", expression = "java(bookMapper.toModel(request.getBookId()))")
    public abstract BookReview toModel(BookReviewRequest request);
    public abstract BookReviewDTO toDTO(BookReview bookReview);

}
