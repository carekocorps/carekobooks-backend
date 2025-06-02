package br.com.edu.ifce.maracanau.carekobooks.factory.book.infrastructure.domain.entity;

import br.com.edu.ifce.maracanau.carekobooks.factory.user.infrastructure.domain.entity.UserFactory;
import br.com.edu.ifce.maracanau.carekobooks.module.book.infrastructure.domain.entity.BookActivity;
import br.com.edu.ifce.maracanau.carekobooks.module.book.infrastructure.domain.entity.enums.BookProgressStatus;

import java.time.LocalDateTime;
import java.util.Random;

public class BookActivityFactory {

    private BookActivityFactory() {
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
