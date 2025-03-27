package br.com.edu.ifce.maracanau.carekobooks.module.book.application.mapper;

import br.com.edu.ifce.maracanau.carekobooks.module.book.application.representation.dto.BookReviewDTO;
import br.com.edu.ifce.maracanau.carekobooks.module.book.application.representation.request.BookReviewRequest;
import br.com.edu.ifce.maracanau.carekobooks.module.book.infrastructure.model.BookReview;
import br.com.edu.ifce.maracanau.carekobooks.module.user.application.mapper.UserMapper;
import br.com.edu.ifce.maracanau.carekobooks.shared.application.mapper.BaseUpdateMapper;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", uses = {UserMapper.class, BookMapper.class})
public interface BookReviewMapper extends BaseUpdateMapper<BookReview, BookReviewRequest> {

    @Mapping(target = "user", expression = "java(userMapper.toModel(true))")
    @Mapping(target = "book", expression = "java(bookMapper.toModel(request.getBookId()))")
    BookReview toModel(BookReviewRequest request);
    BookReviewDTO toDTO(BookReview bookReview);

}
