package br.com.edu.ifce.maracanau.carekobooks.factory.module.book.application.payload.response;

import br.com.edu.ifce.maracanau.carekobooks.factory.module.image.application.payload.response.ImageResponseFactory;
import br.com.edu.ifce.maracanau.carekobooks.module.book.application.payload.response.BookResponse;
import br.com.edu.ifce.maracanau.carekobooks.module.book.infrastructure.domain.entity.Book;

import java.util.Optional;

public class BookResponseFactory {

    private BookResponseFactory() {
    }

    public static BookResponse validResponse(Book book) {
        var response = new BookResponse();
        Optional.ofNullable(book.getImage()).ifPresent(x -> response.setImage(ImageResponseFactory.validResponse(x)));
        Optional.ofNullable(book.getGenres()).ifPresent(x -> response.setGenres(x.stream().map(BookGenreResponseFactory::validResponse).toList()));
        response.setId(book.getId());
        response.setTitle(book.getTitle());
        response.setSynopsis(book.getSynopsis());
        response.setAuthorName(book.getAuthorName());
        response.setPublisherName(book.getPublisherName());
        response.setPublishedAt(book.getPublishedAt());
        response.setPageCount(book.getPageCount());
        response.setUserAverageScore(book.getUserAverageScore());
        response.setReviewAverageScore(book.getReviewAverageScore());
        response.setCreatedAt(book.getCreatedAt());
        response.setUpdatedAt(book.getUpdatedAt());
        return response;
    }

}
