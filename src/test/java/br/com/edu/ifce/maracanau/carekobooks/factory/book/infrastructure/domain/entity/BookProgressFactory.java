package br.com.edu.ifce.maracanau.carekobooks.factory.book.infrastructure.domain.entity;

import br.com.edu.ifce.maracanau.carekobooks.factory.user.infrastructure.domain.entity.UserFactory;
import br.com.edu.ifce.maracanau.carekobooks.module.book.application.payload.request.BookProgressRequest;
import br.com.edu.ifce.maracanau.carekobooks.module.book.infrastructure.domain.entity.BookProgress;
import br.com.edu.ifce.maracanau.carekobooks.module.book.infrastructure.domain.entity.enums.BookProgressStatus;

import java.time.LocalDateTime;
import java.util.Random;

public class BookProgressFactory {

    private static final int DEFAULT_BOOK_PAGE_COUNT = 500;
    private static final int EXCEEDING_PAGE_COUNT = 150;
    private static final int MAX_PAGE_COUNT_FOR_EXCEEDING_TEST = 100;

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
        updatedProgress.setBook(BookFactory.validBook(request.getBookId()));
        updatedProgress.setCreatedAt(progress.getCreatedAt());
        updatedProgress.setUpdatedAt(LocalDateTime.now());
        return updatedProgress;
    }

    public static BookProgress validProgress() {
        var random = new Random();
        var progress = new BookProgress();
        progress.setId(random.nextLong());
        progress.setStatus(BookProgressStatus.READING);
        progress.setIsFavorite(random.nextBoolean());
        progress.setScore(random.nextInt(6));
        progress.setPageCount(random.nextInt(DEFAULT_BOOK_PAGE_COUNT));
        progress.setUser(UserFactory.validUser());

        var book = BookFactory.validBook();
        book.setPageCount(DEFAULT_BOOK_PAGE_COUNT);
        progress.setBook(book);

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
        progress.getBook().setPageCount(MAX_PAGE_COUNT_FOR_EXCEEDING_TEST);
        progress.setPageCount(EXCEEDING_PAGE_COUNT);
        return progress;
    }

    public static BookProgress validProgressWithNullPageCount() {
        var progress = validProgress();
        progress.setPageCount(null);
        progress.getBook().setPageCount(null);
        return progress;
    }

}
