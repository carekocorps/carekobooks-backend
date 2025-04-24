package br.com.edu.ifce.maracanau.carekobooks.module.book.application.mapper;

import br.com.edu.ifce.maracanau.carekobooks.common.layer.application.mapper.annotation.IgnoreBaseModelFields;
import br.com.edu.ifce.maracanau.carekobooks.module.book.application.representation.response.BookActivityResponse;
import br.com.edu.ifce.maracanau.carekobooks.module.book.application.representation.request.BookProgressRequest;
import br.com.edu.ifce.maracanau.carekobooks.module.book.infrastructure.model.BookActivity;
import br.com.edu.ifce.maracanau.carekobooks.module.user.application.mapper.UserMapper;
import br.com.edu.ifce.maracanau.carekobooks.module.user.application.security.provider.UserContextProvider;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(
        componentModel = "spring",
        imports = UserContextProvider.class,
        uses = {
                UserMapper.class,
                BookMapper.class
        }
)
public interface BookActivityMapper {

    @IgnoreBaseModelFields
    @Mapping(target = "user", expression = "java(UserContextProvider.getUser())")
    @Mapping(target = "book", expression = "java(bookMapper.toModel(request.getBookId()))")
    BookActivity toModel(BookProgressRequest request);
    BookActivityResponse toResponse(BookActivity activity);

}
