package br.com.edu.ifce.maracanau.carekobooks.module.book.application.payload.response;

import br.com.edu.ifce.maracanau.carekobooks.common.layer.application.payload.response.BaseResponse;
import br.com.edu.ifce.maracanau.carekobooks.module.image.application.payload.response.ImageResponse;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
public class BookResponse implements BaseResponse {

    private Long id;
    private String title;
    private String synopsis;
    private String authorName;
    private String publisherName;
    private LocalDate publishedAt;
    private Integer pageCount;
    private Double userAverageScore;
    private Double reviewAverageScore;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private ImageResponse image;
    private List<BookGenreResponse> genres;

}
