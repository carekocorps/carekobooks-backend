package br.com.edu.ifce.maracanau.carekobooks.module.book.application.mapper;

import br.com.edu.ifce.maracanau.carekobooks.common.layer.application.mapper.annotation.IgnoreBaseModelFields;
import br.com.edu.ifce.maracanau.carekobooks.module.book.application.representation.response.BookProgressResponse;
import br.com.edu.ifce.maracanau.carekobooks.module.book.application.representation.request.BookProgressRequest;
import br.com.edu.ifce.maracanau.carekobooks.module.book.infrastructure.model.BookProgress;
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
public interface BookProgressMapper {

    @IgnoreBaseModelFields
    @Mapping(target = "user", expression = "java(UserContextProvider.getUser())")
    @Mapping(target = "book", expression = "java(bookMapper.toModel(request.getBookId()))")
    BookProgress toModel(BookProgressRequest request);
    BookProgressResponse toResponse(BookProgress progress);

    @IgnoreBaseModelFields
    @Mapping(target = "user", ignore = true)
    @Mapping(target = "book", expression = "java(bookMapper.toModel(request.getBookId()))")
    void updateModel(@MappingTarget BookProgress model, BookProgressRequest request);

}
