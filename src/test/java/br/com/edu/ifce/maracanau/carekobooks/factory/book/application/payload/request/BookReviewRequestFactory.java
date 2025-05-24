package br.com.edu.ifce.maracanau.carekobooks.factory.book.application.payload.request;

import br.com.edu.ifce.maracanau.carekobooks.module.book.application.payload.request.BookReviewRequest;

import java.util.Random;
import java.util.UUID;

public class BookReviewRequestFactory {

    private BookReviewRequestFactory() {
    }

    public static BookReviewRequest validRequest() {
        var request = new BookReviewRequest();
        request.setTitle(UUID.randomUUID().toString());
        request.setContent(UUID.randomUUID().toString());
        request.setScore(new Random().nextInt(100));
        request.setUsername(UUID.randomUUID().toString().replace("-", ""));
        request.setBookId(new Random().nextLong());
        return request;
    }

    public static BookReviewRequest invalidRequestByBlankTitle() {
        var request = validRequest();
        request.setTitle(null);
        return request;
    }

    public static BookReviewRequest invalidRequestByTitleExceedingMaxLength() {
        var request = validRequest();
        request.setTitle("a".repeat(256));
        return request;
    }

    public static BookReviewRequest invalidRequestByBlankContent() {
        var request = validRequest();
        request.setContent(null);
        return request;
    }

    public static BookReviewRequest invalidRequestByContentExceedingMaxLength() {
        var request = validRequest();
        request.setContent("a".repeat(5001));
        return request;
    }

    public static BookReviewRequest invalidRequestByNullScore() {
        var request = validRequest();
        request.setScore(null);
        return request;
    }

    public static BookReviewRequest invalidRequestByNegativeScore() {
        var request = validRequest();
        request.setScore(-1);
        return request;
    }

    public static BookReviewRequest invalidRequestByScoreExceedingMaxValue() {
        var request = validRequest();
        request.setScore(101);
        return request;
    }

    public static BookReviewRequest invalidRequestByBlankUsername() {
        var request = validRequest();
        request.setUsername(null);
        return request;
    }

    public static BookReviewRequest invalidRequestByUsernameExceedingMaxLength() {
        var request = validRequest();
        request.setUsername("a".repeat(51));
        return request;
    }

    public static BookReviewRequest invalidRequestByUsernameRegexMismatch() {
        var request = validRequest();
        request.setUsername("example-user");
        return request;
    }

    public static BookReviewRequest invalidRequestByBlankBookId() {
        var request = validRequest();
        request.setBookId(null);
        return request;
    }

}
