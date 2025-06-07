package br.com.edu.ifce.maracanau.carekobooks.module.book.application.mapper;

import br.com.edu.ifce.maracanau.carekobooks.common.layer.application.mapper.annotation.IgnoreBaseEntityFields;
import br.com.edu.ifce.maracanau.carekobooks.module.book.application.payload.response.BookThreadReplyResponse;
import br.com.edu.ifce.maracanau.carekobooks.module.book.application.payload.request.BookThreadReplyRequest;
import br.com.edu.ifce.maracanau.carekobooks.module.book.infrastructure.domain.entity.BookThreadReply;
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

    @IgnoreBaseEntityFields
    @Mapping(target = "parent", ignore = true)
    @Mapping(target = "children", ignore = true)
    @Mapping(target = "user", expression = "java(userMapper.toEntity(request.getUsername()))")
    @Mapping(target = "thread", expression = "java(bookThreadMapper.toEntity(request.getThreadId()))")
    BookThreadReply toEntity(BookThreadReplyRequest request);
    BookThreadReplyResponse toResponse(BookThreadReply reply);

    @IgnoreBaseEntityFields
    @Mapping(target = "parent", ignore = true)
    @Mapping(target = "children", ignore = true)
    @Mapping(target = "user", expression = "java(userMapper.toEntity(request.getUsername()))")
    @Mapping(target = "thread", expression = "java(bookThreadMapper.toEntity(request.getThreadId()))")
    void updateEntity(@MappingTarget BookThreadReply reply, BookThreadReplyRequest request);

}
