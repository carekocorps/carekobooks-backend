package br.com.edu.ifce.maracanau.carekobooks.module.book.application.mapper;

import br.com.edu.ifce.maracanau.carekobooks.common.layer.application.mapper.annotation.IgnoreBaseModelFields;
import br.com.edu.ifce.maracanau.carekobooks.module.book.application.payload.response.BookActivityResponse;
import br.com.edu.ifce.maracanau.carekobooks.module.book.application.payload.request.BookProgressRequest;
import br.com.edu.ifce.maracanau.carekobooks.module.book.infrastructure.model.BookActivity;
import br.com.edu.ifce.maracanau.carekobooks.module.user.application.mapper.UserMapper;
import br.com.edu.ifce.maracanau.carekobooks.module.user.application.security.context.provider.AuthenticatedUserProvider;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(
        componentModel = "spring",
        imports = AuthenticatedUserProvider.class,
        uses = {
                UserMapper.class,
                BookMapper.class
        }
)
public interface BookActivityMapper {

    @IgnoreBaseModelFields
    @Mapping(target = "user", expression = "java(AuthenticatedUserProvider.getAuthenticatedUser())")
    @Mapping(target = "book", expression = "java(bookMapper.toModel(request.getBookId()))")
    BookActivity toModel(BookProgressRequest request);
    BookActivityResponse toResponse(BookActivity activity);

}
