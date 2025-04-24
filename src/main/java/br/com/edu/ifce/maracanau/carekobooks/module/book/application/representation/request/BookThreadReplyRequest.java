package br.com.edu.ifce.maracanau.carekobooks.module.book.application.representation.request;

import br.com.edu.ifce.maracanau.carekobooks.common.layer.application.representation.request.BaseRequest;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BookThreadReplyRequest implements BaseRequest {

    @NotBlank
    @Size(max = 1000)
    private String content;

    @NotNull
    private Long threadId;

}
