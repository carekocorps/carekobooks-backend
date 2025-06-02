package br.com.edu.ifce.maracanau.carekobooks.module.book.application.payload.request;

import br.com.edu.ifce.maracanau.carekobooks.common.layer.application.payload.request.BaseRequest;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BookReviewRequest implements BaseRequest {

    @NotBlank
    @Size(max = 255)
    private String title;

    @NotBlank
    @Size(max = 5000)
    private String content;

    @NotNull
    @Min(0)
    @Max(100)
    private Integer score;

    @NotBlank
    @Size(max = 50)
    @Pattern(regexp = "^[a-z0-9]+$", message = "Username should only contain letters and numbers")
    @Schema(example = "string")
    private String username;

    @NotNull
    private Long bookId;

}
