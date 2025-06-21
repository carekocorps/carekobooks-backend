package br.com.edu.ifce.maracanau.carekobooks.factory.module.book.api.controller.uri;

import br.com.edu.ifce.maracanau.carekobooks.module.book.infrastructure.domain.entity.Book;
import org.springframework.web.util.UriComponentsBuilder;

public class BookUriFactory {

    private BookUriFactory() {
    }

    public static String validUri() {
        return UriComponentsBuilder
                .fromPath("/api/v1/books")
                .build()
                .toUriString();
    }

    public static String validUri(Long bookId) {
        return UriComponentsBuilder
                .fromPath("/api/v1/books")
                .pathSegment(String.valueOf(bookId))
                .build()
                .toUriString();
    }

    public static String validGenreUri(Long bookId, String genreName) {
        return UriComponentsBuilder
                .fromPath("/api/v1/books")
                .pathSegment(String.valueOf(bookId), "genres", genreName)
                .build()
                .toUriString();
    }

    public static String validImageUri(Long bookId) {
        return UriComponentsBuilder
                .fromPath("/api/v1/books")
                .pathSegment(String.valueOf(bookId), "images")
                .build()
                .toUriString();
    }

    public static String validCacheUri() {
        return UriComponentsBuilder
                .fromPath("/api/v1/books/cache")
                .build()
                .toUriString();
    }

    public static String validQueryUri(Book book, String orderBy, boolean isAscendingOrder) {
        return UriComponentsBuilder
                .fromPath("/api/v1/books")
                .queryParam("title", book.getTitle())
                .queryParam("authorName", book.getAuthorName())
                .queryParam("publisherName", book.getPublisherName())
                .queryParam("genre", book.getGenres().getFirst().getName())
                .queryParam("publishedBefore", book.getPublishedAt())
                .queryParam("publishedAfter", book.getPublishedAt())
                .queryParam("pageCountLower", book.getPageCount())
                .queryParam("pageCountGreater", book.getPageCount())
                .queryParam("createdBefore", book.getCreatedAt().toLocalDate())
                .queryParam("createdAfter", book.getCreatedAt().toLocalDate())
                .queryParam("orderBy", orderBy)
                .queryParam("isAscendingOrder", isAscendingOrder)
                .build()
                .toUriString();
    }

}
