package br.com.edu.ifce.maracanau.carekobooks.module.forum.application.dto;

import br.com.edu.ifce.maracanau.carekobooks.module.book.application.dto.BookDTO;
import br.com.edu.ifce.maracanau.carekobooks.module.user.application.dto.UserDTO;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@Schema(name = "ForumReply")
public class ForumReplyDTO {

    private Long id;
    private String content;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private UserDTO user;
    private BookDTO book;

}
