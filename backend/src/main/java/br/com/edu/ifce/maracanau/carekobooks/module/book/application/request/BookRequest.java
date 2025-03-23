package br.com.edu.ifce.maracanau.carekobooks.module.book.application.request;

import br.com.edu.ifce.maracanau.carekobooks.shared.module.application.request.BaseRequest;
import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
public class BookRequest implements BaseRequest {

    @NotBlank
    @Size(max = 255)
    private String title;

    @Size(max = 1000)
    private String synopsis;

    @NotBlank
    @Size(max = 255)
    private String author;

    @NotBlank
    @Size(max = 255)
    private String publisher;

    @PastOrPresent
    private LocalDate publishedAt;

    @NotNull
    @Min(1)
    private Integer totalPages;

}
