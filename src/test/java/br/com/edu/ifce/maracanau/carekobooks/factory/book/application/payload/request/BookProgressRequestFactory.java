package br.com.edu.ifce.maracanau.carekobooks.factory.book.application.payload.request;

import br.com.edu.ifce.maracanau.carekobooks.module.book.application.payload.request.BookProgressRequest;
import br.com.edu.ifce.maracanau.carekobooks.module.book.infrastructure.domain.entity.enums.BookProgressStatus;

import java.util.Random;
import java.util.UUID;

public class BookProgressRequestFactory {

    private BookProgressRequestFactory() {
    }

    public static BookProgressRequest validRequest() {
        var random = new Random();
        var request = new BookProgressRequest();
        request.setStatus(BookProgressStatus.READING);
        request.setIsFavorite(random.nextBoolean());
        request.setScore(random.nextInt(101));
        request.setPageCount(random.nextInt(1000));
        request.setUsername(UUID.randomUUID().toString().replace("-", "").substring(0, 10));
        request.setBookId(random.nextLong());
        return request;
    }

    public static BookProgressRequest invalidRequestByBlankUsername() {
        var request = validRequest();
        request.setUsername(null);
        return request;
    }

    public static BookProgressRequest invalidRequestByUsernameExceedingMaxLength() {
        var request = validRequest();
        request.setUsername("a".repeat(51));
        return request;
    }

    public static BookProgressRequest invalidRequestByUsernameRegexMismatch() {
        var request = validRequest();
        request.setUsername("invalid-username");
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
