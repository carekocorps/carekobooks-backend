package br.com.edu.ifce.maracanau.carekobooks.factory.book.application.payload.request;

import br.com.edu.ifce.maracanau.carekobooks.factory.book.infrastructure.domain.entity.BookProgressFactory;
import br.com.edu.ifce.maracanau.carekobooks.module.book.application.payload.request.BookProgressRequest;
import br.com.edu.ifce.maracanau.carekobooks.module.book.infrastructure.domain.entity.BookProgress;
import com.github.javafaker.Faker;

public class BookProgressRequestFactory {

    private static final Faker faker = new Faker();

    private BookProgressRequestFactory() {
    }

    public static BookProgressRequest validRequest(BookProgress progress) {
        var request = new BookProgressRequest();
        request.setStatus(progress.getStatus());
        request.setIsFavorite(progress.getIsFavorite());
        request.setScore(progress.getScore());
        request.setPageCount(progress.getPageCount());
        request.setUsername(progress.getUser().getUsername());
        request.setBookId(progress.getBook().getId());
        return request;
    }

    public static BookProgressRequest validRequest() {
        var progress = BookProgressFactory.validProgress();
        return validRequest(progress);
    }

    public static BookProgressRequest invalidRequestByBlankUsername() {
        var request = validRequest();
        request.setUsername(null);
        return request;
    }

    public static BookProgressRequest invalidRequestByUsernameExceedingMaxLength() {
        var request = validRequest();
        request.setUsername(faker.lorem().characters(51));
        return request;
    }

    public static BookProgressRequest invalidRequestByUsernameRegexMismatch() {
        var request = validRequest();
        request.setUsername("invalid@name");
        return request;
    }

    public static BookProgressRequest invalidRequestByBlankBookId() {
        var request = validRequest();
        request.setBookId(null);
        return request;
    }

    public static BookProgressRequest invalidRequestByScoreExceedingMax() {
        var request = validRequest();
        request.setScore(101);
        return request;
    }

    public static BookProgressRequest invalidRequestByNegativeScore() {
        var request = validRequest();
        request.setScore(-1);
        return request;
    }

    public static BookProgressRequest invalidRequestByNegativePageCount() {
        var request = validRequest();
        request.setPageCount(-10);
        return request;
    }

    public static BookProgressRequest invalidRequestByBlankStatus() {
        var request = validRequest();
        request.setStatus(null);
        return request;
    }

    public static BookProgressRequest invalidRequestByBlankIsFavorite() {
        var request = validRequest();
        request.setIsFavorite(null);
        return request;
    }

}
