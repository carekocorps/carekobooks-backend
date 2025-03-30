package br.com.edu.ifce.maracanau.carekobooks.module.forum.application.mapper;

import br.com.edu.ifce.maracanau.carekobooks.module.forum.application.representation.dto.ForumReplyDTO;
import br.com.edu.ifce.maracanau.carekobooks.module.forum.application.representation.request.ForumReplyRequest;
import br.com.edu.ifce.maracanau.carekobooks.module.forum.infrastructure.model.ForumReply;
import br.com.edu.ifce.maracanau.carekobooks.module.user.application.mapper.UserContextMapper;
import br.com.edu.ifce.maracanau.carekobooks.module.user.application.mapper.UserMapper;
import br.com.edu.ifce.maracanau.carekobooks.shared.layer.application.mapper.BaseUpdateMapper;
import org.mapstruct.*;
import org.springframework.beans.factory.annotation.Autowired;

@Mapper(componentModel = "spring", uses = {UserMapper.class, ForumMapper.class})
public abstract class ForumReplyMapper implements BaseUpdateMapper<ForumReply, ForumReplyRequest> {

    @Autowired
    protected UserContextMapper userContextMapper;

    @Mapping(target = "user", expression = "java(userContextMapper.toModel())")
    @Mapping(target = "forum", expression = "java(forumMapper.toModel(request.getForumId()))")
    public abstract ForumReply toModel(ForumReplyRequest request);
    public abstract ForumReplyDTO toDTO(ForumReply forumReply);

}
