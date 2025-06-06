package br.com.edu.ifce.maracanau.carekobooks.module.book.application.mapper;

import br.com.edu.ifce.maracanau.carekobooks.common.layer.application.mapper.annotation.IgnoreBaseEntityFields;
import br.com.edu.ifce.maracanau.carekobooks.module.book.application.payload.response.BookActivityResponse;
import br.com.edu.ifce.maracanau.carekobooks.module.book.application.payload.request.BookProgressRequest;
import br.com.edu.ifce.maracanau.carekobooks.module.book.infrastructure.domain.entity.BookActivity;
import br.com.edu.ifce.maracanau.carekobooks.module.user.application.mapper.UserMapper;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(
        componentModel = "spring",
        uses = {
                UserMapper.class,
                BookMapper.class
        }
)
public interface BookActivityMapper {

    @IgnoreBaseEntityFields
    @Mapping(target = "user", expression = "java(userMapper.toEntity(request.getUsername()))")
    @Mapping(target = "book", expression = "java(bookMapper.toEntity(request.getBookId()))")
    BookActivity toEntity(BookProgressRequest request);
    BookActivityResponse toResponse(BookActivity activity);

}
