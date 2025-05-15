package br.com.edu.ifce.maracanau.carekobooks.module.book.application.payload.request;

import br.com.edu.ifce.maracanau.carekobooks.common.layer.application.payload.request.BaseRequest;
import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.List;

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
    private String authorName;

    @NotBlank
    @Size(max = 255)
    private String publisherName;

    @PastOrPresent
    private LocalDate publishedAt;

    @NotNull
    @Min(1)
    private Integer pageCount;

    @Size(max = 5)
    private List<@NotBlank @Size(max = 50) String> genres;

}
