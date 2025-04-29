package br.com.edu.ifce.maracanau.carekobooks.module.book.application.mapper;

import br.com.edu.ifce.maracanau.carekobooks.common.layer.application.mapper.annotation.IgnoreBaseModelFields;
import br.com.edu.ifce.maracanau.carekobooks.module.book.application.payload.response.BookReviewResponse;
import br.com.edu.ifce.maracanau.carekobooks.module.book.application.payload.request.BookReviewRequest;
import br.com.edu.ifce.maracanau.carekobooks.module.book.infrastructure.model.BookReview;
import br.com.edu.ifce.maracanau.carekobooks.module.user.application.mapper.UserMapper;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(
        componentModel = "spring",
        uses = {
                UserMapper.class,
                BookMapper.class
        }
)
public interface BookReviewMapper {

    @IgnoreBaseModelFields
    @Mapping(target = "user", expression = "java(userMapper.toModel(request.getUsername()))")
    @Mapping(target = "book", expression = "java(bookMapper.toModel(request.getBookId()))")
    BookReview toModel(BookReviewRequest request);
    BookReviewResponse toResponse(BookReview review);

    @IgnoreBaseModelFields
    @Mapping(target = "user", expression = "java(userMapper.toModel(request.getUsername()))")
    @Mapping(target = "book", expression = "java(bookMapper.toModel(request.getBookId()))")
    void updateModel(@MappingTarget BookReview review, BookReviewRequest request);

}
