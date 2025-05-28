package br.com.edu.ifce.maracanau.carekobooks.factory.book.infrastructure.domain.entity;

import br.com.edu.ifce.maracanau.carekobooks.factory.user.infrastructure.domain.entity.UserFactory;
import br.com.edu.ifce.maracanau.carekobooks.module.book.application.payload.request.BookProgressRequest;
import br.com.edu.ifce.maracanau.carekobooks.module.book.infrastructure.domain.entity.BookActivity;
import br.com.edu.ifce.maracanau.carekobooks.module.book.infrastructure.domain.entity.enums.BookProgressStatus;

import java.time.LocalDateTime;
import java.util.Random;

public class BookActivityFactory {

    private BookActivityFactory() {
    }

    public static BookActivity validActivity(BookProgressRequest request) {
        var activity = new BookActivity();
        activity.setId(new Random().nextLong());
        activity.setStatus(request.getStatus());
        activity.setPageCount(request.getPageCount());
        activity.setUser(UserFactory.validUser(request.getUsername()));
        activity.setBook(BookFactory.validBook(request.getBookId()));
        activity.setCreatedAt(LocalDateTime.now());
        activity.setUpdatedAt(activity.getCreatedAt());
        return activity;
    }

    public static BookActivity validActivity() {
        var random = new Random();
        var activity = new BookActivity();
        activity.setId(random.nextLong());
        activity.setStatus(BookProgressStatus.READING);
        activity.setPageCount(random.nextInt());
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
