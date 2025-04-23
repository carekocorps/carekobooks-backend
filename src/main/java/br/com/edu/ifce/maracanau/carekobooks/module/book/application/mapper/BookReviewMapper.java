package br.com.edu.ifce.maracanau.carekobooks.module.book.application.mapper;

import br.com.edu.ifce.maracanau.carekobooks.common.layer.application.mapper.annotation.IgnoreBaseModelFields;
import br.com.edu.ifce.maracanau.carekobooks.module.book.application.representation.response.BookReviewResponse;
import br.com.edu.ifce.maracanau.carekobooks.module.book.application.representation.request.BookReviewRequest;
import br.com.edu.ifce.maracanau.carekobooks.module.book.infrastructure.model.BookReview;
import br.com.edu.ifce.maracanau.carekobooks.module.user.application.mapper.UserMapper;
import br.com.edu.ifce.maracanau.carekobooks.module.user.application.security.provider.UserContextProvider;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(
        componentModel = "spring",
        imports = UserContextProvider.class,
        uses = {
                UserMapper.class,
                BookMapper.class
        }
)
public interface BookReviewMapper {

    @IgnoreBaseModelFields
    @Mapping(target = "user", expression = "java(UserContextProvider.getUser())")
    @Mapping(target = "book", expression = "java(bookMapper.toModel(request.getBookId()))")
    BookReview toModel(BookReviewRequest request);
    BookReviewResponse toResponse(BookReview review);

    @IgnoreBaseModelFields
    @Mapping(target = "user", ignore = true)
    @Mapping(target = "book", expression = "java(bookMapper.toModel(request.getBookId()))")
    void updateModel(@MappingTarget BookReview review, BookReviewRequest request);

}
