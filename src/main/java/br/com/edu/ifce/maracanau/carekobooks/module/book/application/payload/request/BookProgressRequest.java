package br.com.edu.ifce.maracanau.carekobooks.module.book.application.payload.request;

import br.com.edu.ifce.maracanau.carekobooks.module.user.application.payload.request.annotation.Username;
import br.com.edu.ifce.maracanau.carekobooks.module.book.infrastructure.model.enums.BookProgressStatus;
import br.com.edu.ifce.maracanau.carekobooks.common.layer.application.payload.request.BaseRequest;
import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BookProgressRequest implements BaseRequest {

    @NotNull
    private BookProgressStatus status;

    @NotNull
    private Boolean isFavorite;

    @Min(0)
    @Max(100)
    private Integer score;

    @Min(0)
    private Integer pageCount;

    @Username
    private String username;

    @NotNull
    private Long bookId;

}
