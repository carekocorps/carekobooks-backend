package br.com.edu.ifce.maracanau.carekobooks.module.forum.application.mapper;

import br.com.edu.ifce.maracanau.carekobooks.module.forum.application.dto.ForumReplyDTO;
import br.com.edu.ifce.maracanau.carekobooks.module.forum.application.request.ForumReplyRequest;
import br.com.edu.ifce.maracanau.carekobooks.module.forum.infrastructure.model.ForumReply;
import br.com.edu.ifce.maracanau.carekobooks.module.user.application.mapper.UserMapper;
import br.com.edu.ifce.maracanau.carekobooks.shared.module.application.mapper.BaseUpdateMapper;
import org.mapstruct.*;

@Mapper(componentModel = "spring", uses = {UserMapper.class, ForumMapper.class})
public interface ForumReplyMapper extends BaseUpdateMapper<ForumReply, ForumReplyRequest> {

    @Mapping(source = "userId", target = "user", qualifiedByName = "toUserModelFromId")
    @Mapping(source = "forumId", target = "forum", qualifiedByName = "toForumModelFromId")
    ForumReply toModel(ForumReplyRequest request);
    ForumReplyDTO toDTO(ForumReply forumReply);

}
