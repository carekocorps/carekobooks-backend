package br.com.edu.ifce.maracanau.carekobooks.module.book.application.representation.response;

import br.com.edu.ifce.maracanau.carekobooks.module.image.application.representation.response.ImageResponse;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
public class BookResponse {

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
    private ImageResponse image;
    private List<BookGenreResponse> genres;

}
