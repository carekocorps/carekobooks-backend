package br.com.edu.ifce.maracanau.carekobooks.common.factory.module.book.application.payload.request;

import br.com.edu.ifce.maracanau.carekobooks.common.factory.module.book.infrastructure.domain.entity.BookThreadReplyFactory;
import br.com.edu.ifce.maracanau.carekobooks.module.book.application.payload.request.BookThreadReplyRequest;
import br.com.edu.ifce.maracanau.carekobooks.module.book.infrastructure.domain.entity.BookThread;
import br.com.edu.ifce.maracanau.carekobooks.module.book.infrastructure.domain.entity.BookThreadReply;
import br.com.edu.ifce.maracanau.carekobooks.module.user.infrastructure.domain.entity.User;
import com.github.javafaker.Faker;

public class BookThreadReplyRequestFactory {

    private static final Faker faker = new Faker();

    private BookThreadReplyRequestFactory() {
    }

    public static BookThreadReplyRequest validRequest(BookThreadReply reply) {
        var request = new BookThreadReplyRequest();
        request.setContent(reply.getContent());
        request.setUsername(reply.getUser().getUsername());
        request.setThreadId(reply.getThread().getId());
        return request;
    }

    public static BookThreadReplyRequest validRequest(BookThread thread, User user) {
        var reply = BookThreadReplyFactory.validReplyWithNullId(null, thread, user);
        return validRequest(reply);
    }

    public static BookThreadReplyRequest validRequest() {
        var reply = BookThreadReplyFactory.validReplyWithNullId();
        return validRequest(reply);
    }

    public static BookThreadReplyRequest invalidRequestByBlankContent() {
        var request = validRequest();
        request.setContent(null);
        return request;
    }

    public static BookThreadReplyRequest invalidRequestByContentExceedingMaxLength() {
        var request = validRequest();
        request.setContent(faker.lorem().characters(1001));
        return request;
    }

    public static BookThreadReplyRequest invalidRequestByBlankUsername() {
        var request = validRequest();
        request.setUsername(null);
        return request;
    }

    public static BookThreadReplyRequest invalidRequestByUsernameExceedingMaxLength() {
        var request = validRequest();
        request.setUsername(faker.lorem().characters(51));
        return request;
    }

    public static BookThreadReplyRequest invalidRequestByUsernameRegexMismatch() {
        var request = validRequest();
        request.setUsername("example@name");
        return request;
    }

    public static BookThreadReplyRequest invalidRequestByBlankThreadId() {
        var request = validRequest();
        request.setThreadId(null);
        return request;
    }

}
