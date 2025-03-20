package br.com.edu.ifce.maracanau.carekobooks.module.forum.application.mapper;

import br.com.edu.ifce.maracanau.carekobooks.module.forum.application.dto.ForumDTO;
import br.com.edu.ifce.maracanau.carekobooks.module.forum.application.dto.ForumRequestDTO;
import br.com.edu.ifce.maracanau.carekobooks.shared.application.mapper.BaseMapper;
import br.com.edu.ifce.maracanau.carekobooks.module.forum.infra.model.Forum;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ForumMapper extends BaseMapper<Forum, ForumRequestDTO> {

    Forum toEntity(ForumRequestDTO forumRequestDTO);

    ForumDTO toDTO(Forum forum);

}
