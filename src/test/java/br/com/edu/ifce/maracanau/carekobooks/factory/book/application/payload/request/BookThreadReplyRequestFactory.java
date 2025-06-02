package br.com.edu.ifce.maracanau.carekobooks.factory.book.application.payload.request;

import br.com.edu.ifce.maracanau.carekobooks.module.book.application.payload.request.BookThreadReplyRequest;

import java.util.Random;
import java.util.UUID;

public class BookThreadReplyRequestFactory {

    private BookThreadReplyRequestFactory() {
    }

    public static BookThreadReplyRequest validRequest() {
        var request = new BookThreadReplyRequest();
        request.setContent(UUID.randomUUID().toString());
        request.setUsername(UUID.randomUUID().toString().replace("-", ""));
        request.setThreadId(new Random().nextLong());
        return request;
    }

    public static BookThreadReplyRequest invalidRequestByBlankContent() {
        var request = validRequest();
        request.setContent(null);
        return request;
    }

    public static BookThreadReplyRequest invalidRequestByContentExceedingMaxLength() {
        var request = validRequest();
        request.setContent("a".repeat(1001));
        return request;
    }

    public static BookThreadReplyRequest invalidRequestByBlankUsername() {
        var request = validRequest();
        request.setUsername(null);
        return request;
    }

    public static BookThreadReplyRequest invalidRequestByUsernameExceedingMaxLength() {
        var request = validRequest();
        request.setUsername("a".repeat(51));
        return request;
    }

    public static BookThreadReplyRequest invalidRequestByUsernameRegexMismatch() {
        var request = validRequest();
        request.setUsername("example-user");
        return request;
    }

    public static BookThreadReplyRequest invalidRequestByBlankThreadId() {
        var request = validRequest();
        request.setThreadId(null);
        return request;
    }

}
