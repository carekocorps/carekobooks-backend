package br.com.edu.ifce.maracanau.carekobooks.module.forum.application.mapper;

import br.com.edu.ifce.maracanau.carekobooks.common.layer.application.mapper.annotation.IgnoreBaseModelFields;
import br.com.edu.ifce.maracanau.carekobooks.module.book.application.mapper.BookMapper;
import br.com.edu.ifce.maracanau.carekobooks.module.forum.application.representation.response.ForumResponse;
import br.com.edu.ifce.maracanau.carekobooks.module.forum.application.representation.request.ForumRequest;
import br.com.edu.ifce.maracanau.carekobooks.module.forum.infrastructure.repository.ForumRepository;
import br.com.edu.ifce.maracanau.carekobooks.module.forum.infrastructure.model.Forum;
import br.com.edu.ifce.maracanau.carekobooks.module.user.application.mapper.UserMapper;
import br.com.edu.ifce.maracanau.carekobooks.module.user.application.security.provider.UserContextProvider;
import org.mapstruct.*;
import org.springframework.beans.factory.annotation.Autowired;

@Mapper(
        componentModel = "spring",
        imports = UserContextProvider.class,
        uses = {
                UserMapper.class,
                BookMapper.class
        }
)
public abstract class ForumMapper {

    @Autowired
    protected ForumRepository forumRepository;

    @IgnoreBaseModelFields
    @Mapping(target = "replies", ignore = true)
    @Mapping(target = "user", expression = "java(UserContextProvider.getUser())")
    @Mapping(target = "book", expression = "java(bookMapper.toModel(request.getBookId()))")
    public abstract Forum toModel(ForumRequest request);
    public abstract ForumResponse toResponse(Forum forum);

    @IgnoreBaseModelFields
    @Mapping(target = "replies", ignore = true)
    @Mapping(target = "user", ignore = true)
    @Mapping(target = "book", expression = "java(bookMapper.toModel(request.getBookId()))")
    public abstract void updateModel(@MappingTarget Forum forum, ForumRequest request);

    public Forum toModel(Long id) {
        return forumRepository.findById(id).orElse(null);
    }

}
