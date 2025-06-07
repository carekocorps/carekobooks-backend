package br.com.edu.ifce.maracanau.carekobooks.module.book.application.mapper;

import br.com.edu.ifce.maracanau.carekobooks.common.layer.application.mapper.annotation.IgnoreBaseEntityFields;
import br.com.edu.ifce.maracanau.carekobooks.module.book.application.payload.response.BookReviewResponse;
import br.com.edu.ifce.maracanau.carekobooks.module.book.application.payload.request.BookReviewRequest;
import br.com.edu.ifce.maracanau.carekobooks.module.book.infrastructure.domain.entity.BookReview;
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

    @IgnoreBaseEntityFields
    @Mapping(target = "user", expression = "java(userMapper.toEntity(request.getUsername()))")
    @Mapping(target = "book", expression = "java(bookMapper.toEntity(request.getBookId()))")
    BookReview toEntity(BookReviewRequest request);
    BookReviewResponse toResponse(BookReview review);

    @IgnoreBaseEntityFields
    @Mapping(target = "user", expression = "java(userMapper.toEntity(request.getUsername()))")
    @Mapping(target = "book", expression = "java(bookMapper.toEntity(request.getBookId()))")
    void updateEntity(@MappingTarget BookReview review, BookReviewRequest request);

}
