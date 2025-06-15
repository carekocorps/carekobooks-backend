package br.com.edu.ifce.maracanau.carekobooks.factory.module.book.application.payload.query;

import br.com.edu.ifce.maracanau.carekobooks.module.book.infrastructure.domain.entity.BookActivity;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;

public class BookActivityQueryFactory {

    private BookActivityQueryFactory() {
    }

    public static URI validURI(BookActivity activity, String orderBy, boolean isAscendingOrder) {
        return UriComponentsBuilder
                .fromPath("/api/v1/books/activities")
                .queryParam("username", activity.getUser().getUsername())
                .queryParam("genre", activity.getBook().getGenres().getFirst().getName())
                .queryParam("status", activity.getStatus())
                .queryParam("pageCount", activity.getPageCount())
                .queryParam("bookId", activity.getBook().getId())
                .queryParam("createdBefore", activity.getCreatedAt().toLocalDate())
                .queryParam("createdAfter", activity.getCreatedAt().toLocalDate())
                .queryParam("orderBy", orderBy)
                .queryParam("isAscendingOrder", isAscendingOrder)
                .build()
                .toUri();
    }

}
