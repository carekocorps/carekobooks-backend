package br.com.edu.ifce.maracanau.carekobooks.module.forum.application.mapper;

import br.com.edu.ifce.maracanau.carekobooks.module.forum.application.representation.dto.ForumReplyDTO;
import br.com.edu.ifce.maracanau.carekobooks.module.forum.application.representation.request.ForumReplyRequest;
import br.com.edu.ifce.maracanau.carekobooks.module.forum.infrastructure.model.ForumReply;
import br.com.edu.ifce.maracanau.carekobooks.module.user.application.mapper.UserMapper;
import br.com.edu.ifce.maracanau.carekobooks.shared.application.mapper.BaseUpdateMapper;
import org.mapstruct.*;

@Mapper(componentModel = "spring", uses = {UserMapper.class, ForumMapper.class})
public interface ForumReplyMapper extends BaseUpdateMapper<ForumReply, ForumReplyRequest> {

    @Mapping(target = "user", expression = "java(userMapper.toModel(true))")
    @Mapping(target = "forum", expression = "java(forumMapper.toModel(request.getForumId()))")
    ForumReply toModel(ForumReplyRequest request);
    ForumReplyDTO toDTO(ForumReply forumReply);

}
