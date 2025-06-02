package br.com.edu.ifce.maracanau.carekobooks.module.book.application.payload.request;

import br.com.edu.ifce.maracanau.carekobooks.module.book.infrastructure.domain.entity.enums.BookProgressStatus;
import br.com.edu.ifce.maracanau.carekobooks.common.layer.application.payload.request.BaseRequest;
import io.swagger.v3.oas.annotations.media.Schema;
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

    @NotBlank
    @Size(max = 50)
    @Pattern(regexp = "^(?!_+$)[a-z0-9_]+$", message = "Username should only contain letters and numbers")
    @Schema(example = "string")
    private String username;

    @NotNull
    private Long bookId;

}
