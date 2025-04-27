package br.com.edu.ifce.maracanau.carekobooks.module.book.application.mapper;

import br.com.edu.ifce.maracanau.carekobooks.common.layer.application.mapper.annotation.IgnoreBaseModelFields;
import br.com.edu.ifce.maracanau.carekobooks.module.book.application.representation.response.BookThreadReplyResponse;
import br.com.edu.ifce.maracanau.carekobooks.module.book.application.representation.request.BookThreadReplyRequest;
import br.com.edu.ifce.maracanau.carekobooks.module.book.infrastructure.model.BookThreadReply;
import br.com.edu.ifce.maracanau.carekobooks.module.user.application.mapper.UserMapper;
import br.com.edu.ifce.maracanau.carekobooks.module.user.application.security.context.provider.AuthenticatedUserProvider;
import org.mapstruct.*;

@Mapper(
        componentModel = "spring",
        imports = AuthenticatedUserProvider.class,
        uses = {
                UserMapper.class,
                BookThreadMapper.class
        }
)
public interface BookThreadReplyMapper {

    @IgnoreBaseModelFields
    @Mapping(target = "parent", ignore = true)
    @Mapping(target = "children", ignore = true)
    @Mapping(target = "user", expression = "java(AuthenticatedUserProvider.getAuthenticatedUser())")
    @Mapping(target = "thread", expression = "java(bookThreadMapper.toModel(request.getThreadId()))")
    BookThreadReply toModel(BookThreadReplyRequest request);
    BookThreadReplyResponse toResponse(BookThreadReply bookThreadReply);

    @IgnoreBaseModelFields
    @Mapping(target = "parent", ignore = true)
    @Mapping(target = "children", ignore = true)
    @Mapping(target = "user", ignore = true)
    @Mapping(target = "thread", expression = "java(bookThreadMapper.toModel(request.getThreadId()))")
    void updateModel(@MappingTarget BookThreadReply reply, BookThreadReplyRequest request);

}
