package br.com.edu.ifce.maracanau.carekobooks.module.book.application.representation.request;

import br.com.edu.ifce.maracanau.carekobooks.module.book.infrastructure.model.enums.BookProgressStatus;
import br.com.edu.ifce.maracanau.carekobooks.shared.layer.application.request.BaseRequest;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
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
    private Integer pagesRead;

    @NotNull
    private Long bookId;

}
