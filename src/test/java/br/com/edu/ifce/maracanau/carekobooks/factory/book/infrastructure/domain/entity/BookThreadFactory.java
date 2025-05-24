package br.com.edu.ifce.maracanau.carekobooks.factory.book.infrastructure.domain.entity;

import br.com.edu.ifce.maracanau.carekobooks.factory.user.infrastructure.domain.entity.UserFactory;
import br.com.edu.ifce.maracanau.carekobooks.module.book.infrastructure.domain.entity.BookThread;

import java.util.Random;
import java.util.UUID;

public class BookThreadFactory {

    private BookThreadFactory() {
    }

    public static BookThread validThread() {
        var random = new Random();
        var thread = new BookThread();
        thread.setId(random.nextLong());
        thread.setTitle(UUID.randomUUID().toString());
        thread.setDescription(UUID.randomUUID().toString());
        thread.setBook(BookFactory.validBook());
        thread.setUser(UserFactory.validUser());
        thread.setReplies(null);
        return thread;
    }

    public static BookThread invalidReviewByEmptyUser() {
        var thread = validThread();
        thread.setUser(null);
        return thread;
    }

    public static BookThread invalidReviewByEmptyBook() {
        var thread = validThread();
        thread.setBook(null);
        return thread;
    }
}
