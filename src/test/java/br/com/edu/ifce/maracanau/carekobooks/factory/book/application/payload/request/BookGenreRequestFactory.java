package br.com.edu.ifce.maracanau.carekobooks.factory.book.application.payload.request;

import br.com.edu.ifce.maracanau.carekobooks.factory.book.infrastructure.domain.entity.BookGenreFactory;
import br.com.edu.ifce.maracanau.carekobooks.module.book.application.payload.request.BookGenreRequest;
import br.com.edu.ifce.maracanau.carekobooks.module.book.infrastructure.domain.entity.BookGenre;

public class BookGenreRequestFactory {

    private BookGenreRequestFactory() {
    }

    public static BookGenreRequest validRequest(BookGenre genre) {
        var request = new BookGenreRequest();
        request.setName(genre.getName());
        request.setDisplayName(genre.getDisplayName());
        request.setDescription(genre.getDescription());
        return request;
    }

    public static BookGenreRequest validRequest() {
        var genre = BookGenreFactory.validGenre();
        return validRequest(genre);
    }

    public static BookGenreRequest invalidRequestByBlankName() {
        var request = validRequest();
        request.setName("");
        return request;
    }

    public static BookGenreRequest invalidRequestByNameRegexMismatch() {
        var request = validRequest();
        request.setName("example-genre");
        return request;
    }

    public static BookGenreRequest invalidRequestByNameExceedingMaxLength() {
        var request = validRequest();
        request.setName("a".repeat(51));
        return request;
    }

    public static BookGenreRequest invalidRequestByBlankDisplayName() {
        var request = validRequest();
        request.setDisplayName("");
        return request;
    }

    public static BookGenreRequest invalidRequestByDisplayNameExceedingMaxLength() {
        var request = validRequest();
        request.setDisplayName("a".repeat(101));
        return request;
    }

    public static BookGenreRequest invalidRequestByDescriptionExceedingMaxLength() {
        var request = validRequest();
        request.setDescription("a".repeat(256));
        return request;
    }

}
