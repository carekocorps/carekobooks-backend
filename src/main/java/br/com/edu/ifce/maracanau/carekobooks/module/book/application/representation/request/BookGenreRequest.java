package br.com.edu.ifce.maracanau.carekobooks.module.book.application.representation.request;

import br.com.edu.ifce.maracanau.carekobooks.shared.application.request.BaseRequest;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BookGenreRequest implements BaseRequest {

    @NotBlank
    @Size(max = 50)
    private String name;

    @Size(max = 255)
    private String description;

}
