package br.com.edu.ifce.maracanau.carekobooks.module.book.application.payload.response.simplified;

import br.com.edu.ifce.maracanau.carekobooks.common.layer.application.payload.response.BaseResponse;
import br.com.edu.ifce.maracanau.carekobooks.module.book.application.payload.response.BookGenreResponse;
import br.com.edu.ifce.maracanau.carekobooks.module.image.application.payload.response.ImageResponse;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
public class SimplifiedBookResponse implements BaseResponse {

    private Long id;
    private String title;
    private String authorName;
    private String publisherName;
    private LocalDate publishedAt;
    private Integer pageCount;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private ImageResponse image;
    private List<BookGenreResponse> genres;

}
