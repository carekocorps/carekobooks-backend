package br.com.edu.ifce.maracanau.carekobooks.common.factory.module.book.api.controller.uri;

import br.com.edu.ifce.maracanau.carekobooks.module.book.infrastructure.domain.entity.BookProgress;
import org.springframework.web.util.UriComponentsBuilder;

public class BookProgressUriFactory {

    private BookProgressUriFactory() {
    }

    public static String validUri() {
        return UriComponentsBuilder
                .fromPath("/api/v1/books/progresses")
                .build()
                .toUriString();
    }

    public static String validUri(Long progressId) {
        return UriComponentsBuilder
                .fromPath("/api/v1/books/progresses")
                .pathSegment(String.valueOf(progressId))
                .build()
                .toUriString();
    }

    public static String validFavoritesUri(Long progressId) {
        return UriComponentsBuilder
                .fromPath("/api/v1/books/progresses")
                .pathSegment(String.valueOf(progressId), "favorites")
                .build()
                .toUriString();
    }

    public static String validQueryUri(BookProgress progress, String orderBy, boolean isAscendingOrder) {
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
                .toUriString();
    }

}
