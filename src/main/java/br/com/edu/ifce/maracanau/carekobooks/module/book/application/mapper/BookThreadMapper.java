package br.com.edu.ifce.maracanau.carekobooks.module.book.application.mapper;

import br.com.edu.ifce.maracanau.carekobooks.common.layer.application.mapper.annotation.IgnoreBaseEntityFields;
import br.com.edu.ifce.maracanau.carekobooks.module.book.application.payload.response.BookThreadResponse;
import br.com.edu.ifce.maracanau.carekobooks.module.book.application.payload.request.BookThreadRequest;
import br.com.edu.ifce.maracanau.carekobooks.module.book.infrastructure.repository.BookThreadRepository;
import br.com.edu.ifce.maracanau.carekobooks.module.book.infrastructure.domain.entity.BookThread;
import br.com.edu.ifce.maracanau.carekobooks.module.user.application.mapper.UserMapper;
import lombok.Setter;
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

    @Setter(onMethod_ = @Autowired)
    protected BookThreadRepository bookThreadRepository;

    @IgnoreBaseEntityFields
    @Mapping(target = "replies", ignore = true)
    @Mapping(target = "user", expression = "java(userMapper.toEntity(request.getUsername()))")
    @Mapping(target = "book", expression = "java(bookMapper.toEntity(request.getBookId()))")
    public abstract BookThread toEntity(BookThreadRequest request);
    public abstract BookThreadResponse toResponse(BookThread thread);

    @IgnoreBaseEntityFields
    @Mapping(target = "replies", ignore = true)
    @Mapping(target = "user", expression = "java(userMapper.toEntity(request.getUsername()))")
    @Mapping(target = "book", expression = "java(bookMapper.toEntity(request.getBookId()))")
    public abstract void updateEntity(@MappingTarget BookThread thread, BookThreadRequest request);

    public BookThread toEntity(Long id) {
        return bookThreadRepository.findById(id).orElse(null);
    }

}
