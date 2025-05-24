package br.com.edu.ifce.maracanau.carekobooks.factory.book.infrastructure.domain.entity;

import br.com.edu.ifce.maracanau.carekobooks.factory.user.infrastructure.domain.entity.UserFactory;
import br.com.edu.ifce.maracanau.carekobooks.module.book.infrastructure.domain.entity.BookReview;

import java.time.LocalDateTime;
import java.util.Random;
import java.util.UUID;

public class BookReviewFactory {

    private BookReviewFactory() {
    }

    public static BookReview validReview() {
        var random = new Random();
        var review = new BookReview();
        review.setId(random.nextLong());
        review.setTitle(UUID.randomUUID().toString());
        review.setContent(UUID.randomUUID().toString());
        review.setScore(random.nextInt(100));
        review.setUser(UserFactory.validUser());
        review.setBook(BookFactory.validBook());
        review.setCreatedAt(LocalDateTime.now());
        review.setUpdatedAt(review.getCreatedAt());
        return review;
    }

    public static BookReview invalidReviewByEmptyUser() {
        var review = validReview();
        review.setUser(null);
        return review;
    }

    public static BookReview invalidReviewByEmptyBook() {
        var review = validReview();
        review.setBook(null);
        return review;
    }

}
