package br.com.edu.ifce.maracanau.carekobooks.factory.book.application.payload.response.simplified;

import br.com.edu.ifce.maracanau.carekobooks.module.book.application.payload.response.simplified.SimplifiedBookResponse;
import br.com.edu.ifce.maracanau.carekobooks.module.book.infrastructure.domain.entity.Book;

public class SimplifiedBookResponseFactory {

    private SimplifiedBookResponseFactory() {
    }

    public static SimplifiedBookResponse validResponse(Book book) {
        var response = new SimplifiedBookResponse();
        response.setId(book.getId());
        response.setTitle(book.getTitle());
        response.setAuthorName(book.getAuthorName());
        response.setPublisherName(book.getPublisherName());
        response.setPublishedAt(book.getPublishedAt());
        response.setPageCount(book.getPageCount());
        response.setImage(null);
        response.setGenres(null);
        response.setCreatedAt(book.getCreatedAt());
        response.setUpdatedAt(book.getUpdatedAt());
        return response;
    }

}
