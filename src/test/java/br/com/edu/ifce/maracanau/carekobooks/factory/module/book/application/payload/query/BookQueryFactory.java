package br.com.edu.ifce.maracanau.carekobooks.factory.module.book.application.payload.query;

import br.com.edu.ifce.maracanau.carekobooks.module.book.infrastructure.domain.entity.Book;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;

public class BookQueryFactory {

    private BookQueryFactory() {
    }

    public static URI validURI(Book book, String orderBy, boolean isAscendingOrder) {
        return UriComponentsBuilder
                .fromPath("/api/v1/books")
//                .queryParam("title", book.getTitle())
//                .queryParam("authorName", book.getAuthorName())
//                .queryParam("publisherName", book.getPublisherName())
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
                .toUri();
    }

}
