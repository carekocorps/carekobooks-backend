package br.com.edu.ifce.maracanau.carekobooks.factory.module.book.application.payload.query;

import br.com.edu.ifce.maracanau.carekobooks.module.book.infrastructure.domain.entity.BookThreadReply;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;

public class BookThreadReplyQueryFactory {

    private BookThreadReplyQueryFactory() {
    }

    public static URI validURI(BookThreadReply reply, String orderBy, boolean isAscendingOrder) {
        return UriComponentsBuilder
                .fromPath("/api/v1/books/threads/replies")
                .queryParam("parentId", reply.getParent().getId())
                .queryParam("username", reply.getUser().getUsername())
                .queryParam("bookId", reply.getThread().getBook().getId())
                .queryParam("createdBefore", reply.getCreatedAt().toLocalDate())
                .queryParam("createdAfter", reply.getCreatedAt().toLocalDate())
                .queryParam("orderBy", orderBy)
                .queryParam("isAscendingOrder", isAscendingOrder)
                .build()
                .toUri();
    }

}
