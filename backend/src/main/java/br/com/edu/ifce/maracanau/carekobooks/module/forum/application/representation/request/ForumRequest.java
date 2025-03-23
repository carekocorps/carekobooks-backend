package br.com.edu.ifce.maracanau.carekobooks.module.forum.application.representation.request;

import br.com.edu.ifce.maracanau.carekobooks.shared.application.request.BaseRequest;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ForumRequest implements BaseRequest {

    @NotBlank
    @Size(max = 255)
    private String title;

    @NotBlank
    @Size(max = 1000)
    private String description;

    @NotNull
    private Long bookId;

}
