package br.com.edu.ifce.maracanau.carekobooks.module.book.application.mapper;

import br.com.edu.ifce.maracanau.carekobooks.common.layer.application.mapper.annotation.IgnoreBaseModelFields;
import br.com.edu.ifce.maracanau.carekobooks.module.book.application.payload.response.BookThreadReplyResponse;
import br.com.edu.ifce.maracanau.carekobooks.module.book.application.payload.request.BookThreadReplyRequest;
import br.com.edu.ifce.maracanau.carekobooks.module.book.infrastructure.model.BookThreadReply;
import br.com.edu.ifce.maracanau.carekobooks.module.user.application.mapper.UserMapper;
import org.mapstruct.*;

@Mapper(
        componentModel = "spring",
        uses = {
                UserMapper.class,
                BookThreadMapper.class
        }
)
public interface BookThreadReplyMapper {

    @IgnoreBaseModelFields
    @Mapping(target = "parent", ignore = true)
    @Mapping(target = "children", ignore = true)
    @Mapping(target = "user", expression = "java(userMapper.toModel(request.getUsername()))")
    @Mapping(target = "thread", expression = "java(bookThreadMapper.toModel(request.getThreadId()))")
    BookThreadReply toModel(BookThreadReplyRequest request);
    BookThreadReplyResponse toResponse(BookThreadReply reply);

    @IgnoreBaseModelFields
    @Mapping(target = "parent", ignore = true)
    @Mapping(target = "children", ignore = true)
    @Mapping(target = "user", expression = "java(userMapper.toModel(request.getUsername()))")
    @Mapping(target = "thread", expression = "java(bookThreadMapper.toModel(request.getThreadId()))")
    void updateModel(@MappingTarget BookThreadReply reply, BookThreadReplyRequest request);

}
