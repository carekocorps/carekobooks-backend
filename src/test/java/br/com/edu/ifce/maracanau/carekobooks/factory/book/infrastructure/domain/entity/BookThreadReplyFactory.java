package br.com.edu.ifce.maracanau.carekobooks.factory.book.infrastructure.domain.entity;

import br.com.edu.ifce.maracanau.carekobooks.factory.user.infrastructure.domain.entity.UserFactory;
import br.com.edu.ifce.maracanau.carekobooks.module.book.application.payload.request.BookThreadReplyRequest;
import br.com.edu.ifce.maracanau.carekobooks.module.book.infrastructure.domain.entity.BookThreadReply;
import com.github.javafaker.Faker;

import java.time.LocalDateTime;
import java.util.List;

public class BookThreadReplyFactory {

    private static final Faker faker = new Faker();

    private BookThreadReplyFactory() {
    }

    public static BookThreadReply updatedReply(BookThreadReply reply, BookThreadReplyRequest request) {
        var updatedReply = new BookThreadReply();
        updatedReply.setId(reply.getId());
        updatedReply.setContent(request.getContent());
        updatedReply.setUser(UserFactory.validUser(request.getUsername()));
        updatedReply.setThread(BookThreadFactory.validThread(request.getThreadId()));
        updatedReply.setParent(reply.getParent());
        updatedReply.setChildren(reply.getChildren());
        updatedReply.setCreatedAt(reply.getCreatedAt());
        updatedReply.setUpdatedAt(LocalDateTime.now());
        return updatedReply;
    }

    public static BookThreadReply validReply(BookThreadReplyRequest request) {
        var reply = new BookThreadReply();
        reply.setId(faker.number().randomNumber());
        reply.setContent(request.getContent());
        reply.setUser(UserFactory.validUser(request.getUsername()));
        reply.setThread(BookThreadFactory.validThread());
        reply.setParent(null);
        reply.setChildren(List.of());
        reply.setCreatedAt(LocalDateTime.now());
        reply.setUpdatedAt(LocalDateTime.now());
        return reply;
    }

    public static BookThreadReply validReply() {
        var reply = new BookThreadReply();
        reply.setId(faker.number().randomNumber());
        reply.setContent(faker.lorem().paragraph());
        reply.setUser(UserFactory.validUser());
        reply.setThread(BookThreadFactory.validThread());
        reply.setParent(null);
        reply.setChildren(List.of());
        reply.setCreatedAt(LocalDateTime.now());
        reply.setUpdatedAt(LocalDateTime.now());
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
