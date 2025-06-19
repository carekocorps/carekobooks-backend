package br.com.edu.ifce.maracanau.carekobooks.factory.module.book.api.controller.uri;

import br.com.edu.ifce.maracanau.carekobooks.module.book.infrastructure.domain.entity.BookActivity;
import org.springframework.web.util.UriComponentsBuilder;

public class BookActivityUriFactory {

    private BookActivityUriFactory() {
    }

    public static String validUri(Long activityId) {
        return UriComponentsBuilder
                .fromPath("/api/v1/books/activities")
                .pathSegment(String.valueOf(activityId))
                .build()
                .toUriString();
    }

    public static String validQueryUri(BookActivity activity, String orderBy, boolean isAscendingOrder) {
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
                .toUriString();
    }

    public static String validFollowingQueryUri(String username, BookActivity activity, String orderBy, boolean isAscendingOrder) {
        return UriComponentsBuilder
                .fromPath("/api/v1/books/activities/social/following")
                .queryParam("username", username)
                .queryParam("createdBefore", activity.getCreatedAt().toLocalDate())
                .queryParam("createdAfter", activity.getCreatedAt().toLocalDate())
                .queryParam("orderBy", orderBy)
                .queryParam("isAscendingOrder", isAscendingOrder)
                .build()
                .toUriString();
    }

}
