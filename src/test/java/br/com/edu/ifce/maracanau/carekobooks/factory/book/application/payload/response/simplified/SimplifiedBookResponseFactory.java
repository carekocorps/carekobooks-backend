package br.com.edu.ifce.maracanau.carekobooks.factory.book.application.payload.response.simplified;

import br.com.edu.ifce.maracanau.carekobooks.factory.book.application.payload.response.BookGenreResponseFactory;
import br.com.edu.ifce.maracanau.carekobooks.factory.image.application.payload.response.ImageResponseFactory;
import br.com.edu.ifce.maracanau.carekobooks.module.book.application.payload.response.simplified.SimplifiedBookResponse;
import br.com.edu.ifce.maracanau.carekobooks.module.book.infrastructure.domain.entity.Book;

import java.util.Optional;

public class SimplifiedBookResponseFactory {

    private SimplifiedBookResponseFactory() {
    }

    public static SimplifiedBookResponse validResponse(Book book) {
        var response = new SimplifiedBookResponse();
        Optional.ofNullable(book.getImage()).ifPresent(x -> response.setImage(ImageResponseFactory.validResponse(x)));
        Optional.ofNullable(book.getGenres()).ifPresent(x -> response.setGenres(x.stream().map(BookGenreResponseFactory::validResponse).toList()));

        response.setId(book.getId());
        response.setTitle(book.getTitle());
        response.setAuthorName(book.getAuthorName());
        response.setPublisherName(book.getPublisherName());
        response.setPublishedAt(book.getPublishedAt());
        response.setPageCount(book.getPageCount());
        response.setCreatedAt(book.getCreatedAt());
        response.setUpdatedAt(book.getUpdatedAt());
        return response;
    }

}
