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

    public static BookReviewRequest invalidRequestByEmptyTitle() {
        var request = validRequest();
        request.setTitle(null);
        return request;
    }

}
