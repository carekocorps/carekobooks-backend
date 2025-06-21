package br.com.edu.ifce.maracanau.carekobooks.common.factory.module.book.infrastructure.domain.entity;

import br.com.edu.ifce.maracanau.carekobooks.common.factory.module.user.infrastructure.domain.entity.UserFactory;
import br.com.edu.ifce.maracanau.carekobooks.module.book.application.payload.request.BookThreadReplyRequest;
import br.com.edu.ifce.maracanau.carekobooks.module.book.infrastructure.domain.entity.BookThread;
import br.com.edu.ifce.maracanau.carekobooks.module.book.infrastructure.domain.entity.BookThreadReply;
import br.com.edu.ifce.maracanau.carekobooks.module.user.infrastructure.domain.entity.User;
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

    public static BookThreadReply validReplyWithNullId(BookThreadReply parent, BookThread thread, User user) {
        var reply = new BookThreadReply();
        reply.setId(null);
        reply.setContent(faker.lorem().paragraph());
        reply.setUser(user);
        reply.setThread(thread);
        reply.setParent(parent);
        reply.setChildren(List.of());
        reply.setCreatedAt(LocalDateTime.now());
        reply.setUpdatedAt(LocalDateTime.now());
        return reply;
    }

    public static BookThreadReply validReplyWithNullId(BookThread thread, User user) {
        return validReplyWithNullId(null, thread, user);
    }

    public static BookThreadReply validReplyWithNullId() {
        var thread = BookThreadFactory.validThread();
        var user = UserFactory.validUser();
        return validReplyWithNullId(thread, user);
    }

    public static BookThreadReply validReply() {
        var reply = validReplyWithNullId();
        reply.setId(faker.number().randomNumber());
        return reply;
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
