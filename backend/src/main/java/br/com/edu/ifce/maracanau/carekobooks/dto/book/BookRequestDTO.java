package br.com.edu.ifce.maracanau.carekobooks.dto.book;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
public class BookRequestDTO {

    private String title;
    private String synopsis;
    private String author;
    private String publisher;
    private LocalDate publishedAt;
    private Integer totalPages;
    private Integer scoreSum;
    private Integer scoreCount;

}
