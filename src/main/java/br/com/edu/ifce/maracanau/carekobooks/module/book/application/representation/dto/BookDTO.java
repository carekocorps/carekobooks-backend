package br.com.edu.ifce.maracanau.carekobooks.module.book.application.representation.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@Schema(name = "Book")
public class BookDTO {

    private Long id;
    private String title;
    private String synopsis;
    private String author;
    private String publisher;
    private LocalDate publishedAt;
    private Integer totalPages;
    private Double userAverageScore;
    private Double reviewAverageScore;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private List<BookGenreDTO> genres;

}
