package br.com.edu.ifce.maracanau.carekobooks.factory.module.book.api.controller.uri;

import br.com.edu.ifce.maracanau.carekobooks.module.book.infrastructure.domain.entity.BookGenre;
import org.springframework.web.util.UriComponentsBuilder;

public class BookGenreUriFactory {

    private BookGenreUriFactory() {
    }

    public static String validUri() {
        return UriComponentsBuilder
                .fromPath("/api/v1/books/genres")
                .build()
                .toUriString();
    }

    public static String validUri(String genreName) {
        return UriComponentsBuilder
                .fromPath("/api/v1/books/genres")
                .pathSegment(genreName)
                .build()
                .toUriString();
    }

    public static String validQueryUri(BookGenre genre, String orderBy, boolean isAscendingOrder) {
        return UriComponentsBuilder
                .fromPath("/api/v1/books/genres")
                .queryParam("name", genre.getName())
                .queryParam("displayName", genre.getDisplayName())
                .queryParam("createdBefore", genre.getCreatedAt().toLocalDate())
                .queryParam("createdAfter", genre.getCreatedAt().toLocalDate())
                .queryParam("orderBy", orderBy)
                .queryParam("isAscendingOrder", isAscendingOrder)
                .build()
                .toUriString();
    }

}
