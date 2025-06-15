package br.com.edu.ifce.maracanau.carekobooks.factory.module.book.application.payload.request;

import br.com.edu.ifce.maracanau.carekobooks.factory.module.book.infrastructure.domain.entity.BookThreadFactory;
import br.com.edu.ifce.maracanau.carekobooks.module.book.application.payload.request.BookThreadRequest;
import br.com.edu.ifce.maracanau.carekobooks.module.book.infrastructure.domain.entity.BookThread;
import com.github.javafaker.Faker;

public class BookThreadRequestFactory {

    private static final Faker faker = new Faker();

    private BookThreadRequestFactory() {
    }

    public static BookThreadRequest validRequest(BookThread thread) {
        var request = new BookThreadRequest();
        request.setTitle(thread.getTitle());
        request.setDescription(thread.getDescription());
        request.setUsername(thread.getUser().getUsername());
        request.setBookId(thread.getBook().getId());
        return request;
    }

    public static BookThreadRequest validRequest() {
        var thread = BookThreadFactory.validThread();
        return validRequest(thread);
    }

    public static BookThreadRequest invalidRequestByBlankTitle() {
        var request = validRequest();
        request.setTitle(null);
        return request;
    }

    public static BookThreadRequest invalidRequestByTitleExceedingMaxLength() {
        var request = validRequest();
        request.setTitle(faker.lorem().characters(256));
        return request;
    }

    public static BookThreadRequest invalidRequestByBlankDescription() {
        var request = validRequest();
        request.setDescription(null);
        return request;
    }

    public static BookThreadRequest invalidRequestByDescriptionExceedingMaxLength() {
        var request = validRequest();
        request.setDescription(faker.lorem().characters(1001));
        return request;
    }

    public static BookThreadRequest invalidRequestByBlankUsername() {
        var request = validRequest();
        request.setUsername(null);
        return request;
    }

    public static BookThreadRequest invalidRequestByUsernameExceedingMaxLength() {
        var request = validRequest();
        request.setUsername(faker.lorem().characters(51));
        return request;
    }

    public static BookThreadRequest invalidRequestByUsernameRegexMismatch() {
        var request = validRequest();
        request.setUsername("example@name");
        return request;
    }

    public static BookThreadRequest invalidRequestByBlankBookId() {
        var request = validRequest();
        request.setBookId(null);
        return request;
    }

}
