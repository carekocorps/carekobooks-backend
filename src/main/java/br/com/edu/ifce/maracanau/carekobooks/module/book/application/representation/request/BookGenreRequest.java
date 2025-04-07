package br.com.edu.ifce.maracanau.carekobooks.module.book.application.representation.request;

import br.com.edu.ifce.maracanau.carekobooks.common.layer.application.representation.request.BaseRequest;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BookGenreRequest implements BaseRequest {

    @NotBlank
    @Size(max = 50)
    @Pattern(regexp = "^[a-z_0-9]+$", message = "Name should only contain letters and numbers")
    @Schema(example = "string")
    private String name;

    @Size(max = 255)
    @Schema(example = "string")
    private String description;

}
