package br.com.edu.ifce.maracanau.carekobooks.factory.module.book.application.payload.query;

import br.com.edu.ifce.maracanau.carekobooks.module.book.infrastructure.domain.entity.BookActivity;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;

public class BookActivityFollowingQueryFactory {

    private BookActivityFollowingQueryFactory() {
    }

    public static URI validURI(String username, BookActivity activity, String orderBy, boolean isAscendingOrder) {
        return UriComponentsBuilder
                .fromPath("/api/v1/books/activities/social/following")
                .queryParam("username", username)
                .queryParam("createdBefore", activity.getCreatedAt().toLocalDate())
                .queryParam("createdAfter", activity.getCreatedAt().toLocalDate())
                .queryParam("orderBy", orderBy)
                .queryParam("isAscendingOrder", isAscendingOrder)
                .build()
                .toUri();
    }

}
