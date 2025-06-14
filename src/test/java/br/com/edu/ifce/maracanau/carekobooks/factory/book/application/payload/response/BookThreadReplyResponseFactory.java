package br.com.edu.ifce.maracanau.carekobooks.factory.book.application.payload.response;

import br.com.edu.ifce.maracanau.carekobooks.factory.user.payload.response.simplified.SimplifiedUserResponseFactory;
import br.com.edu.ifce.maracanau.carekobooks.module.book.application.payload.response.BookThreadReplyResponse;
import br.com.edu.ifce.maracanau.carekobooks.module.book.infrastructure.domain.entity.BookThreadReply;

public class BookThreadReplyResponseFactory {

    private BookThreadReplyResponseFactory() {
    }

    public static BookThreadReplyResponse validResponse(BookThreadReply reply) {
        var response = new BookThreadReplyResponse();
        response.setId(reply.getId());
        response.setContent(reply.getContent());
        response.setIsContainingChildren(false);
        response.setCreatedAt(reply.getCreatedAt());
        response.setUpdatedAt(reply.getUpdatedAt());
        response.setUser(SimplifiedUserResponseFactory.validResponse(reply.getUser()));
        response.setThread(BookThreadResponseFactory.validResponse(reply.getThread()));
        return response;
    }

}
