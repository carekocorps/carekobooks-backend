package br.com.edu.ifce.maracanau.carekobooks.factory.module.book.application.payload.request;

import br.com.edu.ifce.maracanau.carekobooks.factory.module.book.infrastructure.domain.entity.BookFactory;
import br.com.edu.ifce.maracanau.carekobooks.module.book.application.payload.request.BookRequest;
import br.com.edu.ifce.maracanau.carekobooks.module.book.infrastructure.domain.entity.Book;
import br.com.edu.ifce.maracanau.carekobooks.module.book.infrastructure.domain.entity.BookGenre;
import com.github.javafaker.Faker;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class BookRequestFactory {

    private static final Faker faker = new Faker();

    private BookRequestFactory() {
    }

    public static BookRequest validRequest(Book book) {
        var request = new BookRequest();
        request.setTitle(book.getTitle());
        request.setSynopsis(book.getSynopsis());
        request.setAuthorName(book.getAuthorName());
        request.setPublisherName(book.getPublisherName());
        request.setPublishedAt(book.getPublishedAt());
        request.setPageCount(book.getPageCount());
        request.setGenres(book.getGenres().stream().map(BookGenre::getName).toList());
        return request;
    }

    public static BookRequest validRequest(List<BookGenre> genres) {
        var book = BookFactory.validBookWithNullId(genres);
        return validRequest(book);
    }

    public static BookRequest validRequest() {
        var book = BookFactory.validBookWithNullId();
        return validRequest(book);
    }

    public static BookRequest validRequestWithEmptyGenres() {
        var book = BookFactory.validBookWithNullIdAndEmptyGenres();
        return validRequest(book);
    }

    public static BookRequest invalidRequestByRepeatingGenres() {
        var request = validRequest();
        var genres = new ArrayList<>(request.getGenres());
        genres.add(request.getGenres().getFirst());
        request.setGenres(genres);
        return request;
    }

    public static BookRequest invalidRequestByBlankTitle() {
        var request = validRequest();
        request.setTitle(null);
        return request;
    }

    public static BookRequest invalidRequestByTitleExceedingMaxLength() {
        var request = validRequest();
        request.setTitle(faker.lorem().characters(256));
        return request;
    }

    public static BookRequest invalidRequestBySynopsisExceedingMaxLength() {
        var request = validRequest();
        request.setSynopsis(faker.lorem().characters(1001));
        return request;
    }

    public static BookRequest invalidRequestByBlankAuthorName() {
        var request = validRequest();
        request.setAuthorName(null);
        return request;
    }

    public static BookRequest invalidRequestByAuthorNameExceedingMaxLength() {
        var request = validRequest();
        request.setAuthorName(faker.lorem().characters(256));
        return request;
    }

    public static BookRequest invalidRequestByBlankPublisherName() {
        var request = validRequest();
        request.setPublisherName(null);
        return request;
    }

    public static BookRequest invalidRequestByPublisherNameExceedingMaxLength() {
        var request = validRequest();
        request.setPublisherName(faker.lorem().characters(256));
        return request;
    }

    public static BookRequest invalidRequestByInvalidPublishedAt() {
        var request = validRequest();
        request.setPublishedAt(LocalDate.now().plusYears(1));
        return request;
    }

    public static BookRequest invalidRequestByNullPageCount() {
        var request = validRequest();
        request.setPageCount(null);
        return request;
    }

    public static BookRequest invalidRequestByNegativePageCount() {
        var request = validRequest();
        request.setPageCount(-10);
        return request;
    }

    public static BookRequest invalidRequestByBlankGenre() {
        var request = validRequest();
        request.setGenres(List.of(""));
        return request;
    }

    public static BookRequest invalidRequestByGenreExceedingMaxLength() {
        var request = validRequest();
        var genres = new ArrayList<>(request.getGenres());
        genres.set(0, faker.lorem().characters(51));
        request.setGenres(genres);
        return request;
    }

    public static BookRequest invalidRequestByTooManyGenres() {
        var request = validRequest();
        request.setGenres(List.of("romance", "drama", "horror", "adventure", "fiction", "thriller"));
        return request;
    }

}
