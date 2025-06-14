package br.com.edu.ifce.maracanau.carekobooks.factory.book.application.payload.request;

import br.com.edu.ifce.maracanau.carekobooks.factory.book.infrastructure.domain.entity.BookThreadReplyFactory;
import br.com.edu.ifce.maracanau.carekobooks.module.book.application.payload.request.BookThreadReplyRequest;
import br.com.edu.ifce.maracanau.carekobooks.module.book.infrastructure.domain.entity.BookThreadReply;

public class BookThreadReplyRequestFactory {

    private BookThreadReplyRequestFactory() {
    }

    public static BookThreadReplyRequest validRequest(BookThreadReply reply) {
        var request = new BookThreadReplyRequest();
        request.setContent(reply.getContent());
        request.setUsername(reply.getUser().getUsername());
        request.setThreadId(reply.getThread().getId());
        return request;
    }

    public static BookThreadReplyRequest validRequest() {
        var reply = BookThreadReplyFactory.validReply();
        return validRequest(reply);
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
