package br.com.edu.ifce.maracanau.carekobooks.dto.forumreply;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Schema(name = "ForumReply")
public class ForumReplyDTO {

    private Long id;
    private String content;

}
