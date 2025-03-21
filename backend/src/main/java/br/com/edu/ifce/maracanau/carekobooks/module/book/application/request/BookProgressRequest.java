package br.com.edu.ifce.maracanau.carekobooks.module.book.application.request;

import br.com.edu.ifce.maracanau.carekobooks.module.book.infrastructure.model.enums.BookProgressStatus;
import br.com.edu.ifce.maracanau.carekobooks.shared.application.request.BaseRequest;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BookProgressRequest implements BaseRequest {

    @NotNull
    private BookProgressStatus status;

    @Min(0)
    private Integer score;

    @Min(0)
    private Integer pagesRead;

    @NotNull
    private Long userId;

    @NotNull
    private Long bookId;

}
