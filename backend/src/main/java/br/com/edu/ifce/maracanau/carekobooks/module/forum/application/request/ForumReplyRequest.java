package br.com.edu.ifce.maracanau.carekobooks.module.forum.application.request;

import br.com.edu.ifce.maracanau.carekobooks.shared.module.application.request.BaseRequest;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ForumReplyRequest implements BaseRequest {

    @NotBlank
    @Size(max = 1000)
    private String content;

    @NotNull
    private Long userId;

    @NotNull
    private Long forumId;

}
