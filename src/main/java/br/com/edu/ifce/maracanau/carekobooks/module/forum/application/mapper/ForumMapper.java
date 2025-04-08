package br.com.edu.ifce.maracanau.carekobooks.module.forum.application.mapper;

import br.com.edu.ifce.maracanau.carekobooks.module.book.application.mapper.BookMapper;
import br.com.edu.ifce.maracanau.carekobooks.module.forum.application.representation.response.ForumResponse;
import br.com.edu.ifce.maracanau.carekobooks.module.forum.application.representation.request.ForumRequest;
import br.com.edu.ifce.maracanau.carekobooks.module.forum.infrastructure.repository.ForumRepository;
import br.com.edu.ifce.maracanau.carekobooks.module.forum.infrastructure.model.Forum;
import br.com.edu.ifce.maracanau.carekobooks.module.user.application.mapper.UserContextMapper;
import br.com.edu.ifce.maracanau.carekobooks.module.user.application.mapper.UserMapper;
import br.com.edu.ifce.maracanau.carekobooks.common.layer.application.mapper.BaseUpdateMapper;
import org.mapstruct.*;
import org.springframework.beans.factory.annotation.Autowired;

@Mapper(componentModel = "spring", config = BaseUpdateMapper.class, uses = {UserMapper.class, BookMapper.class})
public abstract class ForumMapper implements BaseUpdateMapper<Forum, ForumRequest> {

    @Autowired
    protected ForumRepository forumRepository;

    @Autowired
    protected UserContextMapper userContextMapper;

    @Mapping(target = "user", expression = "java(userContextMapper.toModel())")
    @Mapping(target = "book", expression = "java(bookMapper.toModel(request.getBookId()))")
    public abstract Forum toModel(ForumRequest request);
    public abstract ForumResponse toResponse(Forum forum);

    public Forum toModel(Long id) {
        return forumRepository.findById(id).orElse(null);
    }

}
