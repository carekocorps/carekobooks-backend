package br.com.edu.ifce.maracanau.carekobooks.common.factory.module.book.infrastructure.domain.entity;

import br.com.edu.ifce.maracanau.carekobooks.common.factory.module.user.infrastructure.domain.entity.UserFactory;
import br.com.edu.ifce.maracanau.carekobooks.module.book.application.payload.request.BookProgressRequest;
import br.com.edu.ifce.maracanau.carekobooks.module.book.infrastructure.domain.entity.Book;
import br.com.edu.ifce.maracanau.carekobooks.module.book.infrastructure.domain.entity.BookActivity;
import br.com.edu.ifce.maracanau.carekobooks.module.book.infrastructure.domain.entity.enums.BookProgressStatus;
import br.com.edu.ifce.maracanau.carekobooks.module.user.infrastructure.domain.entity.User;
import net.datafaker.Faker;

import java.time.LocalDateTime;

public class BookActivityFactory {

    private static final Faker faker = new Faker();

    private BookActivityFactory() {
    }

    public static BookActivity validActivityWithNullId(Book book, User user) {
        var activity = new BookActivity();
        activity.setId(null);
        activity.setStatus(faker.options().option(BookProgressStatus.class));
        activity.setPageCount(faker.number().numberBetween(0, 1000));
        activity.setUser(user);
        activity.setBook(book);
        activity.setCreatedAt(LocalDateTime.now());
        activity.setUpdatedAt(activity.getCreatedAt());
        return activity;
    }

    public static BookActivity validActivityWithNullId() {
        var book = BookFactory.validBookWithNullId();
        var user = UserFactory.validUserWithNullId();
        return validActivityWithNullId(book, user);
    }

    public static BookActivity validActivity() {
        var activity = validActivityWithNullId();
        activity.setId(faker.number().randomNumber());
        return activity;
    }

    public static BookActivity validActivity(User user) {
        var activity = validActivity();
        activity.setUser(user);
        return activity;
    }

    public static BookActivity validActivity(BookProgressRequest request) {
        var activity = new BookActivity();
        activity.setId(faker.number().randomNumber());
        activity.setStatus(request.getStatus());
        activity.setPageCount(request.getPageCount());
        activity.setUser(UserFactory.validUser(request.getUsername()));
        activity.setBook(BookFactory.validBook(request.getBookId()));
        activity.setCreatedAt(LocalDateTime.now());
        activity.setUpdatedAt(activity.getCreatedAt());
        return activity;
    }

    public static BookActivity invalidActivityByEmptyUser() {
        var activity = validActivity();
        activity.setUser(null);
        return activity;
    }

    public static BookActivity invalidActivityByEmptyBook() {
        var activity = validActivity();
        activity.setBook(null);
        return activity;
    }

}
