package br.com.edu.ifce.maracanau.carekobooks.module.book.application.mapper;

import br.com.edu.ifce.maracanau.carekobooks.common.layer.application.mapper.annotation.IgnoreBaseModelFields;
import br.com.edu.ifce.maracanau.carekobooks.module.book.application.payload.response.BookThreadResponse;
import br.com.edu.ifce.maracanau.carekobooks.module.book.application.payload.request.BookThreadRequest;
import br.com.edu.ifce.maracanau.carekobooks.module.book.infrastructure.repository.BookThreadRepository;
import br.com.edu.ifce.maracanau.carekobooks.module.book.infrastructure.domain.entity.BookThread;
import br.com.edu.ifce.maracanau.carekobooks.module.user.application.mapper.UserMapper;
import org.mapstruct.*;
import org.springframework.beans.factory.annotation.Autowired;

@Mapper(
        componentModel = "spring",
        uses = {
                UserMapper.class,
                BookMapper.class
        }
)
public abstract class BookThreadMapper {

    @Autowired
    protected BookThreadRepository bookThreadRepository;

    @IgnoreBaseModelFields
    @Mapping(target = "replies", ignore = true)
    @Mapping(target = "user", expression = "java(userMapper.toModel(request.getUsername()))")
    @Mapping(target = "book", expression = "java(bookMapper.toModel(request.getBookId()))")
    public abstract BookThread toModel(BookThreadRequest request);
    public abstract BookThreadResponse toResponse(BookThread thread);

    @IgnoreBaseModelFields
    @Mapping(target = "replies", ignore = true)
    @Mapping(target = "user", expression = "java(userMapper.toModel(request.getUsername()))")
    @Mapping(target = "book", expression = "java(bookMapper.toModel(request.getBookId()))")
    public abstract void updateModel(@MappingTarget BookThread thread, BookThreadRequest request);

    public BookThread toModel(Long id) {
        return bookThreadRepository.findById(id).orElse(null);
    }

}
