package br.com.edu.ifce.maracanau.carekobooks.factory.module.book.application.payload.query;

import br.com.edu.ifce.maracanau.carekobooks.module.book.infrastructure.domain.entity.BookGenre;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;

public class BookGenreQueryFactory {

    private BookGenreQueryFactory() {
    }

    public static URI validURI(BookGenre genre, String orderBy, boolean isAscendingOrder) {
        return UriComponentsBuilder
                .fromPath("/api/v1/books/genres")
//                .queryParam("name", genre.getName())
//                .queryParam("displayName", genre.getDisplayName())
                .queryParam("createdBefore", genre.getCreatedAt().toLocalDate())
                .queryParam("createdAfter", genre.getCreatedAt().toLocalDate())
                .queryParam("orderBy", orderBy)
                .queryParam("isAscendingOrder", isAscendingOrder)
                .build()
                .toUri();
    }

}
