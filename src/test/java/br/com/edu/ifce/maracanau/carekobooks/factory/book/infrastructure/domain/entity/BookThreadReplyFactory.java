package br.com.edu.ifce.maracanau.carekobooks.factory.book.infrastructure.domain.entity;

import br.com.edu.ifce.maracanau.carekobooks.factory.user.infrastructure.domain.entity.UserFactory;
import br.com.edu.ifce.maracanau.carekobooks.module.book.infrastructure.domain.entity.BookThreadReply;

import java.util.Random;
import java.util.UUID;

public class BookThreadReplyFactory {

    private BookThreadReplyFactory() {
    }

    public static BookThreadReply validReply() {
        var random = new Random();
        var reply = new BookThreadReply();
        reply.setId(random.nextLong());
        reply.setContent(UUID.randomUUID().toString());
        reply.setUser(UserFactory.validUser());
        reply.setThread(BookThreadFactory.validThread());
        reply.setParent(null);
        reply.setChildren(null);
        return reply;
    }

    public static BookThreadReply invalidReviewByEmptyUser() {
        var reply = validReply();
        reply.setUser(null);
        return reply;
    }

    public static BookThreadReply invalidReviewByEmptyThread() {
        var reply = validReply();
        reply.setThread(null);
        return reply;
    }

}
