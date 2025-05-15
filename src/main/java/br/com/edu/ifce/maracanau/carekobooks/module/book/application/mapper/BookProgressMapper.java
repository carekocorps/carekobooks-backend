package br.com.edu.ifce.maracanau.carekobooks.module.book.application.mapper;

import br.com.edu.ifce.maracanau.carekobooks.common.layer.application.mapper.annotation.IgnoreBaseModelFields;
import br.com.edu.ifce.maracanau.carekobooks.module.book.application.payload.response.BookProgressResponse;
import br.com.edu.ifce.maracanau.carekobooks.module.book.application.payload.request.BookProgressRequest;
import br.com.edu.ifce.maracanau.carekobooks.module.book.infrastructure.domain.entity.BookProgress;
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
public interface BookProgressMapper {

    @IgnoreBaseModelFields
    @Mapping(target = "user", expression = "java(userMapper.toModel(request.getUsername()))")
    @Mapping(target = "book", expression = "java(bookMapper.toModel(request.getBookId()))")
    BookProgress toModel(BookProgressRequest request);
    BookProgressResponse toResponse(BookProgress progress);

    @IgnoreBaseModelFields
    @Mapping(target = "user", expression = "java(userMapper.toModel(request.getUsername()))")
    @Mapping(target = "book", expression = "java(bookMapper.toModel(request.getBookId()))")
    void updateModel(@MappingTarget BookProgress progress, BookProgressRequest request);

}
