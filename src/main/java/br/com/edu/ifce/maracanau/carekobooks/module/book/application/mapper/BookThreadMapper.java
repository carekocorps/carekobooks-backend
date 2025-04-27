package br.com.edu.ifce.maracanau.carekobooks.module.book.application.mapper;

import br.com.edu.ifce.maracanau.carekobooks.common.layer.application.mapper.annotation.IgnoreBaseModelFields;
import br.com.edu.ifce.maracanau.carekobooks.module.book.application.representation.response.BookThreadResponse;
import br.com.edu.ifce.maracanau.carekobooks.module.book.application.representation.request.BookThreadRequest;
import br.com.edu.ifce.maracanau.carekobooks.module.book.infrastructure.repository.BookThreadRepository;
import br.com.edu.ifce.maracanau.carekobooks.module.book.infrastructure.model.BookThread;
import br.com.edu.ifce.maracanau.carekobooks.module.user.application.mapper.UserMapper;
import br.com.edu.ifce.maracanau.carekobooks.module.user.application.security.context.provider.AuthenticatedUserProvider;
import org.mapstruct.*;
import org.springframework.beans.factory.annotation.Autowired;

@Mapper(
        componentModel = "spring",
        imports = AuthenticatedUserProvider.class,
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
    @Mapping(target = "user", expression = "java(AuthenticatedUserProvider.getAuthenticatedUser())")
    @Mapping(target = "book", expression = "java(bookMapper.toModel(request.getBookId()))")
    public abstract BookThread toModel(BookThreadRequest request);
    public abstract BookThreadResponse toResponse(BookThread thread);

    @IgnoreBaseModelFields
    @Mapping(target = "replies", ignore = true)
    @Mapping(target = "user", ignore = true)
    @Mapping(target = "book", expression = "java(bookMapper.toModel(request.getBookId()))")
    public abstract void updateModel(@MappingTarget BookThread thread, BookThreadRequest request);

    public BookThread toModel(Long id) {
        return bookThreadRepository.findById(id).orElse(null);
    }

}
