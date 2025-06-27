package br.com.edu.ifce.maracanau.carekobooks.common.factory.module.book.application.payload.request;

import br.com.edu.ifce.maracanau.carekobooks.common.factory.module.book.infrastructure.domain.entity.BookFactory;
import br.com.edu.ifce.maracanau.carekobooks.module.book.application.payload.request.BookRequest;
import br.com.edu.ifce.maracanau.carekobooks.module.book.application.payload.request.BookUpdateRequest;
import br.com.edu.ifce.maracanau.carekobooks.module.book.infrastructure.domain.entity.Book;
import br.com.edu.ifce.maracanau.carekobooks.module.book.infrastructure.domain.entity.BookGenre;
import net.datafaker.Faker;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class BookUpdateRequestFactory {

    private static final Faker faker = new Faker();

    private BookUpdateRequestFactory() {
    }

    public static BookUpdateRequest validRequest(BookRequest request, Boolean retainCurrentImage) {
        var updateRequest = new BookUpdateRequest();
        updateRequest.setRetainCurrentImage(retainCurrentImage);
        updateRequest.setTitle(request.getTitle());
        updateRequest.setSynopsis(request.getSynopsis());
        updateRequest.setAuthorName(request.getAuthorName());
        updateRequest.setPublisherName(request.getPublisherName());
        updateRequest.setPublishedAt(request.getPublishedAt());
        updateRequest.setPageCount(request.getPageCount());
        updateRequest.setGenres(request.getGenres());
        return updateRequest;
    }

    public static BookUpdateRequest validRequest(Book book, Boolean retainCurrentImage) {
        var request = new BookUpdateRequest();
        request.setRetainCurrentImage(retainCurrentImage);
        request.setTitle(book.getTitle());
        request.setSynopsis(book.getSynopsis());
        request.setAuthorName(book.getAuthorName());
        request.setPublisherName(book.getPublisherName());
        request.setPublishedAt(book.getPublishedAt());
        request.setPageCount(book.getPageCount());
        request.setGenres(book.getGenres().stream().map(BookGenre::getName).toList());
        return request;
    }

    public static BookUpdateRequest validRequest(List<BookGenre> genres, Boolean retainCurrentImage) {
        var book = BookFactory.validBookWithNullId(genres);
        return validRequest(book, retainCurrentImage);
    }

    public static BookUpdateRequest validRequest(Boolean retainCurrentImage) {
        return validRequest(BookRequestFactory.validRequest(), retainCurrentImage);
    }

    public static BookUpdateRequest validRequest() {
        return validRequest(faker.bool().bool());
    }

    public static BookUpdateRequest invalidRequestByRepeatingGenres() {
        var request = validRequest();
        var genres = new ArrayList<>(request.getGenres());
        genres.add(request.getGenres().getFirst());
        request.setGenres(genres);
        return request;
    }

    public static BookUpdateRequest invalidRequestByBlankTitle() {
        var request = validRequest();
        request.setTitle(null);
        return request;
    }

    public static BookUpdateRequest invalidRequestByTitleExceedingMaxLength() {
        var request = validRequest();
        request.setTitle(faker.lorem().characters(256));
        return request;
    }

    public static BookUpdateRequest invalidRequestBySynopsisExceedingMaxLength() {
        var request = validRequest();
        request.setSynopsis(faker.lorem().characters(1001));
        return request;
    }

    public static BookUpdateRequest invalidRequestByBlankAuthorName() {
        var request = validRequest();
        request.setAuthorName(null);
        return request;
    }

    public static BookUpdateRequest invalidRequestByAuthorNameExceedingMaxLength() {
        var request = validRequest();
        request.setAuthorName(faker.lorem().characters(256));
        return request;
    }

    public static BookUpdateRequest invalidRequestByBlankPublisherName() {
        var request = validRequest();
        request.setPublisherName(null);
        return request;
    }

    public static BookUpdateRequest invalidRequestByPublisherNameExceedingMaxLength() {
        var request = validRequest();
        request.setPublisherName(faker.lorem().characters(256));
        return request;
    }

    public static BookUpdateRequest invalidRequestByInvalidPublishedAt() {
        var request = validRequest();
        request.setPublishedAt(LocalDate.now().plusYears(1));
        return request;
    }

    public static BookUpdateRequest invalidRequestByNullPageCount() {
        var request = validRequest();
        request.setPageCount(null);
        return request;
    }

    public static BookUpdateRequest invalidRequestByNegativePageCount() {
        var request = validRequest();
        request.setPageCount(-10);
        return request;
    }

    public static BookUpdateRequest invalidRequestByNullRetainCurrentImage() {
        var request = validRequest();
        request.setRetainCurrentImage(null);
        return request;
    }

    public static BookUpdateRequest invalidRequestByBlankGenre() {
        var request = validRequest();
        request.setGenres(List.of(""));
        return request;
    }

    public static BookUpdateRequest invalidRequestByGenreExceedingMaxLength() {
        var request = validRequest();
        var genres = new ArrayList<>(request.getGenres());
        genres.set(0, faker.lorem().characters(51));
        request.setGenres(genres);
        return request;
    }

    public static BookUpdateRequest invalidRequestByTooManyGenres() {
        var request = validRequest();
        request.setGenres(List.of("romance", "drama", "horror", "adventure", "fiction", "thriller"));
        return request;
    }

}
