package br.com.edu.ifce.maracanau.carekobooks.factory.module.book.api.controller.uri;

import br.com.edu.ifce.maracanau.carekobooks.module.book.infrastructure.domain.entity.BookThreadReply;
import org.springframework.web.util.UriComponentsBuilder;

public class BookThreadReplyUriFactory {

    private BookThreadReplyUriFactory() {
    }

    public static String validUri() {
        return UriComponentsBuilder
                .fromPath("/api/v1/books/threads/replies")
                .build()
                .toUriString();
    }

    public static String validUri(Long replyId) {
        return UriComponentsBuilder
                .fromPath("/api/v1/books/threads/replies")
                .pathSegment(String.valueOf(replyId))
                .build()
                .toUriString();
    }

    public static String validChildrenUri(Long replyId) {
        return UriComponentsBuilder
                .fromPath("/api/v1/books/threads/replies")
                .pathSegment(String.valueOf(replyId), "children")
                .build()
                .toUriString();
    }

    public static String validQueryUri(BookThreadReply reply, String orderBy, boolean isAscendingOrder) {
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
                .toUriString();
    }

}
