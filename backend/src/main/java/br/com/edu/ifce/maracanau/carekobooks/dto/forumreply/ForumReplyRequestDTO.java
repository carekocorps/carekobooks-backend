package br.com.edu.ifce.maracanau.carekobooks.dto.forumreply;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Schema(name = "ForumReplyRequest")
public class ForumReplyRequestDTO {

    @NotBlank
    @Size(max = 1000)
    private String content;

    

}
