package br.com.edu.ifce.maracanau.carekobooks.factory.book.application.payload.response;

import br.com.edu.ifce.maracanau.carekobooks.factory.book.infrastructure.domain.entity.BookGenreFactory;
import br.com.edu.ifce.maracanau.carekobooks.module.book.application.payload.response.BookGenreResponse;

public class BookGenreResponseFactory {

    private BookGenreResponseFactory() {
    }

    public static BookGenreResponse validResponse() {
        var genre = BookGenreFactory.validGenre();
        var response = new BookGenreResponse();
        response.setId(genre.getId());
        response.setName(genre.getName());
        response.setDisplayName(genre.getDisplayName());
        response.setDescription(genre.getDescription());
        response.setCreatedAt(genre.getCreatedAt());
        response.setUpdatedAt(genre.getUpdatedAt());
        return response;
    }

}
