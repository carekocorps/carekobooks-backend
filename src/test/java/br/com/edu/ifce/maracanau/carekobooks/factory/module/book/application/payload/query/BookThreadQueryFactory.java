package br.com.edu.ifce.maracanau.carekobooks.factory.module.book.application.payload.query;

import br.com.edu.ifce.maracanau.carekobooks.module.book.infrastructure.domain.entity.BookThread;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;

public class BookThreadQueryFactory {

    private BookThreadQueryFactory() {
    }

    public static URI validURI(BookThread thread, String orderBy, boolean isAscendingOrder) {
        return UriComponentsBuilder
                .fromPath("/api/v1/books/threads")
//                .queryParam("title", thread.getTitle())
                .queryParam("username", thread.getUser().getUsername())
                .queryParam("bookId", thread.getBook().getId())
                .queryParam("createdBefore", thread.getCreatedAt().toLocalDate())
                .queryParam("createdAfter", thread.getCreatedAt().toLocalDate())
                .queryParam("orderBy", orderBy)
                .queryParam("isAscendingOrder", isAscendingOrder)
                .build()
                .toUri();
    }

}
