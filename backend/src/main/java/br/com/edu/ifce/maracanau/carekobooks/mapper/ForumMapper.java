package br.com.edu.ifce.maracanau.carekobooks.mapper;

import br.com.edu.ifce.maracanau.carekobooks.dto.forum.ForumDTO;
import br.com.edu.ifce.maracanau.carekobooks.dto.forum.ForumPageQueryDTO;
import br.com.edu.ifce.maracanau.carekobooks.model.Forum;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ForumMapper extends BaseMapper<Forum, ForumPageQueryDTO> {

    Forum toEntity(ForumPageQueryDTO forumPageQueryDTO);

    ForumDTO toDTO(Forum forum);

}
