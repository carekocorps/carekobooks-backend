package br.com.edu.ifce.maracanau.carekobooks.factory.book.application.payload.query;

import br.com.edu.ifce.maracanau.carekobooks.module.book.infrastructure.domain.entity.BookProgress;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;

public class BookProgressQueryFactory {

    private BookProgressQueryFactory() {
    }

    public static URI validURI(BookProgress progress, String orderBy, boolean isAscendingOrder) {
        return UriComponentsBuilder
                .fromPath("/api/v1/books/progresses")
                .queryParam("username", progress.getUser().getUsername())
                .queryParam("genre", progress.getBook().getGenres().getFirst().getName())
                .queryParam("status", progress.getStatus())
                .queryParam("isFavorite", progress.getIsFavorite())
                .queryParam("score", progress.getScore())
                .queryParam("pageCount", progress.getPageCount())
                .queryParam("bookId", progress.getBook().getId())
                .queryParam("createdBefore", progress.getCreatedAt().toLocalDate())
                .queryParam("createdAfter", progress.getCreatedAt().toLocalDate())
                .queryParam("orderBy", orderBy)
                .queryParam("isAscendingOrder", isAscendingOrder)
                .build()
                .toUri();
    }

}
