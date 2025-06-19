package br.com.edu.ifce.maracanau.carekobooks.factory.module.book.api.controller.uri;

import br.com.edu.ifce.maracanau.carekobooks.module.book.infrastructure.domain.entity.BookThread;
import org.springframework.web.util.UriComponentsBuilder;

public class BookThreadUriFactory {

    private BookThreadUriFactory() {
    }

    public static String validUri(Long threadId) {
        return UriComponentsBuilder
                .fromPath("/api/v1/books/threads")
                .pathSegment(String.valueOf(threadId))
                .build()
                .toUriString();
    }

    public static String validQueryUri(BookThread thread, String orderBy, boolean isAscendingOrder) {
        return UriComponentsBuilder
                .fromPath("/api/v1/books/threads")
                .queryParam("title", thread.getTitle())
                .queryParam("username", thread.getUser().getUsername())
                .queryParam("bookId", thread.getBook().getId())
                .queryParam("createdBefore", thread.getCreatedAt().toLocalDate())
                .queryParam("createdAfter", thread.getCreatedAt().toLocalDate())
                .queryParam("orderBy", orderBy)
                .queryParam("isAscendingOrder", isAscendingOrder)
                .build()
                .toUriString();
    }

}
