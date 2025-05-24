package br.com.edu.ifce.maracanau.carekobooks.factory.book.infrastructure.domain.entity;

import br.com.edu.ifce.maracanau.carekobooks.factory.user.infrastructure.domain.entity.UserFactory;
import br.com.edu.ifce.maracanau.carekobooks.module.book.infrastructure.domain.entity.BookProgress;
import br.com.edu.ifce.maracanau.carekobooks.module.book.infrastructure.domain.entity.enums.BookProgressStatus;

import java.util.Random;

public class BookProgressFactory {

    private BookProgressFactory() {
    }

    public static BookProgress validProgress() {
        var random = new Random();
        var progress = new BookProgress();
        progress.setId(random.nextLong());
        progress.setStatus(BookProgressStatus.READING);
        progress.setIsFavorite(random.nextBoolean());
        progress.setScore(random.nextInt(6));
        progress.setPageCount(random.nextInt(500));
        progress.setUser(UserFactory.validUser());
        progress.setBook(BookFactory.validBook());
        return progress;
    }

    public static BookProgress invalidReviewByEmptyUser() {
        var progress = validProgress();
        progress.setUser(null);
        return progress;
    }

    public static BookProgress invalidReviewByEmptyBook() {
        var progress = validProgress();
        progress.setBook(null);
        return progress;
    }

    public static BookProgress invalidProgressByExceedingPageCount() {
        var progress = validProgress();
        var book = progress.getBook();
        book.setPageCount(100);
        progress.setPageCount(150);
        return progress;
    }

}
