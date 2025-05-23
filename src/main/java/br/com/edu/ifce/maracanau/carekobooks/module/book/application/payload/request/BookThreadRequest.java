package br.com.edu.ifce.maracanau.carekobooks.module.book.application.payload.request;

import br.com.edu.ifce.maracanau.carekobooks.common.layer.application.payload.request.BaseRequest;
import br.com.edu.ifce.maracanau.carekobooks.module.user.application.payload.request.constraints.Username;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BookThreadRequest implements BaseRequest {

    @NotBlank
    @Size(max = 255)
    private String title;

    @NotBlank
    @Size(max = 1000)
    private String description;

    @Username
    private String username;

    @NotNull
    private Long bookId;

}
