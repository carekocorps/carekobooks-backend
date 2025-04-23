package br.com.edu.ifce.maracanau.carekobooks.module.forum.application.mapper;

import br.com.edu.ifce.maracanau.carekobooks.common.layer.application.mapper.annotation.IgnoreBaseModelFields;
import br.com.edu.ifce.maracanau.carekobooks.module.forum.application.representation.response.ForumReplyResponse;
import br.com.edu.ifce.maracanau.carekobooks.module.forum.application.representation.request.ForumReplyRequest;
import br.com.edu.ifce.maracanau.carekobooks.module.forum.infrastructure.model.ForumReply;
import br.com.edu.ifce.maracanau.carekobooks.module.user.application.mapper.UserMapper;
import br.com.edu.ifce.maracanau.carekobooks.module.user.application.security.provider.UserContextProvider;
import org.mapstruct.*;

@Mapper(
        componentModel = "spring",
        imports = UserContextProvider.class,
        uses = {
                UserMapper.class,
                ForumMapper.class
        }
)
public interface ForumReplyMapper {

    @IgnoreBaseModelFields
    @Mapping(target = "user", expression = "java(UserContextProvider.getUser())")
    @Mapping(target = "forum", expression = "java(forumMapper.toModel(request.getForumId()))")
    ForumReply toModel(ForumReplyRequest request);
    ForumReplyResponse toResponse(ForumReply forumReply);

    @IgnoreBaseModelFields
    @Mapping(target = "user", ignore = true)
    @Mapping(target = "forum", expression = "java(forumMapper.toModel(request.getForumId()))")
    void updateModel(@MappingTarget ForumReply reply, ForumReplyRequest request);

}
