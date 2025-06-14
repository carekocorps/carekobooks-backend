package br.com.edu.ifce.maracanau.carekobooks.factory.book.application.payload.query;

import br.com.edu.ifce.maracanau.carekobooks.module.book.infrastructure.domain.entity.BookActivity;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;

public class BookActivityQueryFactory {

    private BookActivityQueryFactory() {
    }

    public static URI validURI(BookActivity activity) {
        return UriComponentsBuilder
                .fromPath("/api/v1/books/activities")
                .queryParam("username", activity.getUser().getUsername())
                .queryParam("genre", activity.getBook().getGenres().getFirst().getName())
                .queryParam("status", activity.getStatus())
                .queryParam("pageCount", activity.getPageCount())
                .queryParam("bookId", activity.getBook().getId())
                .build()
                .toUri();
    }

}
