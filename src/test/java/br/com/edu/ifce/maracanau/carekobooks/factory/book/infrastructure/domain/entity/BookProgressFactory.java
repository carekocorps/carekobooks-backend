package br.com.edu.ifce.maracanau.carekobooks.factory.book.infrastructure.domain.entity;

import br.com.edu.ifce.maracanau.carekobooks.factory.user.infrastructure.domain.entity.UserFactory;
import br.com.edu.ifce.maracanau.carekobooks.module.book.application.payload.request.BookProgressRequest;
import br.com.edu.ifce.maracanau.carekobooks.module.book.infrastructure.domain.entity.Book;
import br.com.edu.ifce.maracanau.carekobooks.module.book.infrastructure.domain.entity.BookProgress;
import br.com.edu.ifce.maracanau.carekobooks.module.book.infrastructure.domain.entity.enums.BookProgressStatus;
import br.com.edu.ifce.maracanau.carekobooks.module.user.infrastructure.domain.entity.User;
import com.github.javafaker.Faker;

import java.time.LocalDateTime;
import java.util.Random;

public class BookProgressFactory {

    private static final Faker faker = new Faker();

    private BookProgressFactory() {
    }

    public static BookProgress updatedProgress(BookProgress progress, BookProgressRequest request) {
        var updatedProgress = new BookProgress();
        updatedProgress.setId(progress.getId());
        updatedProgress.setStatus(request.getStatus());
        updatedProgress.setIsFavorite(request.getIsFavorite());
        updatedProgress.setScore(request.getScore());
        updatedProgress.setPageCount(request.getPageCount());
        updatedProgress.setUser(UserFactory.validUser(request.getUsername()));
        updatedProgress.setBook(BookFactory.validBook(request.getBookId(), request.getPageCount()));
        updatedProgress.setCreatedAt(progress.getCreatedAt());
        updatedProgress.setUpdatedAt(LocalDateTime.now());
        return updatedProgress;
    }

    public static BookProgress validProgressWithNullId(Book book, User user) {
        var progress = new BookProgress();
        progress.setId(faker.number().randomNumber());
        progress.setStatus(faker.options().option(BookProgressStatus.class));
        progress.setIsFavorite(faker.bool().bool());
        progress.setScore(faker.number().numberBetween(0, 100));
        progress.setPageCount(faker.number().numberBetween(0, book.getPageCount()));
        progress.setId(null);
        progress.setUser(user);
        progress.setBook(book);
        progress.setCreatedAt(LocalDateTime.now());
        progress.setUpdatedAt(LocalDateTime.now());
        return progress;
    }

    public static BookProgress validProgress() {
        var book = BookFactory.validBook();
        var user = UserFactory.validUser();
        var progress = validProgressWithNullId(book, user);
        progress.setId(faker.number().randomNumber());
        return progress;
    }

    public static BookProgress validProgress(BookProgressRequest request) {
        var progress = new BookProgress();
        progress.setId(new Random().nextLong());
        progress.setStatus(request.getStatus());
        progress.setIsFavorite(request.getIsFavorite());
        progress.setScore(request.getScore());
        progress.setPageCount(request.getPageCount());
        progress.setUser(UserFactory.validUser(request.getUsername()));
        progress.setBook(BookFactory.validBook(request.getBookId(), progress.getPageCount()));
        progress.setCreatedAt(LocalDateTime.now());
        progress.setUpdatedAt(LocalDateTime.now());
        return progress;
    }

    public static BookProgress invalidProgressByEmptyUser() {
        var progress = validProgress();
        progress.setUser(null);
        return progress;
    }

    public static BookProgress invalidProgressByEmptyBook() {
        var progress = validProgress();
        progress.setBook(null);
        return progress;
    }

    public static BookProgress invalidProgressByExceedingPageCount() {
        var progress = validProgress();
        progress.getBook().setPageCount(faker.number().numberBetween(0, 1000));
        progress.setPageCount(progress.getBook().getPageCount() + 1);
        return progress;
    }

    public static BookProgress validProgressWithNullPageCount() {
        var progress = validProgress();
        progress.setPageCount(null);
        progress.getBook().setPageCount(null);
        return progress;
    }

}
