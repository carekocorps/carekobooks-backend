package br.com.edu.ifce.maracanau.carekobooks.module.forum.application.mapper;

import br.com.edu.ifce.maracanau.carekobooks.module.forum.application.dto.ForumReplyDTO;
import br.com.edu.ifce.maracanau.carekobooks.module.forum.application.request.ForumReplyRequest;
import br.com.edu.ifce.maracanau.carekobooks.module.forum.infrastructure.model.ForumReply;
import br.com.edu.ifce.maracanau.carekobooks.module.user.application.mapper.UserMapper;
import br.com.edu.ifce.maracanau.carekobooks.shared.application.mapper.BaseMapper;
import org.mapstruct.*;

@Mapper(componentModel = "spring", uses = {UserMapper.class, ForumMapper.class})
public abstract class ForumReplyMapper implements BaseMapper<ForumReply, ForumReplyRequest> {

    @Mapping(source = "userId", target = "user", qualifiedByName = "toUserModelFromId")
    @Mapping(source = "forumId", target = "forum", qualifiedByName = "toForumModelFromId")
    public abstract ForumReply toModel(ForumReplyRequest request);
    public abstract ForumReplyDTO toDTO(ForumReply forumReply);

}