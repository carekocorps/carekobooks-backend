package br.com.edu.ifce.maracanau.carekobooks.factory.module.book.application.payload.request;

import br.com.edu.ifce.maracanau.carekobooks.factory.module.book.infrastructure.domain.entity.BookGenreFactory;
import br.com.edu.ifce.maracanau.carekobooks.module.book.application.payload.request.BookGenreRequest;
import br.com.edu.ifce.maracanau.carekobooks.module.book.infrastructure.domain.entity.BookGenre;
import com.github.javafaker.Faker;

public class BookGenreRequestFactory {

    private static final Faker faker = new Faker();

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
        var genre = BookGenreFactory.validGenreWithNullId();
        return validRequest(genre);
    }

    public static BookGenreRequest invalidRequestByBlankName() {
        var request = validRequest();
        request.setName(null);
        return request;
    }

    public static BookGenreRequest invalidRequestByNameRegexMismatch() {
        var request = validRequest();
        request.setName("invalid@name");
        return request;
    }

    public static BookGenreRequest invalidRequestByNameExceedingMaxLength() {
        var request = validRequest();
        request.setName(faker.lorem().characters(51));
        return request;
    }

    public static BookGenreRequest invalidRequestByBlankDisplayName() {
        var request = validRequest();
        request.setDisplayName(null);
        return request;
    }

    public static BookGenreRequest invalidRequestByDisplayNameExceedingMaxLength() {
        var request = validRequest();
        request.setDisplayName(faker.lorem().characters(101));
        return request;
    }

    public static BookGenreRequest invalidRequestByDescriptionExceedingMaxLength() {
        var request = validRequest();
        request.setDescription(faker.lorem().characters(256));
        return request;
    }

}
