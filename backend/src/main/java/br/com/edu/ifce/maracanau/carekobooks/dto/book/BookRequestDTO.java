package br.com.edu.ifce.maracanau.carekobooks.dto.book;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@Schema(name = "BookRequest")
public class BookRequestDTO {

    @NotBlank
    @Size(max = 255)
    private String title;

    @NotBlank
    @Size(max = 1000)
    private String synopsis;

    @NotBlank
    @Size(max = 255)
    private String author;

    @NotBlank
    @Size(max = 255)
    private String publisher;

    @NotNull
    @PastOrPresent
    private LocalDate publishedAt;

    @NotNull
    @Min(1)
    private Integer totalPages;

}
