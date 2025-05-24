package br.com.edu.ifce.maracanau.carekobooks.factory.book.application.payload.request;

import br.com.edu.ifce.maracanau.carekobooks.module.book.application.payload.request.BookThreadRequest;
import java.util.Random;
import java.util.UUID;

public class BookThreadRequestFactory {

    private BookThreadRequestFactory() {
    }

    public static BookThreadRequest validRequest() {
        var request = new BookThreadRequest();
        request.setTitle(UUID.randomUUID().toString());
        request.setDescription(UUID.randomUUID().toString());
        request.setUsername(UUID.randomUUID().toString().replace("-", ""));
        request.setBookId(new Random().nextLong());
        return request;
    }

    public static BookThreadRequest invalidRequestByBlankTitle() {
        var request = validRequest();
        request.setTitle(null);
        return request;
    }

    public static BookThreadRequest invalidRequestByTitleExceedingMaxLength() {
        var request = validRequest();
        request.setTitle("a".repeat(256));
        return request;
    }

    public static BookThreadRequest invalidRequestByBlankDescription() {
        var request = validRequest();
        request.setDescription(null);
        return request;
    }

    public static BookThreadRequest invalidRequestByDescriptionExceedingMaxLength() {
        var request = validRequest();
        request.setDescription("a".repeat(1001));
        return request;
    }

    public static BookThreadRequest invalidRequestByBlankUsername() {
        var request = validRequest();
        request.setUsername(null);
        return request;
    }

    public static BookThreadRequest invalidRequestByUsernameExceedingMaxLength() {
        var request = validRequest();
        request.setUsername("a".repeat(51));
        return request;
    }

    public static BookThreadRequest invalidRequestByUsernameRegexMismatch() {
        var request = validRequest();
        request.setUsername("example-user");
        return request;
    }

    public static BookThreadRequest invalidRequestByBlankBookId() {
        var request = validRequest();
        request.setBookId(null);
        return request;
    }

}
