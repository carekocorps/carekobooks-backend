package br.com.edu.ifce.maracanau.carekobooks.factory.book.infrastructure.domain.entity;

import br.com.edu.ifce.maracanau.carekobooks.factory.user.infrastructure.domain.entity.UserFactory;
import br.com.edu.ifce.maracanau.carekobooks.module.book.application.payload.request.BookProgressRequest;
import br.com.edu.ifce.maracanau.carekobooks.module.book.infrastructure.domain.entity.BookActivity;
import br.com.edu.ifce.maracanau.carekobooks.module.book.infrastructure.domain.entity.enums.BookProgressStatus;
import com.github.javafaker.Faker;

import java.time.LocalDateTime;

public class BookActivityFactory {

    private static final Faker faker = new Faker();

    private BookActivityFactory() {
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

    public static BookActivity validActivity() {
        var activity = new BookActivity();
        activity.setId(faker.number().randomNumber());
        activity.setStatus(faker.options().option(BookProgressStatus.class));
        activity.setPageCount(faker.number().numberBetween(0, 1000));
        activity.setUser(UserFactory.validUser());
        activity.setBook(BookFactory.validBook());
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
