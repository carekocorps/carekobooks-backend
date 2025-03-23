package br.com.edu.ifce.maracanau.carekobooks.module.forum.application.mapper;

import br.com.edu.ifce.maracanau.carekobooks.module.book.application.mapper.BookMapper;
import br.com.edu.ifce.maracanau.carekobooks.module.forum.application.dto.ForumDTO;
import br.com.edu.ifce.maracanau.carekobooks.module.forum.application.request.ForumRequest;
import br.com.edu.ifce.maracanau.carekobooks.module.forum.infrastructure.repository.ForumRepository;
import br.com.edu.ifce.maracanau.carekobooks.module.user.application.mapper.UserMapper;
import br.com.edu.ifce.maracanau.carekobooks.module.forum.infrastructure.model.Forum;
import br.com.edu.ifce.maracanau.carekobooks.shared.module.application.mapper.BaseUpdateMapper;
import org.mapstruct.*;
import org.springframework.beans.factory.annotation.Autowired;

@Mapper(componentModel = "spring", uses = {UserMapper.class, BookMapper.class})
public abstract class ForumMapper implements BaseUpdateMapper<Forum, ForumRequest> {

    @Autowired
    protected ForumRepository forumRepository;

    @Mapping(source = "userId", target = "user", qualifiedByName = "toUserModelFromId")
    @Mapping(source = "bookId", target = "book", qualifiedByName = "toBookModelFromId")
    public abstract Forum toModel(ForumRequest request);
    public abstract ForumDTO toDTO(Forum forum);

    @Named("toForumModelFromId")
    public Forum toModel(Long id) {
        return forumRepository.findById(id).orElse(null);
    }

}
